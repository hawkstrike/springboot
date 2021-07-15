package kr.co.springboot.search.controller;

import kr.co.springboot.search.service.SearchService;
import kr.co.springboot.search.vo.SearchBoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping(value = "")
	public ModelAndView main(@RequestParam HashMap<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("search/search");
		//Page<SearchBoardVO> resultList = searchService.findAll(paramMap);
		SearchHits<SearchBoardVO> resultList = searchService.search(paramMap);
		
		mav.addObject("resultList", resultList);
		
		return mav;
	}
}