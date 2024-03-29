package com.example.fastcampusmysql.application.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.application.usercase.CreatePostLikeUsecase;
import com.example.fastcampusmysql.application.usercase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usercase.GetTimelinePostsUseCase;
import com.example.fastcampusmysql.domain.member.dto.PostDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.util.CursorRequest;
import com.example.fastcampusmysql.domain.util.PageCursor;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostWriteService postWriteService;
	private final PostReadService postReadService;
	private final GetTimelinePostsUseCase getTimelinePostsUseCase;
	private final CreatePostUsecase createPostUsecase;
	private final CreatePostLikeUsecase createPostLikeUsecase;


	@PostMapping("")
	public Long create(PostCommand command) {
		return createPostUsecase.execute(command);
	}

	@GetMapping("/daily-post-counts")
	public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
		return postReadService.getDailyPostCount(request);
	}

	@GetMapping("/members/{memberId}")
	public Page<PostDto> getPosts(
		@PathVariable Long memberId,
		Pageable pageable
		// @RequestParam Integer page,
		// @RequestParam Integer size
	) {
		return postReadService.getPosts(memberId, pageable );//PageRequest.of(page, size));
	}

	@GetMapping("/members/{memberId}/by-cursor")
	public PageCursor<Post> getPostsByCursor(
		@PathVariable Long memberId,
		CursorRequest cursorRequest
	) {
		return postReadService.getPosts(memberId, cursorRequest);
	}

	@GetMapping("/members/{memberId}/timeline")
	public PageCursor<Post> getTimeline(
		@PathVariable Long memberId,
		CursorRequest cursorRequest
	) {
		return getTimelinePostsUseCase.execute(memberId, cursorRequest);
	}

	@PostMapping("/{postId}/likes/v1")
	public void likePost(@PathVariable Long postId) {
		// postWriteService.likePost(postId);
		postWriteService.likePostByOptimisticLock(postId);
	}


	@PostMapping("/{postId}/likes/v2")
	public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId) {
		createPostLikeUsecase.execute(postId, memberId);
	}



}
