package kr.co.springboot.search.service;

import kr.co.springboot.search.repository.SearchRepository;
import kr.co.springboot.search.vo.SearchBoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepository;
	
	public Page<SearchBoardVO> findAll(HashMap<String, Object> paramMap) throws Exception {
		log.info("[findAll] [paramMap] - " + paramMap.toString());
		
		String currentPage = paramMap.getOrDefault("currentPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "10").toString().strip();
		String sortOrder = paramMap.getOrDefault("sortOrder", "create_date").toString().strip();
		Page<SearchBoardVO> resultList = searchRepository.findAll(PageRequest.of(Integer.parseInt(currentPage), Integer.parseInt(pageSize), Sort.by(Sort.Order.desc(sortOrder))));
		
		log.info("[findAll] [currentPage] - " + currentPage);
		log.info("[findAll] [resultList] [totalCount] - " + resultList.getTotalElements());
		log.info("[findAll] [resultList] [totalPage] - " + resultList.getTotalPages());
		log.info("[findAll] [resultList] [resultCount] - " + resultList.getContent().toString());
		
		return resultList;
	}
}