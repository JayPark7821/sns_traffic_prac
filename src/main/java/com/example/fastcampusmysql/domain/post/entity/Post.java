package com.example.fastcampusmysql.domain.post.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {
	private Long id;
	private Long memberId;
	private String contents;
	private LocalDate createdDate;
	private Long likeCount;
	private Long version;
	private LocalDateTime createdAt;

	@Builder
	public Post(Long id, Long memberId, String contents, LocalDate createdDate, Long likeCount, Long version,
		LocalDateTime createdAt) {
		this.id = id;
		this.memberId = Objects.requireNonNull(memberId);
		this.contents = Objects.requireNonNull(contents);
		this.createdDate = createdDate == null ? LocalDate.now() :createdDate  ;
		this.likeCount = likeCount == null ? 0 : likeCount;
		this.version = version == null ? 0 : version;
		this.createdAt = createdAt == null ? LocalDateTime.now() :createdAt ;
	}

	public void increamentLikeCount() {
		likeCount += 1;
	}
}
