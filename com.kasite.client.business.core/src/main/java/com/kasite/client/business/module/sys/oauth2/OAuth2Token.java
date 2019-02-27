package com.kasite.client.business.module.sys.oauth2;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author daiys
 * @email 343675979@qq.com
 */
public class OAuth2Token implements AuthenticationToken {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4095493097281368632L;
	private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
