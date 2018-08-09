package com.mybatis.sample.config;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * Entity Component에서 공통적으로 상속받아야 하는 abstract 클래스
 * 
 * @author kclee01
 * @version 1.0
 * @since 2017-07-06
 */
public abstract class AbstractSqlSessionDaoSupport extends SqlSessionDaoSupport  {

    protected abstract String getDataSourceName() ;
    /**
     * Return a NamedParameterJdbcTemplate wrapping the configured JdbcTemplate.
     */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return SqlSessionTemplateHolder.getSqlSessionTemplate(getDataSourceName());
	}
    
	protected void initDao() throws Exception {
		setSqlSessionTemplate(getSqlSessionTemplate());
	}

    @Override
    protected void checkDaoConfig() {
    	//do nothing
    }
    
    public <T> T getMapper(Class<T> type ) {
        return getSqlSessionTemplate().getMapper(type);
    }
    
    public <T> T select(String statement, Object parameter) {
        return getSqlSessionTemplate().selectOne(statement, parameter);
    }
    
    public <T> List<T> selectList(String statement, Object parameter) {
        return getSqlSessionTemplate().selectList(statement, parameter);
    }

    public int delete(String statement, Object parameter) {
    	return getSqlSessionTemplate().delete(statement, parameter);
    }
    
    public int insert(String statement, Object parameter) {
    	return getSqlSessionTemplate().insert(statement, parameter);
    }
}
