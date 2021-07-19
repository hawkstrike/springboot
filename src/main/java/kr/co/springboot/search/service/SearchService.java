package kr.co.springboot.search.service;

import kr.co.springboot.search.repository.SearchRepository;
import kr.co.springboot.search.vo.SearchBoardVO;
import kr.co.springboot.search.vo.SearchInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepository;
	
	@Autowired
	private SearchInfoVO searchInfoVO;
	
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
		String[] searchFieldArr = (String[]) paramMap.getOrDefault("searchField[]", new String[]{"title", "content"});
		QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query);
		Operator operator = ("and".equals(searchOperator)) ? Operator.AND : Operator.OR;
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		
		queryBuilder.defaultOperator(operator);
		
		for (String searchField : searchFieldArr) {
			String[] fieldInfoArr = searchField.split("/");
			String field = fieldInfoArr[0];
			float score = (fieldInfoArr.length >= 2) ? Float.parseFloat(fieldInfoArr[1]) : 100f;
			
			log.warn("[search] [field] - " + field);
			
			queryBuilder.field(field, score);
			highlightBuilder.field(field);
		}
		
		highlightBuilder.preTags("<span style=\"color:red\">");
		highlightBuilder.postTags("</span>");
		highlightBuilder.order(HighlightBuilder.Order.SCORE);
		highlightBuilder.fragmentSize(200);
		highlightBuilder.numOfFragments(1);
		highlightBuilder.requireFieldMatch(true);
		
		NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
				.withPageable(PageRequest.of(Integer.parseInt(currentPage), Integer.parseInt(pageSize)))
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
				.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.fromString(sortOrder)))
				.withHighlightBuilder(highlightBuilder);
		
		if (!"".equals(query)) {
			searchQuery.withQuery(queryBuilder);
		}
		
		return elasticsearchOperations.search(searchQuery.build(), SearchBoardVO.class);
	}
}