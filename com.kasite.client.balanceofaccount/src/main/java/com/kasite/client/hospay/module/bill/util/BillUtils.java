package com.kasite.client.hospay.module.bill.util;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.client.init.HisService;
import com.kasite.client.hospay.module.bill.dao.ThreePartyBalanceDao;
import com.kasite.client.hospay.module.bill.entity.bill.bo.ErrorHisBills;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.Order;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.QLCBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.httpclient.http.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @author cc TODO 订单业务帮助类
 */
@Component
public class BillUtils {

	private static final Logger logger = LoggerFactory.getLogger(BillUtils.class);

	private static volatile HisService service = null;

	@Autowired
	RequestHandlerParam requestHandlerParam;

	@Autowired
	ThreePartyBalanceDao threePartyBalanceDao;

	@Autowired
	List<HisService> hisServices;

	/**
	 * 判断当前系统为几方对账
	 * 
	 * @param partyBills
	 * @return
	 */
	public ThreePartyBalance branch(ThreePartyBalance partyBills) {
		ThreePartyBalance threePartyBills = null;
		/* 根据此节点 requestHandlerParam.severalParties 判断当前属于几方对账,然后执行相应的汇总逻辑 */
		try {
			if (Constant.THREEPARTY.equals(requestHandlerParam.severalParties)) {
				threePartyBills = returnThreePartyBalance(partyBills);
			} else if (Constant.TWOPARTY.equals(requestHandlerParam.severalParties)) {
				threePartyBills = returnTwoPartyBalance(partyBills);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return threePartyBills;
	}

	/**
	 * 处理三方汇总账单明细数据
	 * 
	 * @param partyBills
	 * @return
	 * @throws SQLException
	 */
	private ThreePartyBalance returnThreePartyBalance(ThreePartyBalance partyBills) throws SQLException {

		// 保存全流程系统生成当前订单的时间
		partyBills.setLastDate(DateOper.getNowDateTime());
		// 设置同步状态
		partyBills.setIsSyn("0");
		/* 新增订单状态异常的字段，用于后期数据的快速抽取 */
		String qlcState = partyBills.getQlcOrderState();
		String hisState = partyBills.getHisOrderState();
		String channelState = partyBills.getChannelOrderState();
		/* 以全流程订单状态为主与第三方,HIS端订单状态比对,如异常则需要保存异常状态 */
		if (!qlcState.equals(hisState) || !qlcState.equals(channelState)) {
			partyBills.setErrorState("-1");
			/* 设置默认执行状态为待执行 */
			partyBills.setExeState("0");
			logger.info("发现异常订单，异常订单的状态是:{}，订单号:{}，", partyBills.getErrorState(), partyBills.getOrderId());
			/* 根据全流程订单状态（1,2）区分当前订单属于支付订单还是退费订单,并做相应长短款的判断 */
			if (Constant.CHARGEORDERSTATE.equals(qlcState)) {
				/*
				 * 由于在汇总全流程账单明细时使用到了P_Bill表中的数据作为唯一匹配数据，来校准全流程中缺失的订单数据，
				 * 所以遇到订单为支付订单且是长款订单时，则需进行校验
				 * 1.当前订单qlcState为1且hisState为空，由此可知当前订单是长款订单
				 * 2.使用当前订单的orderId和channelOrderId到P_Bill表中进行查询是否存在退费订单orderType为2
				 * 如果存在则证明1的观点 *
				 */
				if (Constant.CHARGEORDERSTATE.equals(partyBills.getQlcOrderState())
						&& StringUtils.isBlank(partyBills.getHisOrderId())) {
					if (threePartyBalanceDao.queryOrderExistBill(partyBills.getOrderId(), Constant.REFUNDORDERSTATE)) {
						/* 修改当前的退费订单为2[账平(处置后)] */
						partyBills.setBizType("2");
						partyBills.setExeState("2");
					}
				}
				if (StringUtil.isNotBlank(qlcState) && StringUtil.isNotBlank(hisState)
						&& StringUtil.isBlank(channelState)) {
					/* 短款 */
					partyBills.setCheckState("-1");
				} else if (StringUtil.isNotBlank(qlcState) && StringUtil.isNotBlank(channelState)
						&& StringUtil.isBlank(hisState)) {
					/* 长款 */
					partyBills.setCheckState("1");
				}
			} else if (Constant.REFUNDORDERSTATE.equals(qlcState)) {
				/*
				 * 由于在汇总全流程账单明细时使用到了P_Bill表中的数据作为唯一匹配数据，来校准全流程中缺失的订单数据，
				 * 所以遇到订单为退费订单时就需要做跨日账单补齐的动作
				 * 1.异常的退款订单需要到p_bill表与P_His_Bill中去查询是否存在对应的支付订单 1.1
				 * 存在：则按照现有的流程继续不做特殊处理 1.2
				 * 不存在：查询是否在P_BILL表中存在支付订单而在P_HIS_BILL表中不存在支付订单 1.2.1
				 * YES：则说明该笔退费订单是由于之前的某笔支付的长款订单触发的业务,直接账平（处置后）
				 *
				 ** *
				 */
				if (Constant.REFUNDORDERSTATE.equals(partyBills.getQlcOrderState())
						&& StringUtils.isBlank(partyBills.getHisOrderId())) {
					// 退款订单存在微信支付订单在HIS也存在支付订单,则不进行处理
					if (threePartyBalanceDao.queryOrderExistBill(partyBills.getOrderId(), Constant.CHARGEORDERSTATE)
							&& threePartyBalanceDao.queryOrderExistHisBill(partyBills.getOrderId(), "1")) {
						/* 不做特殊处理 */
						partyBills.setBizType("");
						partyBills.setExeState("0");
					} else if (threePartyBalanceDao.queryOrderExistBill(partyBills.getOrderId(),
							Constant.CHARGEORDERSTATE)
							&& !threePartyBalanceDao.queryOrderExistHisBill(partyBills.getOrderId(), "1")) {
						/* 修改当前的退费订单为2[账平(处置后)] */
						partyBills.setBizType("2");
						partyBills.setExeState("2");
					}
				}
				if (StringUtil.isNotBlank(qlcState) && StringUtil.isNotBlank(hisState)
						&& StringUtil.isBlank(channelState)) {
					/* 长款 */
					partyBills.setCheckState("1");
				} else if (StringUtil.isNotBlank(qlcState) && StringUtil.isNotBlank(channelState)
						&& StringUtil.isBlank(hisState)) {
					/* 短款 */
					partyBills.setCheckState("-1");
				}
			}
			/* 保存异常订单之前需要先判断当前异常订单是否已经存在 */
			if (!threePartyBalanceDao.queryExceptionBillWhetherNull(partyBills)) {
				logger.info("开始保存异常订单");
				threePartyBalanceDao.insertExceptionBill(partyBills);
				logger.info("结束异常订单保存！！！");
			}
		} else {
			/* 正常 */
			partyBills.setCheckState("0");
			partyBills.setErrorState("1");
			/* 设置默认执行状态为待执行 */
			partyBills.setExeState("0");
		}
		return partyBills;
	}

	/**
	 * 处理两方汇总账单明细数据
	 * 
	 * @param partyBills
	 * @return
	 * @throws SQLException
	 */
	private ThreePartyBalance returnTwoPartyBalance(ThreePartyBalance partyBills) throws SQLException {
		//************************************
		/* 亚心医院个性化参数修改*/
//		String orderMemo = partyBills.getOrderMemo();
//		JSONObject result = JSONObject.parseObject(orderMemo);
//		partyBills.setOperatorName(result.getString("病人姓名"));
//		partyBills.setCardNo(result.getString("病案号"));
		//*************************************
		// 保存全流程系统生成当前订单的时间
		partyBills.setLastDate(DateOper.getNowDateTime());
		// 设置同步状态
		partyBills.setIsSyn("0");
		/* 新增订单状态异常的字段，用于后期数据的快速抽取 */
		String qlcState = partyBills.getQlcOrderState();
		String channelState = partyBills.getChannelOrderState();
		/* 以全流程订单状态为主与第三方订单状态比对,如异常则需要保存异常状态 */
		if (!qlcState.equals(channelState)) {
			partyBills.setErrorState("-1");
			/* 设置默认执行状态为待执行 */
			logger.info("发现异常订单，异常订单的状态是:{}，订单号:{}，", partyBills.getErrorState(), partyBills.getOrderId());
			partyBills.setExeState("0");
			/* 根据全流程订单状态（1,2）区分当前订单属于支付订单还是退费订单,并做相应长短款的判断 */
			if (Constant.CHARGEORDERSTATE.equals(channelState)) {
				/*
				 * P_Bill表中的数据作为唯一准确数据，来校准全流程中缺失的订单数据，所以遇到订单为支付订单且是长款订单时，则需进行校验
				 * 1.当前订单channelState为1且qlcState为空，由此可知当前订单是长款订单
				 * 2.使用当前订单的channelOrderId到P_Bill表中进行查询是否存在退费订单orderType为2
				 * 如果存在则证明1的观点 *
				 */
				if (Constant.CHARGEORDERSTATE.equals(partyBills.getChannelOrderState())) {
					if (threePartyBalanceDao.queryOrderExistBill(partyBills.getChannelOrderId(),
							Constant.REFUNDORDERSTATE)) {
						/* 修改当前的退费订单为2[账平(处置后)] */
						partyBills.setBizType("2");
						partyBills.setExeState("2");
					}
				}
				if (StringUtil.isNotBlank(qlcState) && StringUtil.isBlank(channelState)) {
					/* 短款 */
					partyBills.setCheckState("-1");
				} else if (StringUtil.isBlank(qlcState) && StringUtil.isNotBlank(channelState)) {
					/* 长款 */
					partyBills.setCheckState("1");
				}
			} else if (Constant.REFUNDORDERSTATE.equals(channelState)) {
				/*
				 * 由于P_Bill表中的数据作为唯一准确数据，来校准全流程中缺失的订单数据，
				 * 所以遇到订单为退费订单时就需要做跨日账单补齐的动作 1.异常的退款订单需要到p_bill表中去查询是否存在对应的支付订单
				 * 1.1 存在：则按照现有的流程继续不做特殊处理
				 * YES：则说明该笔退费订单是由于之前的某笔支付的长款订单触发的业务,直接账平（处置后） *
				 */
				if (Constant.REFUNDORDERSTATE.equals(partyBills.getChannelOrderState())) {
					// 退款订单存在微信支付订单,则不进行处理
					if (threePartyBalanceDao.queryOrderExistBill(partyBills.getChannelOrderId(),
							Constant.CHARGEORDERSTATE)) {
						/* 不做特殊处理 */
						partyBills.setExeState("0");
						partyBills.setBizType("");

					} else if (threePartyBalanceDao.queryOrderExistBill(partyBills.getOrderId(),
							Constant.CHARGEORDERSTATE)
							&& !threePartyBalanceDao.queryOrderExistHisBill(partyBills.getOrderId(), "1")) {
						/* 修改当前的退费订单为2[账平(处置后)] */
						partyBills.setExeState("2");
						partyBills.setBizType("2");

					}
				}
				if (StringUtil.isNotBlank(qlcState) && StringUtil.isBlank(channelState)) {
					/* 长款 */
					partyBills.setCheckState("1");
				} else if (StringUtil.isBlank(qlcState) && StringUtil.isNotBlank(channelState)) {
					/* 短款 */
					partyBills.setCheckState("-1");
				}
			}
			/* 保存异常订单之前需要先判断当前异常订单是否已经存在 */
			if (!threePartyBalanceDao.queryExceptionBillWhetherNull(partyBills)) {
				logger.info("开始保存异常订单");
				threePartyBalanceDao.insertExceptionBill(partyBills);
				logger.info("结束异常订单保存！！！");
			}
		} else {
			/* 正常 */
			partyBills.setCheckState("0");
			partyBills.setErrorState("1");
			/* 设置默认执行状态为待执行 */
			partyBills.setExeState("0");
		}
		return partyBills;
	}

	/**
	 * 查询出来的异常账单由于会缺少一些展示字段,所以这边需要到o_order表中获取那些展示字段 然后重新set到QLCBalance对象当中
	 *
	 * @param order
	 * @param qlcBalance
	 *
	 * @return
	 */
	public static QLCBalance changeQLCBalance(Order order, QLCBalance qlcBalance) {
		qlcBalance.setServiceId(order.getServiceId());
		qlcBalance.setReserveId(order.getReserveId());
		qlcBalance.setPriceName(order.getPriceName());
		qlcBalance.setOrderMemo(order.getOrderMemo());
		qlcBalance.setCardNo(order.getCardNo());
		qlcBalance.setOperator(order.getOperator());
		qlcBalance.setOperatorName(order.getOperatorName());
		return qlcBalance;
	}

	/*
	 * 返回业务状态
	 * 
	 * @param type
	 * @param exeState
	 * @return
	 */
	public String changeType(String type, String exeState) {
		String bizType;
		if (Constant.BIZTYPEONE.equals(type) && Constant.EXESTATEFINISH.equals(exeState)) {
			bizType = "2";
		} else if (Constant.BIZTYPEONE.equals(type) && Constant.EXESTATEFINISH.equals(exeState)) {
			bizType = "1";
		} else if (Constant.BIZTYPEONE.equals(type) && Constant.EXESTATEBEGIN.equals(exeState)) {
			bizType = "3";
		} else if (Constant.BIZTYPETWO.equals(type) && Constant.EXESTATEBEGIN.equals(exeState)) {
			bizType = "4";
		} else if (Constant.BIZTYPEONE.equals(type) && Constant.EXESTATEWAIT.equals(exeState)) {
			bizType = "5";
		} else if (Constant.BIZTYPETWO.equals(type) && Constant.EXESTATEWAIT.equals(exeState)) {
			bizType = "6";
		} else if (Constant.BIZTYPETHREE.equals(type) && Constant.EXESTATEWAIT.equals(exeState)) {
			bizType = "7";
		} else if (Constant.BIZTYPETHREE.equals(type) && Constant.EXESTATEBEGIN.equals(exeState)) {
			bizType = "8";
		} else if (Constant.BIZTYPETHREE.equals(type) && Constant.EXESTATEFINISH.equals(exeState)) {
			bizType = "9";
		} else {
			bizType = "0";
		}
		return bizType;
	}

	/*
	 * 改变汇总账单明细数据
	 * 根据flag来判断当前为更新数据还是插入数据
	 */
	public ThreePartyBalance changePartyBillData(ThreePartyBalance errorThreePartyBalance,ErrorHisBills bill,boolean flag) {
		if (flag){
			errorThreePartyBalance.setHisOrderId(bill.getHisOrderId());
			errorThreePartyBalance.setErrorState("1");
			errorThreePartyBalance.setHisOrderState("1");
			// 匹配HIS数据，更新状态为账平
			errorThreePartyBalance.setCheckState("0");
			// 设置同步状态
			errorThreePartyBalance.setIsSyn("1");
		}else {
			logger.info("开始为当前异常订单设置参数!!!");
			errorThreePartyBalance.setLastDate(DateOper.getNowDateTime());
			/* 设置异常订单状态 */
			errorThreePartyBalance.setErrorState("-1");
			errorThreePartyBalance.setOrderId(bill.getOrderId());
			errorThreePartyBalance.setServiceId(bill.getServiceId());
			errorThreePartyBalance.setReserveId(bill.getReserveId());
			errorThreePartyBalance.setPriceName(bill.getPriceName());
			errorThreePartyBalance.setOrderMemo(bill.getOrderMemo());
			errorThreePartyBalance.setCardNo(bill.getCardNo());
			errorThreePartyBalance.setOperator(bill.getOperator());
			errorThreePartyBalance.setOperatorName(bill.getOperatorName());
			errorThreePartyBalance.setCreateDate(bill.getCreateDate());
			errorThreePartyBalance.setChannelId(bill.getChannelId());
			if (Constant.PAYBIZTYPE.equals(bill.getOrderType())) {
				/* 短款 */
				errorThreePartyBalance.setReceivableMoney(bill.getReceivableMoney());
				errorThreePartyBalance.setCheckState("-1");
				errorThreePartyBalance.setQlcOrderState("1");
				errorThreePartyBalance.setHisOrderState("1");
			} else {
				/* 长款 */
				errorThreePartyBalance.setCheckState("1");
				errorThreePartyBalance.setRefundMoney(bill.getReceivableMoney());
				errorThreePartyBalance.setQlcOrderState("2");
				errorThreePartyBalance.setHisOrderState("2");
			}
			errorThreePartyBalance.setHisOrderId(bill.getHisOrderId());
			errorThreePartyBalance.setChannelOrderId("");
			errorThreePartyBalance.setPayUpdateKey("");
			errorThreePartyBalance.setChannelOrderState("");
			// 设置同步状态
			errorThreePartyBalance.setIsSyn("2");
		}
		return errorThreePartyBalance;
	}

	/*
	 * 通过配置文件匹配获得当前具体的HisClient实例
	 * @return
	 */
	public HisService getHisInstance() {
		if (service == null) {
			synchronized (HisService.class) {
				if (service == null) {
					for (HisService factory : hisServices) {
						if (requestHandlerParam.hisClientInstance.equals(factory.getClass().getSimpleName())) {
							service = factory;
							break;
						}
					}
				}
			}
		}
		return service;
	}

	/* 返回JSONObject对象数据*/
	public JSONObject returnMsg(String code,String msg){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("RespCode",code);
		jsonObject.put("RespMessage",msg);
		return jsonObject;
	}
}
