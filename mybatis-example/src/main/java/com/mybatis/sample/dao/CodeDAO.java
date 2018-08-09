package com.mybatis.sample.dao;

import java.util.List;
import java.util.Map;

import com.mybatis.sample.vo.CodeVO;

public interface CodeDAO {
	
	public List<CodeVO> selectCodeList(CodeVO vo);

	public int insertCode(CodeVO vo);

	public int deleteCode(CodeVO vo);
	
}
