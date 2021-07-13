package kr.co.springboot.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IndexService {
	
	@Qualifier("elasticsearchOperations")
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	private String errorId;
	
	public IndexService() {
		errorId = "-1";
	}
	
	public String index(IndexQuery indexQuery, String indexName) throws Exception {
		if (indexQuery == null) {
			log.error("[index] - indexQuery is null!");
			
			return errorId;
		}
		
		if (indexName == null || "".equals(indexName)) {
			log.error("[index] - indexName is invalid!");
			
			return errorId;
		}
		
		log.info("[index] [indexName] - " + IndexCoordinates.of(indexName));
		
		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexName));
		
		log.info("[index] [documentId] - " + documentId);
		
		return documentId;
	}
	
	public List<String> bulkIndex(List<IndexQuery> indexQueryList, String indexName) throws Exception {
		if (indexQueryList == null || indexQueryList.isEmpty()) {
			log.error("[bulkIndex] - indexQueryList is empty!");
			
			return null;
		}
		
		if (indexName == null || "".equals(indexName)) {
			log.error("[bulkIndex] - indexName is invalid!");
		}
		
		log.info("[bulkIndex] [indexQueryList.size] - " + indexQueryList.size());
		log.info("[bulkIndex] [indexName] - " + indexName);
		
		List<String> documentIdList = elasticsearchOperations.bulkIndex(indexQueryList, IndexCoordinates.of(indexName))
				.stream().map(indexedObjectInformation -> indexedObjectInformation.getId()).collect(Collectors.toList());
		
		return documentIdList;
	}
}