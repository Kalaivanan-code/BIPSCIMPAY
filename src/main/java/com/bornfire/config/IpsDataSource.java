package com.bornfire.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
@EnableTransactionManagement
@ConfigurationProperties("spring.datasource")
//@EnableConfigurationProperties(DataSourceProperties.class)


@EnableJpaRepositories(basePackages = "com.bornfire.entity", entityManagerFactoryRef = "datasrc", transactionManagerRef = "datasrcTransactionManager")
public class IpsDataSource {
	public static final String CONFIGURATION_PREFIX = "spring.datasource";

	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String url;
	
	@NotNull
	private String drverclass;
	
	@NotNull
	private int minimumIdle;
	@NotNull
	private int maximumPoolSize;
	@NotNull
	private int idleTimeout;
	@NotNull
	private int maxLifetime;
	@NotNull
	private int connectionTimeout;
	@NotNull
	private String poolName;
	
	/*@Autowired
	private DataSourceProperties properties;*/
	
	
	
	public void setUsername(String username) {
		this.username = username;
	}



	public void setMinimumIdle(int minimumIdle) {
		this.minimumIdle = minimumIdle;
	}


	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}



	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}



	public void setMaxLifetime(int maxLifetime) {
		this.maxLifetime = maxLifetime;
	}



	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}



	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}


	public void setDrverclass(String drverclass) {
		this.drverclass = drverclass;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	@Bean
    public LocalSessionFactoryBean datasrc() throws SQLException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(srcdataSource());
        sessionFactory.setPackagesToScan("com.bornfire.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
 
        return sessionFactory;
    }
	
	private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();       
        hibernateProperties.setProperty(
          "hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
       // hibernateProperties.setProperty("javax.persistence.query.timeout","15000");
        return hibernateProperties;
    }
	

	@Bean
	DataSource srcdataSource() throws SQLException {
		 
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setJdbcUrl(url);
		dataSource.setDriverClassName(drverclass);
		dataSource.setLoginTimeout(200);
		dataSource.setMinimumIdle(minimumIdle);
		dataSource.setMaximumPoolSize(maximumPoolSize);
		dataSource.setIdleTimeout(idleTimeout);
		dataSource.setMaxLifetime(maxLifetime);
		dataSource.setConnectionTimeout(connectionTimeout);
		dataSource.setPoolName(poolName);
		/*dataSource.setMinimumIdle(10);
		dataSource.setMaximumPoolSize(5000);
		dataSource.setIdleTimeout(30000);
		dataSource.setMaxLifetime(2000000);
		dataSource.setConnectionTimeout(30000);
		dataSource.setPoolName("HikaryTest");*/
		
		
		return dataSource;
	}
	
	/*@SuppressWarnings("rawtypes")
	@ConfigurationProperties(prefix = IpsDataSource.CONFIGURATION_PREFIX)
	@Bean
	public DataSource srcdataSource() {
	    DataSourceBuilder factory = DataSourceBuilder
	            .create(this.properties.getClassLoader())
	            .driverClassName(this.properties.getDriverClassName())
	            .url(this.properties.getUrl())
	            .username(this.properties.getUsername())
	            .password(this.properties.getPassword());
	    return factory.build();
	}*/

	@Bean
	public PlatformTransactionManager datasrcTransactionManager() throws SQLException {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(datasrc().getObject());
		return transactionManager;
	}
	

}
