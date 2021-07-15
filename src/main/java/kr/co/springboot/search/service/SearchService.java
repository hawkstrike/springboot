package kr.co.springboot.search.service;

import kr.co.springboot.search.repository.SearchRepository;
import kr.co.springboot.search.vo.SearchBoardVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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

import java.util.HashMap;

@Slf4j
@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepository;
	
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
	
	public SearchHits<SearchBoardVO> search(HashMap<String, Object> paramMap) throws Exception {
		log.info("[search] [paramMap] - " + paramMap.toString());
		
		String query = paramMap.getOrDefault("query", "").toString().strip();
		String searchOperator = paramMap.getOrDefault("searchOperator", "and").toString().strip();
		String currentPage = paramMap.getOrDefault("currentPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "3").toString().strip();
		String sortField = paramMap.getOrDefault("sortField", "create_date").toString().strip();
		String sortOrder = paramMap.getOrDefault("sortOrder", "desc").toString().strip();
		String[] searchFieldArr = (String[]) paramMap.getOrDefault("searchField[]", new String[]{});
		StringBuffer queryString = new StringBuffer(query);
		QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(queryString.toString());
		Operator operator = ("and".equals(searchOperator)) ? Operator.AND : Operator.OR;
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		
		queryBuilder.defaultOperator(operator);
		
		for (String searchField : searchFieldArr) {
			String[] fieldInfoArr = searchField.split("/");
			String field = fieldInfoArr[0];
			float score = (fieldInfoArr.length >= 2) ? Float.parseFloat(fieldInfoArr[1]) : 0f;
			
			queryBuilder.field(field, score);
			highlightBuilder.field(field);
		}
		
		Query searchQuery = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder)
				.withPageable(PageRequest.of(Integer.parseInt(currentPage), Integer.parseInt(pageSize)))
				.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.fromString(sortOrder)))
				.withHighlightBuilder(highlightBuilder)
				.build();
		
		return elasticsearchOperations.search(searchQuery, SearchBoardVO.class);
	}
}