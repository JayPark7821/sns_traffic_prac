package com.example.fastcampusmysql.domain.follow.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Follow {

	private Long id;
	private Long fromMemberId;
	private Long toMemberId;
	private LocalDateTime createdAt;

	@Builder
	public Follow(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt) {
		this.id = id;
		this.fromMemberId = fromMemberId;
		this.toMemberId = toMemberId;
		this.createdAt = createdAt;
	}
}
