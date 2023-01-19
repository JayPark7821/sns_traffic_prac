package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.util.PageHelper;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostRepository {
	private static final String TABLE = "Post";
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER = (ResultSet resultSet, int rouNum)
		-> new DailyPostCount(resultSet.getLong("memberId"),
		resultSet.getObject("createdDate", LocalDate.class),
		resultSet.getLong("count"));

	private static final RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Post.builder()
		.id(resultSet.getLong("id"))
		.memberId(resultSet.getLong("memberId"))
		.contents(resultSet.getString("contents"))
		.createdDate(resultSet.getObject("createdDate", LocalDate.class))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.likeCount(resultSet.getLong("likeCount"))
		.build();

	public void bulkInsert(List<Post> posts) {
		String sql = String.format("""
				INSERT INTO `%s` (memberId, contents, createdDate, createdAt)
				VALUES (:memberId, :contents, :createdDate, :createdAt)
			""", TABLE);

		SqlParameterSource[] params = posts.stream()
			.map(BeanPropertySqlParameterSource::new)
			.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
	}

	public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
		String sql = String.format("""
				SELECT createdDate, memberId, count(id)
				FROM %s
				WHERE memberId = :memberId and createdDate between :firstDate and : lastDate
				GROUP BY memberId, createdDate
			""", TABLE);
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(request);
		return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
	}

	public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
		MapSqlParameterSource params = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("size", pageable.getPageSize())
			.addValue("offset", pageable.getOffset());

		String sql = String.format("""
				SELECT * 
				FROM %s
				WHERE memberId = :memberId
				ORDER BY %s
				LIMIT :size
				OFFSET :offset
			""", TABLE, PageHelper.orderBy(pageable.getSort()));
		List<Post> posts = namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
		return new PageImpl(posts, pageable, getCount(memberId));

	}

	public Optional<Post> findById(Long id) {
		String sql = String.format("""
				SELECT * 
				FROM %s
				WHERE id = :postId
			""", TABLE);
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("postId", id);
		return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, params, ROW_MAPPER));
	}

	public List<com.example.fastcampusmysql.domain.post.entity.Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, Long size) {
		String sql = String.format("""
			select *
			from %s
			WHERE memberId = :memberId
			ORDER BY id desc
			LIMIT :size
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("size", size);
		return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
	}

	public List<Post> findAllByInMemberIdsAndOrderByIdDesc(List<Long> memberIds, Long size) {
		if (memberIds.isEmpty()) {
			return List.of();
		}
		String sql = String.format("""
			select *
			from %s
			WHERE memberId in (:memberIds)
			ORDER BY id desc
			LIMIT :size
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("memberIds", memberIds)
			.addValue("size", size);
		return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
	}
	public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, Long size) {
		String sql = String.format("""
			select *
			from %s
			WHERE memberId = :memberId and id < :id
			ORDER BY id desc
			LIMIT :size
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("memberId", memberId)
			.addValue("size", size)
			.addValue("id", id);
		return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
	}

	public List<Post> findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(Long id, List<Long> memberIds, Long size) {
		if (memberIds.isEmpty()) {
			return List.of();
		}

		String sql = String.format("""
			select *
			from %s
			WHERE memberId in (:memberIds) and id < :id
			ORDER BY id desc
			LIMIT :size
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("memberIds", memberIds)
			.addValue("size", size)
			.addValue("id", id);
		return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
	}
	private Long getCount(Long memberId) {
		String sql = String.format("""
			select count(id)
			from %s
			WHERE memberId = :memberId
			""", TABLE);
		MapSqlParameterSource param = new MapSqlParameterSource()
			.addValue("memberId", memberId);
		return namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
	}

	public Post save(Post post) {

		if (post.getId() == null) {
			return insert(post);
		}
		return update(post);
	}

	public List<Post> findAllByIdIn(List<Long> postIds) {
		if (postIds.isEmpty()) {
			return List.of();
		}

		var params = new MapSqlParameterSource()
			.addValue("postIds", postIds);

		String query = String.format("""
                SELECT *
                FROM %s
                WHERE id in (:postIds)
                """, TABLE);

		return namedParameterJdbcTemplate.query(query, params, ROW_MAPPER);

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

	private Post update(Post post) {
		// TODO : implement
		String sql = String.format(
			"""
			UPDATE %s SET 
			 MEMBERID = :memberId,
			 CONTENTS = :contents,
			 CREATEDDATE = :createdDate,
			 LIKECOUNT = :likeCount,
			 CREATEDAT = :createdAt
			WHERE ID = :id 
			""",
			TABLE);
		SqlParameterSource params = new BeanPropertySqlParameterSource(post);
		namedParameterJdbcTemplate.update(sql, params);
		return post;
	}

}
