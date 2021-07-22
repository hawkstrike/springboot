package kr.co.springboot.search.service;

import kr.co.springboot.search.vo.SearchInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchSettingService {
	
	@Autowired
	private SearchInfoVO searchInfoVO;
	
	public Class<?> getClassByIndex(int index) {
		return searchInfoVO.getClassByMap(index);
	}
	
	public Class<?> getClassByIndex(String index) {
		return searchInfoVO.getClassByMap(index);
	}
	
	public List<String> getIndices() {
		return searchInfoVO.getIndices();
	}
	
	public String getIndex(int index) {
		return searchInfoVO.getIndexByIndex(index);
	}
	
	public Map<String, String> getKorNamesInMap() {
		return searchInfoVO.getKorNamesInMap();
	}
	
	public String getKorNameInMapByIndex(int index) {
		return searchInfoVO.getKorNameInMapByIndex(index);
	}
	
	public String getKorNameInMapByIndex(String index) {
		return searchInfoVO.getKorNameInMapByIndex(index);
	}
	
	public PageRequest setPageRequest(int currentPage, int pageSize) {
		if (currentPage < 0) {
			log.warn("[setPageRequest] - currentPage is below 0!");
			log.warn("[setPageRequest] - currentPage is automatically set 0!");
			
			currentPage = 0;
		}
		
		if (pageSize < 0) {
			log.warn("[setPageRequest] - pageSize is below 0!");
			log.warn("[setPageRequest] - pageSize is automatically set 0!");
			
			pageSize = 0;
		}
		
		return PageRequest.of(currentPage, pageSize);
	}
	
	public PageRequest setPageRequest(String currentPage, String pageSize) {
		int page = 0;
		int size = 0;
		
		try {
			page = Integer.parseInt(currentPage.strip());
		} catch (NumberFormatException e) {
			log.warn("[setPageRequest] - currentPage can\'t be parse to number (" + currentPage + ")");
			log.warn("[setPageRequest] - currentPage is automatically set 0!");
			
			page = 0;
		}
		
		try {
			size = Integer.parseInt(pageSize.strip());
		} catch (NumberFormatException e) {
			log.warn("[setPageRequest] - pageSize can\'t be parse to number (" + pageSize + ")");
			log.warn("[setPageRequest] - pageSize is automatically set 3!");
			
			size = 3;
		}
		
		return PageRequest.of(page, size);
	}
	
	public QueryStringQueryBuilder setQueryBuilder(String index, String query, String searchOperator) {
		if (index == null || "".equals(index.strip()) || searchInfoVO.getIndexByIndex(index.strip()) < 0) {
			log.error("[setQueryBuilder] - index is invalid! (" + index + ")");
			
			return null;
		}
		
		return setQueryBuilderDetail(searchInfoVO.getIndexByIndex(index.strip()), query, searchOperator);
	}
	
	public QueryStringQueryBuilder setQueryBuilder(int index, String query, String searchOperator) {
		if (index < 0 || searchInfoVO.getIndexByIndex(index) == null || "".equals(searchInfoVO.getIndexByIndex(index))) {
			log.error("[setQueryBuilder] - index is invalid! (" + index + ")");
			
			return null;
		}
		
		return setQueryBuilderDetail(index, query, searchOperator);
	}
	
	private QueryStringQueryBuilder setQueryBuilderDetail(int index, String query, String searchOperator) {
		if (query == null || "".equals(query.strip())) {
			log.error("[setQueryBuilderDetail] - query is empty!");
			
			return null;
		}
		
		if (searchOperator == null || "".equals(searchOperator.strip())) {
			log.warn("[setQueryBuilderDetail] - searchOperator is empty!");
			log.warn("[setQueryBuilderDetail] - searchOperator is automatically set and!");
			
			searchOperator = "and";
		}
		
		QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("*" + query + "*");
		Operator operator = ("and".equals(searchOperator.toLowerCase())) ? Operator.AND : Operator.OR;
		List<String> searchFieldList = searchInfoVO.getSearchFieldByIndex(index);
		
		for (String searchField : searchFieldList) {
			String[] fieldInfoArr = searchField.split("/");
			float score = 0;
			
			try {
				score = (fieldInfoArr.length >= 2) ? Float.parseFloat(fieldInfoArr[1].strip()) : 0f;
			} catch (NumberFormatException e) {
				log.warn("[setQueryBuilderDetail] - field score can\'t be parse to float (" + fieldInfoArr[0] + " | " + fieldInfoArr[1] + ")");
				log.warn("[setQueryBuilderDetail] - field score is automatically set 0!");
				
				score = 0;
			}
			
			queryBuilder.field(fieldInfoArr[0].strip(), score);
		}
		
		queryBuilder.defaultOperator(operator);
		queryBuilder.analyzer("korean_mixed");
		queryBuilder.analyzeWildcard(true);
		queryBuilder.lenient(true);
		
		log.debug("[setQueryBuilderDetail] [queryBuilder] - " + queryBuilder.fields().toString());
		
		return queryBuilder;
	}
	
	public HighlightBuilder setHighlightBuilder(String index, int numOfFragments) {
		if (index == null || "".equals(index.strip()) || searchInfoVO.getIndexByIndex(index.strip()) < 0) {
			log.error("[setHighlightBuilder] - index is invalid! (" + index + ")");
			
			return null;
		}
		
		return setHighlightBuilderDetail(searchInfoVO.getIndexByIndex(index.strip()), numOfFragments);
	}
	
	public HighlightBuilder setHighlightBuilder(int index, int numOfFragments) {
		if (index < 0 || searchInfoVO.getIndexByIndex(index) == null || "".equals(searchInfoVO.getIndexByIndex(index))) {
			log.error("[setHighlightBuilder] - index is invalid! (" + index + ")");
			
			return null;
		}
		
		return setHighlightBuilderDetail(index, numOfFragments);
	}
	
	public HighlightBuilder setHighlightBuilderDetail(int index, int numOfFragments) {
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		List<String> highlightFieldList = searchInfoVO.getHighlightFieldByIndex(index);
		
		for (String highlightField : highlightFieldList) {
			highlightBuilder.field(highlightField);
		}
		
		highlightBuilder.numOfFragments(numOfFragments);
		highlightBuilder.order(HighlightBuilder.Order.SCORE);
		highlightBuilder.requireFieldMatch(true);
		
		log.debug("[setHighlightBuilderDetail] [highlightBuilder] - " + highlightBuilder.fields().toString());
		
		return highlightBuilder;
	}
	
	public List<SortBuilder<?>> setSortBuilder(String sortField, String sortOrder) {
		List<SortBuilder<?>> sortList = new LinkedList<>();
		SortOrder order = null;
		
		sortList.add(SortBuilders.scoreSort().order(SortOrder.DESC));

		if (sortField == null || "".equals(sortField.strip())) {
			log.error("[setSortBuilder] - sortField is empty!");
			
			return sortList;
		}
		
		if (sortOrder == null || "".equals(sortOrder.strip())) {
			log.warn("[setSortBuilder] - sortOrder is empty!");
			log.warn("[setSortBuilder] - sortOrder is automatically set desc!");
			
			order = SortOrder.DESC;
		} else {
			order = ("desc".equals(sortOrder.strip().toLowerCase())) ? SortOrder.DESC : SortOrder.ASC;
		}
		
		sortList.add(0, SortBuilders.fieldSort(sortField).order(order));
		
		return sortList;
	}
}