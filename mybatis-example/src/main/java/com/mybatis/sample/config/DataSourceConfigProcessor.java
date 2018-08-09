package com.mybatis.sample.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class DataSourceConfigProcessor implements BeanFactoryPostProcessor {
	
	public static void main(String[] args) throws IOException {
		new DataSourceConfigProcessor().test();
	}
	public void test() throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		
//		InputStream in = DataSourceConfigProcessor.class.getResourceAsStream("/application.yml");
//		JsonNode node = mapper.readTree(in);
//		System.out.println(node.toString());
//		JsonNode inner = node.path("context.datasource.biz");
//		inner.forEach( n -> {
//			 System.out.println(n.get("driverClassName"));
//		});
//		List<DataSourceVo> list  = mapper.readValue(inner.toString(), List.class); 
//		System.out.println(list);
		Resource resources = new ClassPathResource("application.yml");
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(resources);
        yaml.setDocumentMatchers(new ProfileDocumentMatcher("spring.profiles"));
        //propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        Map<String, Object> map = yaml.getObject();
        
        System.out.println("====:" + map);
        System.out.println("=ffff===:" +map.get("context.datasource.framework"));
        
        List inner = (ArrayList) map.get("context.datasource.biz");
        final List<DataSourceVo> list = new java.util.ArrayList<>();
        inner.forEach( item ->{
        	 DataSourceVo vo = new DataSourceVo();
        	 BeanUtils.copyProperties(vo, item);
        	 list.add(vo);
        });
		System.out.println("-----------vo-----------");
		System.out.println(list);
		
		ObjectMapper m = new ObjectMapper();
		List<DataSourceVo> list2 = m.convertValue(inner, List.class);
		System.out.println("-----------vo2-----------");
		System.out.println(list2);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		GenericBeanDefinition bd = new GenericBeanDefinition();
		beanFactory.registerSingleton("dataSource", dataSource());
		beanFactory.registerSingleton("transactionManager", transactionManager());
		try {
			beanFactory.registerSingleton("sqlSessionFactory", sqlSessionFactory());
			beanFactory.registerSingleton("sqlSessionTemplate", sqlSessionTemplate());
			beanFactory.registerSingleton("sqlSessionTemplateHolder", sqlSessionTemplateHolder());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	    public DataSource dataSource() {
	        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	        dataSource.setDriverClass(org.postgresql.Driver.class);
	        dataSource.setUsername("fw");
	        dataSource.setUrl("jdbc:postgresql://localhost:5432/devfwdb");
	        dataSource.setPassword("fw1234");
	        return dataSource;
	    }

	    public DataSourceTransactionManager transactionManager() {
	        return new DataSourceTransactionManager(dataSource());
	    }

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

	    public SqlSessionTemplate sqlSessionTemplate () throws Exception {
	    	SqlSessionTemplate t = new SqlSessionTemplate(sqlSessionFactory());
	    	return t;
	    }
	    
		/**
		 * 모든 SqlSessionTemplate을 가지고 있는 holder
		 * @throws Exception 
		 */
		public SqlSessionTemplateHolder sqlSessionTemplateHolder() throws Exception  {
			Map<String,SqlSessionTemplate> dataSourceMap = new HashMap<String, SqlSessionTemplate>();
			dataSourceMap.put("SLT-PG-DataSource01", sqlSessionTemplate());
			
			SqlSessionTemplateHolder holder = new SqlSessionTemplateHolder();
			holder.setSqlSessionTemplate(dataSourceMap);
			return holder;
		}
}
