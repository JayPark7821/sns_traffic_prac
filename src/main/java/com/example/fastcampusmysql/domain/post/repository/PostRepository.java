package com.example.fastcampusmysql.domain.post.repository;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostRepository {
	private static final String TABLE = "Post";
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Post save(Post post) {

		if (post.getId() == null) {
			return insert(post);
		}
		throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
	}

	private Post insert(Post post) {
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
			.withTableName(TABLE)
			.usingGeneratedKeyColumns("id");

		SqlParameterSource param = new BeanPropertySqlParameterSource(post);
		long id = jdbcInsert.executeAndReturnKey(param).longValue();
		return Post.builder()
			.id(id)
			.memberId(post.getMemberId())
			.contents(post.getContents())
			.createdAt(post.getCreatedAt())
			.createdDate(post.getCreatedDate())
			.build();
	}

}
