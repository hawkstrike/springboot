package kr.co.springboot.search.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Document(indexName = "springboot")
public class SearchBoardVO {
	@Id
	private Long id; // 아이디
	
	@Field(name = "create_date")
	private LocalDateTime createDate; // 생성 날짜
	
	private String title; // 제목
	
	private String content; // 내용
	
	@Field(name = "user_name")
	private String userName; // 사용자 명
	
	@Field(name = "read_count")
	private int readCount; // 조회수
	
	@Field(name = "delete_yn")
	private String deleteYn; // 삭제 여부
	
	@Field(name = "update_date")
	private LocalDateTime updateDate; // 수정 날짜
	
	@Field(name = "delete_date")
	private LocalDateTime deleteDate; // 삭제 날짜
	
	public String getCreateDate() {
		return (this.createDate != null) ? this.createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : null;
	}
}