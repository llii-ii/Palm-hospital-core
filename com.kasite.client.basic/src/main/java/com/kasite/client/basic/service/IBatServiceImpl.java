package com.kasite.client.basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.client.basic.dao.IBatUserMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.serviceinterface.module.basic.IBatService;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.BatUser;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddBatUser;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBatUser;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBatUser;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * @author lq
 * @version 1.0 2017-7-24 下午2:29:38
 */
@Service("bat.BatApi")
public class IBatServiceImpl extends AbstractBatService implements IBatService {

	@Autowired
	private IBatUserMapper batUserMapper;
	@Autowired
	IBatUserLocalCache batUserLocalCache;
	
//	/**
//	 * 添加公众账号
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	@Override
//	public String addAccount(InterfaceMessage msg) {
//		return null;
//	}
//	/**
//	 * 删除公众账号
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	@Override
//	public String deleteAccount(InterfaceMessage msg) {
//		try {
//			ReqDeleteBatAccount vo = null;
//			try {
//				vo = new ReqDeleteBatAccount(msg);
//			} catch (AbsHosException e) {
//				LogUtil.error(log, e);
//				e.printStackTrace();
//				return CommonUtil.getRetVal(KstHosConstant.DELETEACCOUNT, RetCode.Common.ERROR_PARAM.getCode(),
//						e.getLocalizedMessage());
//			}
//			int result = 0;
//			JdbcConnection conn = null;
//			try {
//				// 获得数据库链接
//				conn = DB.me().getConnection(MyDatabaseEnum.hos);
//				conn.beginTransaction();
//				// 删除数据
//				result = BatDao.deleteAccount(conn, vo.getAccountId());
//				if (result > 0) {
//					// 删除用户
//					BatDao.deleteUserByAccountId(conn, vo.getAccountId());
//				}
//				conn.commitTransaction();// 提交事务
//			} catch (Exception e) {
//				e.printStackTrace();
//				LogUtil.error(log, e);
//				return CommonUtil.getRetVal(KstHosConstant.DELETEACCOUNT, RetCode.Common.ERROR_SQLEXECERROR.getCode(),
//						"删除微信账号表异常：" + e.getMessage());
//			} finally {
//				if (conn != null) {
//					conn.close();// 关闭数据库链接
//				}
//			}
//			// 删除成功
//			if (result > 0) {
//				return CommonUtil.getRetVal(KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000.getCode(),
//						RetCode.Success.RET_10000.getMessage());
//			} else {
//				return CommonUtil.getRetVal(KstHosConstant.DELETEACCOUNT, RetCode.Common.ERROR_SQLEXECERROR.getCode(),
//						"删除微信账号表失败");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LogUtil.error(log, e);
//			return CommonUtil.getRetVal(KstHosConstant.DELETEACCOUNT, RetCode.Common.ERROR_UNKNOWN.getCode(),
//					"系统异常：" + e.getMessage());
//		}
//	}

//	/**
//	 * 更新公众账号
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	@Override
//	public String updateAccount(InterfaceMessage msg) {
//		return null;
//	}

	/**
	 * 添加用户
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String AddBatUser(InterfaceMessage msg) throws Exception {
		ReqAddBatUser vo = new ReqAddBatUser(msg);
		return this.addBatUser(new CommonReq<ReqAddBatUser>(vo)).toResult();
	}

	/**
	 * 查询用户
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryBatUser(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryBatUser> paramReq = new CommonReq<ReqQueryBatUser>(new ReqQueryBatUser(msg));
			CommonResp<RespQueryBatUser> respComm = this.queryBatUser(paramReq);
			return respComm.toResult();
	}

	@Override
	public CommonResp<RespMap> addBatUser(CommonReq<ReqAddBatUser> paramReq) throws Exception{
		ReqAddBatUser req = paramReq.getParam();
		BatUser batUser = new BatUser();
		
		BeanCopyUtils.copyProperties(req, batUser, null);
		batUser.setChannelId(req.getClientId());
		batUser.setOperatorId(req.getOpenId());
		batUser.setConfigKey(req.getConfigKey());
		
//		batUser.setChannelId(req.getAuthInfo().getClientId());
//		batUser.setOpenId(req.getOpenId());
//		batUser.setOperatorId(req.getOpenId());
//		batUser.setOperatorName(req.getOpenId());
//		batUser.setState(1);
//		batUser.setSubscribe(req.getSubscribe());
//		batUser.setSubscribeTime(req.getSubscribeTime());
//		batUser.setUnionId(req.getUnionId());
		
		Example example = new Example(BatUser.class);
		example.createCriteria().andEqualTo("openId", req.getOpenId());
		int count = batUserMapper.selectCountByExample(example);
		if (count > 0) {
			batUserMapper.updateByExampleSelective(batUser, example);
		} else {
			batUser.setId(CommonUtil.getGUID());
			batUserMapper.insertSelective(batUser);
		}
		return new CommonResp<>(paramReq, KstHosConstant.ADDUSER, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQueryBatUser> queryBatUser(CommonReq<ReqQueryBatUser> paramReq) throws Exception{
		ReqQueryBatUser req = paramReq.getParam();
		Example example = new Example(BatUser.class);
		example.createCriteria().andEqualTo("openId", req.getOpenId());
		BatUser user = batUserMapper.selectOneByExample(example);
		if(user==null) {
			return new CommonResp<RespQueryBatUser>(paramReq,KstHosConstant.QUERYUSER, RetCode.Basic.ERROR_NOTFINDMEMBER, "没有找到用户信息：opId"+req.getOpenId());
		}
		BatUserCache userCache = batUserLocalCache.get(user.getOpenId());
		if(userCache==null) {
			//缓存中不存在用户信息，抛出异常提示重新登录
			return  new CommonResp<RespQueryBatUser>(paramReq, KstHosConstant.QUERYUSER, RetCode.Common.OAUTH_ERROR_NOAUTHODINFO,"会话超时，请重新点击公众号菜单进入。");
		}
		RespQueryBatUser resp = new RespQueryBatUser();
		//属性值复制
		BeanCopyUtils.copyProperties(userCache, resp, null);
		BeanCopyUtils.copyProperties(user, resp, null);
		return new CommonResp<RespQueryBatUser>(paramReq,KstHosConstant.QUERYUSER, RetCode.Success.RET_10000, resp);
	}

}
