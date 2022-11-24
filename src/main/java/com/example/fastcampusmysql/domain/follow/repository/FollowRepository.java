package com.example.fastcampusmysql.domain.follow.repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.follow.entity.Follow;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

	private static final String TABLE = "follow";
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final RowMapper<Follow> rowMapper = (ResultSet resultSet, int rowNum) -> Follow.builder()
		.id(resultSet.getLong("id"))
		.fromMemberId(resultSet.getLong("fromMemberId"))
		.toMemberId(resultSet.getLong("toMemberId"))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();

	public List<Follow> findAllByFromMemberId(Long fromMemberId) {
		String sql = String.format("select * from %s where fromMemberId = :fromMemberId", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);
		return namedParameterJdbcTemplate.query(sql, param, rowMapper);

	}

	public List<Follow> findAllByToMemberId(Long toMemberId) {
		String sql = String.format("select * from %s where toMemberId = :toMemberId", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("toMemberId", toMemberId);
		return namedParameterJdbcTemplate.query(sql, param, rowMapper);

	}

	public Follow save(Follow follow) {
		if (follow.getId() == null) {
			return insert(follow);
		}

		throw new UnsupportedOperationException("follow는 갱신을 지원하지 않습니다.");
	}
	private Follow insert(Follow follow) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate() )
			.withTableName(TABLE)
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

}
