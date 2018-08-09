package com.mybatis.sample.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mybatis.sample.config.AbstractSqlSessionDaoSupport;
import com.mybatis.sample.constance.DaoConst;
import com.mybatis.sample.dao.CodeDAO;
import com.mybatis.sample.vo.CodeVO;

@Repository("codeDAO3")
public class CodeDAOImpl3 extends AbstractSqlSessionDaoSupport implements CodeDAO {

	Logger log = Logger.getLogger(this.getClass());
	
	
	@Override
	public List<CodeVO> selectCodeList(CodeVO vo) {
		return selectList(DaoConst.NAMESPACE_CODE + ".selectCodeList", vo);
	}

	@Override
	public int insertCode(CodeVO vo) {
		return insert(DaoConst.NAMESPACE_CODE + ".insertCode", vo);
	}

	@Override
	public int deleteCode(CodeVO vo) {
		return delete(DaoConst.NAMESPACE_CODE + ".deleteCode", vo);
	}

	@Override
	protected String getDataSourceName() {
		return "SLT-PG-DataSource01";
	}
	
}
