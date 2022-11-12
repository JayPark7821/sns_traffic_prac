package com.example.fastcampusmysql.domain.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.util.Assert;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
	final private Long id;

	private String nickname;

	final private String email;

	final private LocalDate birthday;

	final private LocalDateTime createdAt;

	final private static Long NAME_MAX_LENGRTH = 10L;

	@Builder
	public Member(Long id, String nickname, String email, LocalDate birthday, LocalDateTime createdAt) {
		this.id = id;
		validateNickname(nickname);
		this.nickname = Objects.requireNonNull(nickname);
		this.email = Objects.requireNonNull(email);
		this.birthday = Objects.requireNonNull(birthday);
		this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
	}

	void validateNickname(String nickname) {
		Assert.isTrue(nickname.length() <= NAME_MAX_LENGRTH, "최대 길이 초과");
	}
}
