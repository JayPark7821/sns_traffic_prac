package com.example.fastcampusmysql.domain.post.repository;

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

import com.example.fastcampusmysql.domain.post.entity.PostLike;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {
	final static String TABLE = "PostLike";

	final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	final static private RowMapper<PostLike> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> PostLike.builder()
		.id(resultSet.getLong("id"))
		.memberId(resultSet.getLong("memberId"))
		.postId(resultSet.getLong("postId"))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();
 
	public PostLike save(PostLike postLike) {
		if (postLike.getId() == null) {
			return insert(postLike);
		}

		throw new UnsupportedOperationException("PostLike는 갱신을 지원하지 않습니다");
	}

	private PostLike insert(PostLike postLike) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
			.withTableName(TABLE)
			.usingGeneratedKeyColumns("id");

		SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
		var id = jdbcInsert.executeAndReturnKey(params).longValue();

		return PostLike.builder()
			.id(id)
			.memberId(postLike.getMemberId())
			.postId(postLike.getPostId())
			.createdAt(postLike.getCreatedAt())
			.build();
	}

	public Long getCount(Long postId) {
		String sql = String.format("""
			select count(id)
			from %s
			WHERE postId = :postId
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("postId", postId);
		return namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
	}


}