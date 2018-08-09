package com.mybatis.sample.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mybatis.sample.dao.CodeDAO;
import com.mybatis.sample.service.CodeService;
import com.mybatis.sample.vo.CodeVO;

@Service("codeService")
public class CodeServiceImpl implements CodeService {

	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("codeDAO")
	private CodeDAO codeDAO;
	
	@Autowired
	@Qualifier("codeDAO2")
	private CodeDAO codeDAO2;

	@Autowired
	@Qualifier("codeDAO3")
	private CodeDAO codeDAO3;
	
	@Override
	public List<CodeVO> getCodeList(CodeVO vo) {
		List<CodeVO> result1 =  codeDAO.selectCodeList(vo);
		List<CodeVO> result3 =  codeDAO3.selectCodeList(vo);
		log.info(" result3=" + result3);
		List<CodeVO> result2 =  codeDAO2.selectCodeList(vo);
		return result2;
	}

	@Override
	public int saveCode(CodeVO vo) {
		int result = codeDAO.insertCode(vo);
		log.info("[ save result ] " + result);
		return result;
	}

	@Override
	public int removeCode(CodeVO vo) {
		int result = codeDAO.deleteCode(vo);
		log.info("[ remove result ] " + result);
		return result;
	}
}
