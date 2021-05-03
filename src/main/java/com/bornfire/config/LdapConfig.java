package com.bornfire.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {
	
    @Autowired
    Environment env;

	@Bean
	public LdapContextSource ipsLdapContextSource() {
		LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.base"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.user"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        return contextSource;
	}

	@Bean
	public LdapTemplate ipsLdapTemplate() {

		LdapTemplate tmp = new LdapTemplate();
		tmp.setContextSource(ipsLdapContextSource());
		
		
		return tmp;
	}
	
	

}
