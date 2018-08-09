package com.mybatis.sample.service;

import java.util.List;

import com.mybatis.sample.vo.CodeVO;

public interface CodeService {

	List<CodeVO> getCodeList(CodeVO vo);

	int saveCode(CodeVO vo);

	int removeCode(CodeVO vo);

}
