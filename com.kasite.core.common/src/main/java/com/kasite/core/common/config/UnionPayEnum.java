package com.kasite.core.common.config;
public enum UnionPayEnum{
	merId,//商户号
	accessType,//接入类型0：普通商户直连接入 2：平台类商户接入，目前使用0
	secureKey,// 安全密钥(SHA256和SM3计算时使用) ,目前暂时是不到，冗余
	encryptCert,//加密公钥证书
	encryptCertPath,//加密公钥证书路径.cer
	signCert,// 签名证书.pfx
	signCertPath,// 签名证书路径.pfx
	signCertPwd,//签名证书密码
	signCertType,//签名证书类型
	middleCert,// 中级证书  
	rootCert,// 根证书
	middleCertPath,// 中级证书路径  .cer
	rootCertPath,// 根证书路径.cer
	encryptTrackKeyModulus,//磁道加密公钥模数. ,目前暂时是不到，冗余
	encryptTrackKeyExponent,// 磁道加密公钥指数.,目前暂时是不到，冗余
	validateCertDir,//验证签名公钥证书目录.目前暂时是不到，冗余
	ifValidateCNName,//	 是否验证验签证书CN，除了false都验  
	ifValidateRemoteCert,// 是否验证https证书，测试时候false，上线的时候true，（上线有问题，万不得以才能false）
	fileType,//文件类型，一般商户填写00即可
	;
	}