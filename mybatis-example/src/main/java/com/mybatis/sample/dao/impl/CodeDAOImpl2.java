package com.mybatis.sample.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mybatis.sample.config.AbstractSqlSessionDaoSupport;
import com.mybatis.sample.dao.CodeDAO;
import com.mybatis.sample.mapper.CodeMapper;
import com.mybatis.sample.vo.CodeVO;

@Repository("codeDAO2")
public class CodeDAOImpl2  extends AbstractSqlSessionDaoSupport implements CodeDAO {

	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired( required = false)
	private CodeMapper mapper;
	
	@Override
	public List<CodeVO> selectCodeList(CodeVO vo) {
		return this.getMapper(CodeMapper.class).selectCodeList(vo);
		//return mapper.selectCodeList(vo);
	}

	@Override
	public int insertCode(CodeVO vo) {
		return mapper.insertCode(vo);
	}

	@Override
	public int deleteCode(CodeVO vo) {
		return mapper.deleteCode(vo);
	}

	@Override
    protected String getDataSourceName() {
        return "SLT-PG-DataSource01";
    }
	
}
