package com.bornfire.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InformixConnectionManager {
	
	@Autowired
	Environment env;
	Connection conn;

	public Connection getConnection() {
	
		try
		{
			Class.forName(env.getProperty("spring.informix.driverclass"));
			String url = env.getProperty("spring.informix.url")+":"+
			"INFORMIXSERVER="+env.getProperty("spring.informix.server")+";"+
			"user="+env.getProperty("spring.informix.username")+";"+
			"password="+env.getProperty("spring.informix.password")+";";
			
			conn = DriverManager.getConnection(url);
			
	    }catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
}
