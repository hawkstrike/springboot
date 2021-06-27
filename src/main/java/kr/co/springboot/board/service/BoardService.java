package kr.co.springboot.board.service;

import kr.co.springboot.board.repository.BoardRepository;
import kr.co.springboot.board.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
		
		Sort dateSort = Sort.by(Sort.Order.desc("createDate"));
		String pageNumber = paramMap.getOrDefault("startPage", "0").toString().strip();
		String pageSize = paramMap.getOrDefault("pageSize", "10").toString().strip();
		String sortOrder = paramMap.getOrDefault("sort", "id").toString().strip();
		Page<BoardVO> resultList = boardRepository.findAll(PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), Sort.by(sortOrder).and(dateSort)));
		
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
			
			BoardVO.BoardVOBuilder builder = BoardVO.builder();
			
			builder.id(boardVO.getId());
			builder.readCount(boardVO.getReadCount() + 1);
			
			return boardRepository.save(builder.build());
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
		String idStr = paramMap.getOrDefault("id", "-1").toString().strip();
		String title = paramMap.getOrDefault("title", "").toString().strip();
		String content = paramMap.getOrDefault("content", "").toString().strip();
		String userName = paramMap.getOrDefault("userName", "").toString().strip();
		//long id = Long.parseLong(idStr);
		//Optional<BoardVO> optional = boardRepository.findById(id);
		//BoardVO boardVO = (optional.isPresent()) ? optional.get() : new BoardVO();
		BoardVO.BoardVOBuilder builder = BoardVO.builder();
		
		if ("".equals(title)) {
			builder.title(title);
		}
		
		if ("".equals(content)) {
			builder.content(content);
		}
		
		if ("".equals(userName)) {
			builder.userName(userName);
		}
		
		//if (optional.isPresent()) { // update
		if(!"-1".equals(idStr)) {
			builder.id(Long.parseLong(idStr));
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
		List<BoardVO> boardList = new ArrayList<>();
		BoardVO.BoardVOBuilder builder = BoardVO.builder();
		
		builder.deleteYn("Y");
		builder.deleteDate(LocalDateTime.now());
		
		for (long id : idList) {
			builder.id(id);
			
			boardList.add(builder.build());
		}
		
		return boardRepository.saveAll(boardList);
		
		/*if (idList.size() >= 2) {
			boardRepository.deleteAllById(idList);
		} else if (idList.size() == 1) {
			boardRepository.deleteById(idList.get(0));
		}*/
		
		//List<BoardVO> resultList = boardRepository.findAllById(idList);
	}
}