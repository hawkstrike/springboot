package kr.co.springboot.board.service;

import kr.co.springboot.board.repository.BoardRepository;
import kr.co.springboot.board.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	/**
	 * select all about Table Board
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<BoardVO> findAll(HashMap<String, Object> paramMap) throws Exception {
		return null;
	}
	
	/**
	 * select one about Table Board
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public BoardVO findById(HashMap<String, Object> paramMap) throws Exception {
		return null;
	}
	
	/**
	 * insert / update one about Table Board
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public BoardVO save(HashMap<String, Object> paramMap) throws Exception {
		return null;
	}
	
	/**
	 * delete All By Id about Table Board
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAllById(HashMap<String, Object> paramMap) throws Exception {
		return false;
	}
	
	/**
	 * delete one about Table Board
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteById(HashMap<String, Object> paramMap) throws Exception {
		return false;
	}
}