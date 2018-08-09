package com.mybatis.sample.config.old;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mybatis.sample.config.SqlSessionTemplateHolder;

//@Configuration
//@MapperScan( basePackages = "com.mybatis.sample.mapper" )
public class DataConfig {

    //@Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUsername("fw");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/devfwdb");
        dataSource.setPassword("fw1234");
        return dataSource;
    }

    //@Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    //@Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/mapper/**/*Mapper.xml");
        sessionFactory.setMapperLocations(resources);
        sessionFactory.setConfigLocation(resolver.getResource("classpath:config/mybatis/mybatis-config.xml"));
       // sessionFactory.setTypeAliasesPackage("org.lanyonm.playground.domain");
        return sessionFactory.getObject();
    }

    //@Bean
    public SqlSessionTemplate sqlSessionTemplate () throws Exception {
    	SqlSessionTemplate t = new SqlSessionTemplate(sqlSessionFactory());
    	return t;
    }
    
	/**
	 * 모든 SqlSessionTemplate을 가지고 있는 holder
	 * @throws Exception 
	 */
	//@Bean
	public SqlSessionTemplateHolder sqlSessionTemplateHolder() throws Exception  {
		Map<String,SqlSessionTemplate> dataSourceMap = new HashMap<String, SqlSessionTemplate>();
		dataSourceMap.put("SLT-PG-DataSource01", sqlSessionTemplate());
		
		SqlSessionTemplateHolder holder = new SqlSessionTemplateHolder();
		holder.setSqlSessionTemplate(dataSourceMap);
		return holder;
	}

}