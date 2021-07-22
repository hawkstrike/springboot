package kr.co.springboot.search.vo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
	private HashMap<String, String> korNameMap;
	private HashMap<String, HashMap<String, Object>> searchInfoMap;
	private HashMap<String, Class<?>> classMap;
	
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
		korNameMap = new HashMap<>();
		searchInfoMap = new HashMap<>();
		classMap = new HashMap<>();
	}
	
	@PostConstruct
	private void init() {
		indexList = Arrays.stream(environment.getProperty(indexPropertiesKey, "")
				.split(","))
				.distinct()
				.map(index -> index.strip())
				.collect(Collectors.toUnmodifiableList());
		korNameList = Arrays.stream(environment.getProperty(korNamePropertiesKey, "")
				.split(","))
				.distinct()
				.map(korName -> new String(korName.strip().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
				.collect(Collectors.toUnmodifiableList());
		
		log.debug("[init] [indexList] - " + indexList.toString());
		log.debug("[init] [korNameList] - " + korNameList.toString());
		
		for (String index : indexList) {
			if (!"".equals(index)) {
				log.debug("[init] - Start info setting about " + index);
				
				searchInfoMap.putIfAbsent(index, new HashMap<>());
				
				korNameMap.putIfAbsent(index, korNameList.get(indexList.indexOf(index)));
				searchFieldPropertiesKeyList.set(1, index);
				highlightFieldPropertiesKeyList.set(1, index);
				
				searchInfoMap.get(index).put(korNameMapKey, korNameList.get(indexList.indexOf(index)));
				searchInfoMap.get(index).put(searchFieldMapKey, Arrays.stream(environment.getProperty(String.join(".", searchFieldPropertiesKeyList), "")
						.split(","))
						.distinct()
						.map(searchField -> searchField.strip())
						.collect(Collectors.toUnmodifiableList()));
				searchInfoMap.get(index).put(highlightFieldMapKey, Arrays.stream(environment.getProperty(String.join(".", highlightFieldPropertiesKeyList), "")
						.split(","))
						.distinct()
						.map(highlightField -> highlightField.strip())
						.collect(Collectors.toUnmodifiableList()));
				
				log.debug("[init] [" + index + "] [searchField] - " + searchInfoMap.get(index).get(searchFieldMapKey).toString());
				log.debug("[init] [" + index + "] [highlightField] - " + searchInfoMap.get(index).get(highlightFieldMapKey).toString());
			}
		}
		
		setClassMap();
	}
	
	private void setClassMap() {
		classMap.putIfAbsent("board", SearchBoardVO.class);
	}
	
	public Class<?> getClassByMap(int index) {
		return classMap.get(getIndexByIndex(index));
	}
	
	public Class<?> getClassByMap(String index) {
		return classMap.get(index);
	}
	
	public List<String> getIndices() {
		return indexList;
	}
	
	public String getIndexByIndex(int index) {
		return indexList.get(index);
	}
	
	public int getIndexByIndex(String index) {
		return indexList.indexOf(index);
	}
	
	public List<String> getKorNames() {
		return korNameList;
	}
	
	public String getKorNameByIndex(int index) {
		return korNameList.get(index);
	}
	public String getKorNameByIndex(String index) {
		int order = indexList.indexOf(index);
		
		return (order != -1) ? korNameList.get(order) : "";
	}
	
	public Map<String, String> getKorNamesInMap() {
		return korNameMap;
	}
	
	public String getKorNameInMapByIndex(int index) {
		return korNameMap.get(indexList.get(index));
	}
	
	public String getKorNameInMapByIndex(String index) {
		return korNameMap.get(index);
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