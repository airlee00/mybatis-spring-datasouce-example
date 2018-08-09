package com.mybatis.sample.mapper;

import java.util.List;
import java.util.Map;

import com.mybatis.sample.vo.CodeVO;

public interface CodeMapper {

	public List<CodeVO> selectCodeList(CodeVO vo);
	
	public int insertCode(CodeVO vo) ;

	public int deleteCode(CodeVO vo) ;
}
