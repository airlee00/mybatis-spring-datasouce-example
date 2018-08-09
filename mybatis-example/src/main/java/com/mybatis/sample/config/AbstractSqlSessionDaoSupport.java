package com.mybatis.sample.config;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;


import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * Mybatis 환경하 Entity Component에서 공통적으로 상속받아야 하는 abstract 클래스
 * 
 * @author kclee01
 * @version 1.0
 * @since 2018-08-08
 */
public abstract class AbstractSqlSessionDaoSupport extends SqlSessionDaoSupport  {

    protected abstract String getDataSourceName() ;
    /**
     * Return a SqlSessionTemplate wrapping the configured SqlSession
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
    
    /**
     * Mapper를 가져옴 
     * @param mapper 인터페이스 타입
     * @return mapper 인스턴스 
     */
    public <T> T getMapper(Class<T> type) {
        return getSqlSessionTemplate().getMapper(type);
    } 
    /**
     * SELECT 다건 처리 
     * @param selectId mapper의 id로 namespace가 포함된 아이디 이다. ex) com.mybatis.sample.mapper.CodeMapper.selectCodeList
     * @param parameter 조회조건 VO
     * @return
     */
    public <T> List<T> selectList(String selectId, Object parameter) {
        return getSqlSessionTemplate().selectList(selectId, parameter);
    }

    /**
     * SELECT 단건 처리
     * @param selectId mapper의 id로 namespace가 포함된 아이디 이다. ex) com.mybatis.sample.mapper.CodeMapper.selectCodeList
     * @param parameter 조회조건 VO
     * @return
     */
    public <T> T selectOne(String selectId, Object parameter) {
        return getSqlSessionTemplate().selectOne(selectId, parameter);
    }
    
    /**
     * 삭제 처리
     * @param queryId mapper의 id로 namespace가 포함된 아이디 이다.
     * @param parameter VO객체
     * @return
     */
    public int delete(String queryId, Object parameter) {
        return getSqlSessionTemplate().delete(queryId, parameter);
    }
    
    /**
     * 입력 처리
     * @param queryId mapper의 id로 namespace가 포함된 아이디 이다.
     * @param parameter  VO객체
     * @return
     */
    public int insert(String queryId, Object parameter) {
        return getSqlSessionTemplate().insert(queryId, parameter);
    }
    
    /**
     * 수정 처리 
     * @param queryId mapper의 id로 namespace가 포함된 아이디 이다.
     * @param parameter  VO객체
     * @return
     */ 
    public int update(String queryId, Object parameter) {
        return getSqlSessionTemplate().update(queryId, parameter);
    }
    
    /**
     * 다건 업데이트 
     * @param queryId
     * @param list
     * @return
     */
    public <T> int batchUpdate(String queryId, List<T> list) {
        SqlSession sqlSession =  getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH);
        int count =0;
        for(T t : list) {
            count = count + sqlSession.update(queryId, t);
        }
        return count;
    }
    
    /**
     * 다건 업데이트 
     * @param queryId
     * @param list
     * @return
     */
    public <T> int batchInsert(String queryId, List<T> list) {
        SqlSession sqlSession =  getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH);
        int count =0;
        for(T t : list) {
            count = count + sqlSession.insert(queryId, t);
        }
        return count;
    }
}
