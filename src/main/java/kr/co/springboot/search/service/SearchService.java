package kr.co.springboot.search.service;

import kr.co.springboot.search.repository.SearchRepository;
import kr.co.springboot.search.vo.SearchBoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepository;
	
	@Autowired
	private SearchSettingService searchSettingService;
	
	@Qualifier("elasticsearchOperations")
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	
	public Page<SearchBoardVO> findAll(HashMap<String, Object> paramMap) throws Exception {
		log.info("[findAll] [paramMap] - " + paramMap.toString());
		
		String currentPage = paramMap.getOrDefault("currentPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "3").toString().strip();
		String sortOrder = paramMap.getOrDefault("sortOrder", "create_date").toString().strip();
		Page<SearchBoardVO> resultList = searchRepository.findAll(PageRequest.of(Integer.parseInt(currentPage), Integer.parseInt(pageSize), Sort.by(Sort.Order.desc(sortOrder))));
		
		log.info("[findAll] [currentPage] - " + currentPage);
		log.info("[findAll] [resultList] [totalCount] - " + resultList.getTotalElements());
		log.info("[findAll] [resultList] [totalPage] - " + resultList.getTotalPages());
		log.info("[findAll] [resultList] [content] - " + resultList.getContent().toString());
		
		return resultList;
	}
	
	public List<SearchHits<?>> search(HashMap<String, Object> paramMap) throws Exception {
		log.info("[search] [paramMap] - " + paramMap.toString());
		
		String index = paramMap.getOrDefault("index", "ALL").toString().strip();
		String query = paramMap.getOrDefault("query", "").toString().strip();
		String searchOperator = paramMap.getOrDefault("searchOperator", "and").toString().strip();
		String currentPage = paramMap.getOrDefault("currentPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "3").toString().strip();
		String sortField = paramMap.getOrDefault("sortField", "").toString().strip();
		String sortOrder = paramMap.getOrDefault("sortOrder", "desc").toString().strip();
		
		List<String> indexList = ("ALL".equals(index.toUpperCase())) ? searchSettingService.getIndices() : List.of(index);
		List<Query> queryList = new ArrayList<>();
		List<Class<?>> classList = new ArrayList<>();
		
		for (String indexName : indexList) {
			Query searchQuery = new NativeSearchQueryBuilder()
					.withPageable(searchSettingService.setPageRequest(currentPage, pageSize))
					.withSorts(searchSettingService.setSortBuilder(sortField, sortOrder))
					.withQuery(searchSettingService.setQueryBuilder(indexName, query, searchOperator))
					.withHighlightBuilder(searchSettingService.setHighlightBuilder(indexName, 1))
					.build();
			
			queryList.add(searchQuery);
			classList.add(searchSettingService.getClassByIndex(indexName));
		}
		
		return elasticsearchOperations.multiSearch(queryList, classList);
	}
	
	public List<String> getIndices() {
		return searchSettingService.getIndices();
	}
	
	public Map<String, String> getKorNamesInMap() {
		return searchSettingService.getKorNamesInMap();
	}
}