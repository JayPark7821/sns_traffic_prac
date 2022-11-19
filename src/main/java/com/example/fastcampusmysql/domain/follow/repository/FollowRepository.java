package com.example.fastcampusmysql.domain.follow.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

	private static final String TABEL = "follow";
	private final JdbcTemplate jdbcTemplate;

	public Follow save(Follow follow) {
		if (follow.getId() == null) {
			return insert(follow);
		}

		throw new UnsupportedOperationException("follow는 갱신을 지원하지 않습니다.");
	}
	private Follow insert(Follow follow) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName(TABEL)
			.usingGeneratedKeyColumns("id");

		SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
		long id = jdbcInsert.executeAndReturnKey(params).longValue();
		return Follow.builder()
			.id(id)
			.fromMemberId(follow.getFromMemberId())
			.toMemberId(follow.getToMemberId())
			.createdAt(follow.getCreatedAt())
			.build();
	}

	public void create(MemberDto fromMemberId, MemberDto toMemberId) {
		/**
		 * from, to 회원 정보를 받아서
		 * 저장
		 * from to validate
		 */
	}
}
