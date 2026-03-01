package com.becoder.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotesResponse {
	private List<NotesDto> notes;

	private Integer pageNo;
	private Integer pageSize;
	private Integer totalPage;

	private Boolean isFirst;
	private Boolean isLast;
	private Long totalElements;

}
