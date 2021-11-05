package spittr.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "spittr", excludeFilters = {
		@Filter( type =FilterType.ANNOTATION, value=EnableWebMvc.class)})
public class RootConfig {
	
	@Bean
    DataSource dataSource() {
        DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup("java:comp/env/jdbc/spitterdb", DataSource.class);
        } catch (NamingException e) {
            
        }
        return dataSource;
    }
	
	@Bean  //Hibernate session
    public LocalSessionFactoryBean sessionFactory() {
		
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        
        sessionFactory.setDataSource(dataSource());
        
        sessionFactory.setPackagesToScan("spittr.domain");
        
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }
	
	 private final Properties hibernateProperties() {
		 
	        Properties hibernateProperties = new Properties();
	        
	        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");

	        return hibernateProperties;
	    }
	 
	 
	 @Bean
	    public PlatformTransactionManager htxManager() {
	        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
	        transactionManager.setSessionFactory(sessionFactory().getObject());
	        return transactionManager;
	    }
	 
	 @Bean
	 public MessageSource messageSource() {
		 ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		 messageSource.setBasename("messages");
		 messageSource.setDefaultEncoding("UTF-8");
		 return messageSource;
	 }

}
