package kr.co.springboot.search.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Document(indexName = "springboot")
public class SearchBoardVO {
	@Id
	private Long id; // 아이디
	
	private LocalDateTime createDate; // 생성 날짜
	
	private String title; // 제목
	
	private String content; // 내용
	
	private String userName; // 사용자 명
	
	private int readCount; // 조회수
	
	private String deleteYn; // 삭제 여부
	
	private LocalDateTime updateDate; // 수정 날짜
	
	private LocalDateTime deleteDate; // 삭제 날짜
}