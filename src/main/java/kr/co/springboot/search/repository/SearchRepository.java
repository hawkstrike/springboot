package kr.co.springboot.search.repository;

import kr.co.springboot.search.vo.SearchBoardVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchBoardVO, Long> {

}