package com.example.fastcampusmysql.domain.post.service;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalLong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.domain.util.CursorRequest;
import com.example.fastcampusmysql.domain.util.PageCursor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostReadService {
	private final PostRepository postRepository;

	public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
		return postRepository.groupByCreatedDate(request);
	}

	public Page<Post> getPosts(Long memberId, Pageable pageable) {
		return postRepository.findAllByMemberId(memberId, pageable);
	}

	public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
		List<Post> posts = findAllBy(memberId, cursorRequest);
		long nextKey = posts.stream()
			.mapToLong(Post::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new PageCursor<>(cursorRequest.next(nextKey), posts);
	}

	private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId,
				cursorRequest.size());
		} else {
			return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
		}
	}
}
