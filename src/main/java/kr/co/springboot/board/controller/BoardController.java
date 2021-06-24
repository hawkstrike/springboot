package kr.co.springboot.board.controller;

import kr.co.springboot.board.service.BoardService;
import kr.co.springboot.board.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping(value = "")
	public ModelAndView main(@RequestParam HashMap<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("board/board");
		List<BoardVO> resultList = boardService.findAll(paramMap);
		
		mav.addObject("resultList", resultList);
		
		return mav;
	}
	
	@PostMapping(value = "/findOne", produces = "application/json;charset=UTF-8;")
	public ModelAndView findOne(@RequestParam HashMap<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("board/boardDetail");
		BoardVO boardVO = boardService.findById(paramMap);
		
		mav.addObject("boardVO", boardVO);
		
		return mav;
	}
	
	@PostMapping(value = "/save", produces = "application/json;charset=UTF-8;")
	public BoardVO save(@RequestParam HashMap<String, Object> paramMap) throws Exception {
		return boardService.save(paramMap);
	}
	
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8;")
	public List<BoardVO> delete(@RequestParam(value = "id[]") List<Long> idList) throws Exception {
		return boardService.delete(idList);
	}
}