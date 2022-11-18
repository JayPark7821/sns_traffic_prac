package com.example.fastcampusmysql.domain.member.repository;

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

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberNicknameHistoryRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private static final String TABLE = "memberNicknameHistory";
	private static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resultSet, int rowNum) -> MemberNicknameHistory.builder()
		.id(resultSet.getLong("id"))
		.memberId(resultSet.getLong("memberId"))
		.nickname(resultSet.getString("nickname"))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();

	public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
		String sql = String.format("SELECT * FROM %s WHERE MEMBERID = :memberId ", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("memberId", memberId);
		return jdbcTemplate.query(sql, param, rowMapper);
	}

 	public MemberNicknameHistory save(MemberNicknameHistory history) {

		if (history.getId() == null) {
			return insert(history);
		}
		throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
	}

	private MemberNicknameHistory insert(MemberNicknameHistory history) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
			.withTableName(TABLE)
			.usingGeneratedKeyColumns("id");

		SqlParameterSource params = new BeanPropertySqlParameterSource(history);
		long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return MemberNicknameHistory.builder()
			.id(id)
			.memberId(history.getMemberId())
			.nickname(history.getNickname())
			.createdAt(history.getCreatedAt())
			.build();

	}

}
