package kr.co.springboot.search.vo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
public class SearchInfoVO {

	@Autowired
	private Environment environment;
	
	private String prefix;
	private String indexPropertiesKey;
	private String korNamePropertiesKey;
	private String korNameMapKey;
	private String searchFieldMapKey;
	private String highlightFieldMapKey;
	private List<String> searchFieldPropertiesKeyList;
	private List<String> highlightFieldPropertiesKeyList;
	private List<String> indexList;
	private List<String> korNameList;
	private HashMap<String, HashMap<String, Object>> searchInfoMap;
	
	public SearchInfoVO() {
		prefix = "search";
		indexPropertiesKey = prefix + ".index";
		korNamePropertiesKey = prefix + ".index.kor.name";
		korNameMapKey = "korName";
		searchFieldMapKey = "searchField";
		highlightFieldMapKey = "highlightField";
		searchFieldPropertiesKeyList = Arrays.asList(new String[]{prefix, "", "search", "field"});
		highlightFieldPropertiesKeyList = Arrays.asList(new String[]{prefix, "", "highlight", "field"});
		indexList = new ArrayList<>();
		korNameList = new ArrayList<>();
		searchInfoMap = new HashMap<>();
	}
	
	@PostConstruct
	private void init() {
		indexList = List.of(environment.getProperty(indexPropertiesKey, "").split(","));
		korNameList = List.of(environment.getProperty(korNamePropertiesKey, "").split(","));
		
		log.debug("[init] [indexList] - " + indexList.toString());
		log.debug("[init] [korNameList] - " + korNameList.toString());
		
		for (String index : indexList) {
			if (!"".equals(index)) {
				log.debug("[init] - Start info setting about " + index);
				
				searchInfoMap.putIfAbsent(index, new HashMap<>());
				
				searchFieldPropertiesKeyList.set(1, index);
				highlightFieldPropertiesKeyList.set(1, index);
				
				String[] searchFieldInfoArr = environment.getProperty(String.join(".", searchFieldPropertiesKeyList), "").split(",");
				String[] highlightFieldInfoArr = environment.getProperty(String.join(".", highlightFieldPropertiesKeyList), "").split(",");
				
				searchInfoMap.get(index).put(korNameMapKey, korNameList.get(indexList.indexOf(index)));
				searchInfoMap.get(index).put(searchFieldMapKey, List.of(searchFieldInfoArr));
				searchInfoMap.get(index).put(highlightFieldMapKey, List.of(highlightFieldInfoArr));
				
				log.debug("[init] [" + index + "] [searchField] - " + searchInfoMap.get(index).get(searchFieldMapKey).toString());
				log.debug("[init] [" + index + "] [highlightField] - " + searchInfoMap.get(index).get(highlightFieldMapKey).toString());
			}
		}
	}
	
	public List<String> getIndex() {
		return indexList;
	}
	
	public String getIndexByIndex(int index) {
		return indexList.get(index);
	}
	
	public List<String> getKorName() {
		return korNameList;
	}
	
	public String getKorNameByIndex(int index) {
		return korNameList.get(index);
	}
	public String getKorNameByIndex(String index) {
		int order = indexList.indexOf(index);
		
		return (order != -1) ? korNameList.get(order) : "";
	}
	
	public List<String> getSearchFieldByIndex(int index) {
		return (List<String>) searchInfoMap.get(indexList.get(index)).get(searchFieldMapKey);
	}
	
	public List<String> getSearchFieldByIndex(String index) {
		return (List<String>) searchInfoMap.get(index).get(searchFieldMapKey);
	}
	
	public List<String> getHighlightFieldByIndex(int index) {
		return (List<String>) searchInfoMap.get(indexList.get(index)).get(highlightFieldMapKey);
	}
	
	public List<String> getHighlightFieldByIndex(String index) {
		return (List<String>) searchInfoMap.get(index).get(highlightFieldMapKey);
	}
}