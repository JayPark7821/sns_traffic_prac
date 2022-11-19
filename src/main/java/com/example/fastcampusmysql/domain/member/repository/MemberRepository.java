package com.example.fastcampusmysql.domain.member.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private static final String TABLE = "member";
	private static final RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member.builder()
		.id(resultSet.getLong("id"))
		.email(resultSet.getString("email"))
		.nickname(resultSet.getString("nickname"))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.birthday(resultSet.getObject("birthday", LocalDate.class))
		.build();

	public Optional<Member> findById(Long id) {
		/*
			select *
			from Member
			where id = :id
		 */

		String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("id", id);


		Member member = jdbcTemplate.queryForObject(sql, param, rowMapper);
		return Optional.ofNullable(member);

	}

	public List<Member> findAllByIdIn(List<Long> ids) {
		if (ids.isEmpty()) {
			return List.of();
		}
		String sql = String.format("select from %s where id in (:ids)", TABLE);
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("ids", ids);
		return jdbcTemplate.query(sql, params, rowMapper);
	}

	public Member save(Member member) {
		/*
		member id를 보고 갱신 또는 삽입을 정함
		반환값은 id를 담아서 반환한다.
		 */
		if (member.getId() == null) {
			return insert((member));
		}
		return update(member);
	}

	private Member insert(Member member) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
			.withTableName("Member")
			.usingGeneratedKeyColumns("id");

		SqlParameterSource params = new BeanPropertySqlParameterSource(member);
		long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return Member.builder()
			.id(id)
			.email(member.getEmail())
			.birthday(member.getBirthday())
			.nickname(member.getNickname())
			.createdAt(member.getCreatedAt())
			.build();

	}

	private Member update(Member member) {
		// TODO : implement
		String sql = String.format(
			"UPDATE %s SET EMAIL = :email,  EMAIL = :email,  NICKNAME = :nickname,  BIRTHDAY = :birthday WHERE ID = :id ",
			TABLE);
		SqlParameterSource params = new BeanPropertySqlParameterSource(member);
		jdbcTemplate.update(sql, params);
		return member;
	}
}
