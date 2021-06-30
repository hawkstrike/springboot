package kr.co.springboot.board.service;

import kr.co.springboot.board.repository.BoardRepository;
import kr.co.springboot.board.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	/**
	 * select all about Table Board
	 * @param paramMap - page and sort info to find boards
	 * @return selected, paginated, sorted BoardVO(s)
	 * @throws Exception
	 */
	public List<BoardVO> findAll(HashMap<String, Object> paramMap) throws Exception {
		log.info("[findAll] [paramMap] - " + paramMap.toString());
		
		String pageNumber = paramMap.getOrDefault("startPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "10").toString().strip();
		String sortOrder = paramMap.getOrDefault("sort", "createDate").toString().strip();
		Page<BoardVO> resultList = boardRepository.findAll(PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), Sort.by(Sort.Order.desc(sortOrder))));
		
		log.info("[findAll] [resultList] [totalCount] - " + resultList.getTotalElements());
		
		return resultList.getContent();
	}
	
	/**
	 * select one about Table Board
	 * @param paramMap board info to find board detail info
	 * @return selected BoardVO
	 * @throws Exception
	 */
	public BoardVO findById(HashMap<String, Object> paramMap) throws Exception {
		log.info("[findById] [paramMap] - " + paramMap.toString());
		
		String id = paramMap.getOrDefault("id", "-1").toString().strip();
		Optional<BoardVO> optional = boardRepository.findById(Long.parseLong(id));
		BoardVO boardVO = (optional.isPresent()) ? optional.get() : new BoardVO();
		
		if (optional.isPresent()) {
			log.info("[findById] [boardVO] - " + boardVO.toString());
			
			boardRepository.updateReadCount(boardVO.getId());
		} else {
			if ("-1".equals(id)) {
				log.error("[findById] - You need to check board id. Board id was set -1 by force.");
			} else {
				log.error("[findById] - Can't find board! You need to check board id (" + id + ")");
			}
		}
		
		return boardVO;
	}
	
	/**
	 * insert / update one about Table Board
	 * @param paramMap - board info for insertion / updation board
	 * @return inserted / updated board
	 * @throws Exception
	 */
	public BoardVO save(HashMap<String, Object> paramMap) throws Exception {
		log.info("[save] [paramMap] - " + paramMap.toString());
		
		String id = paramMap.getOrDefault("id", "-1").toString().strip();
		String title = paramMap.getOrDefault("title", "").toString().strip();
		String content = paramMap.getOrDefault("content", "").toString().strip();
		String userName = paramMap.getOrDefault("userName", "").toString().strip();
		BoardVO.BoardVOBuilder builder = BoardVO.builder();
		
		if (!"".equals(title)) {
			builder.title(title);
		}
		
		if (!"".equals(content)) {
			builder.content(content);
		}
		
		if (!"".equals(userName)) {
			builder.userName(userName);
		}
		
		if(!"-1".equals(id)) {
			builder.id(Long.parseLong(id));
			builder.updateDate(LocalDateTime.now());
		}
		
		return boardRepository.save(builder.build());
	}
	
	/**
	 * delete by Id(s) about Table Board
	 * @param idList - selected ids for deletion
	 * @return if deleted board list(s)
	 * @throws Exception
	 */
	public List<BoardVO> delete(List<Long> idList) throws Exception {
		return boardRepository.updateDeleteYn(idList);
	}
}