package com.mybatis.sample.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mybatis.sample.config.AbstractSqlSessionDaoSupport;
import com.mybatis.sample.dao.CodeDAO;
import com.mybatis.sample.mapper.CodeMapper;
import com.mybatis.sample.vo.CodeVO;

@Repository("codeDAO4")
public class CodeDAOImpl4 extends AbstractSqlSessionDaoSupport implements CodeDAO {

	Logger log = Logger.getLogger(this.getClass());
	
	@Override
	protected String getDataSourceName() {
		return "SLT-PG-DataSource01";
	}
	
	@Override
	public List<CodeVO> selectCodeList(CodeVO vo) {
		return this.getMapper(CodeMapper.class).selectCodeList(vo);
	}

	@Override
	public int insertCode(CodeVO vo) {
		return this.getMapper(CodeMapper.class).insertCode(vo);
	}

	@Override
	public int deleteCode(CodeVO vo) {
		return this.getMapper(CodeMapper.class).deleteCode(vo);
	}

	
}
