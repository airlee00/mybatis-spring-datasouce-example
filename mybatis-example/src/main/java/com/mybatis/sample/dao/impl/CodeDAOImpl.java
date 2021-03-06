package com.mybatis.sample.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mybatis.sample.constance.DaoConst;
import com.mybatis.sample.dao.CodeDAO;
import com.mybatis.sample.vo.CodeVO;

@Repository("codeDAO")
public class CodeDAOImpl implements CodeDAO {

	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	//@Qualifier("sqlSessionTemplate01")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<CodeVO> selectCodeList(CodeVO vo) {
		return sqlSessionTemplate.selectList(DaoConst.NAMESPACE_CODE + ".selectCodeList", vo);
	}

	@Override
	public int insertCode(CodeVO vo) {
		return sqlSessionTemplate.insert(DaoConst.NAMESPACE_CODE + ".insertCode", vo);
	}

	@Override
	public int deleteCode(CodeVO vo) {
		return sqlSessionTemplate.delete(DaoConst.NAMESPACE_CODE + ".deleteCode", vo);
	}
	
}
