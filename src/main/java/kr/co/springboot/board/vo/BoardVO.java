package kr.co.springboot.board.vo;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity(name = "Board")
@Table(name = "Board", indexes = {@Index(name = "index_board_1", columnList = "deleteYn")})
@Where(clause = "delete_yn = 'N'")
public class BoardVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 아이디
	
	private LocalDateTime createDate; // 생성 날짜
	
	private String title; // 제목
	
	@Lob
	private String content; // 내용
	
	private String userName; // 사용자 명
	
	private int readCount; // 조회수
	
	@Column(columnDefinition = "char(1) default 'N'")
	private String deleteYn; // 삭제 여부
	
	private LocalDateTime updateDate; // 수정 날짜
	
	private LocalDateTime deleteDate; // 삭제 날짜
	
	public String getCreateDate() {
		return this.createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
	}
	
	@PrePersist
	private void prepareForInsert() {
		this.createDate = LocalDateTime.now();
		this.deleteYn = "N";
	}
}