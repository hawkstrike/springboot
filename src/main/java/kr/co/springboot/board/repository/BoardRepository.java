package kr.co.springboot.board.repository;

import kr.co.springboot.board.vo.BoardVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardVO, Long> {
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Board set readCount = readCount + 1 where id = :id")
	void updateReadCount(Long id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Board set deleteYn = 'Y' where id in (:idList)")
	List<BoardVO> updateDeleteYn(List<Long> idList);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("delete from Board where id in :idList")
	void deleteAllById(List<Long> idList);
}