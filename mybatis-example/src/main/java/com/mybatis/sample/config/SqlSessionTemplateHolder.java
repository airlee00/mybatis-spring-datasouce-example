package com.mybatis.sample.config;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author kclee01
 * @version 1.0
 * @since 2017-07-06
 */
public class SqlSessionTemplateHolder implements  InitializingBean { 

	private final static Logger logger = LoggerFactory.getLogger(SqlSessionTemplateHolder.class);
	   
	/* NamedParameterJdbcTemplate map */
	 private static Map<String, SqlSessionTemplate> sqlSessionTemplates = new HashMap<String, SqlSessionTemplate>();


	public static SqlSessionTemplate getSqlSessionTemplate(String dataSourceName) {
		return sqlSessionTemplates.get(dataSourceName);
	}
	
	public static Map<String, SqlSessionTemplate> getSqlSessionTemplates() {
		return sqlSessionTemplates;
	}

	public void setSqlSessionTemplate(Map<String, SqlSessionTemplate> map) {
		Map<String, SqlSessionTemplate> namedParameters = new HashMap<String,SqlSessionTemplate>();
		
		for (Map.Entry<String, SqlSessionTemplate> entry : map.entrySet()) {
			String[] dsNames = entry.getKey().split(",");
			for(String dsName : dsNames) {
				namedParameters.put(dsName, entry.getValue());
		    }
		}
		SqlSessionTemplateHolder.sqlSessionTemplates = namedParameters;
		
		map.clear();
	}


	@Override
    public void afterPropertiesSet() throws Exception {
		logger.info("[FWK] sqlSessionTemplates=={}", sqlSessionTemplates);
    }

	
}
