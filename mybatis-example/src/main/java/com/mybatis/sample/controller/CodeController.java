package com.mybatis.sample.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mybatis.sample.service.CodeService;
import com.mybatis.sample.vo.CodeVO;

@Controller
public class CodeController {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@RequestMapping(value="/code/list")
	public ModelAndView getCodeList(@ModelAttribute CodeVO vo) throws Exception {
		
		
		ModelAndView mv = new ModelAndView();
		
		List<CodeVO> list = codeService.getCodeList(vo);
		
		mv.addObject("list", list);
		log.info("list=" + list );
		
		mv.setViewName("result");
		
		return mv;
	}

	@RequestMapping(value="/code/save")
	public ModelAndView saveCode(@ModelAttribute CodeVO vo) throws Exception {
		
		log.info(vo.toString().replaceAll("null", ""));
		
		ModelAndView mv = new ModelAndView();
		
		// �뜲�씠�꽣 ���옣
		int result = codeService.saveCode(vo);
		
		// ���옣寃곌낵 SET
		mv.addObject("result", result);
		
		// View �씠由� �꽕�젙
		mv.setViewName("result");
		
		return mv;
	}

	@RequestMapping(value="/code/remove")
	public ModelAndView removeCode(@ModelAttribute CodeVO vo) throws Exception {
		
		log.info(vo.toString().replaceAll("null", ""));
		
		ModelAndView mv = new ModelAndView();
		
		// �뜲�씠�꽣 �궘�젣
		int result = codeService.removeCode(vo);
		
		// �궘�젣寃곌낵 SET
		mv.addObject("result", result);
		
		// View �씠由� �꽕�젙
		mv.setViewName("result");
		
		return mv;
	}
}
