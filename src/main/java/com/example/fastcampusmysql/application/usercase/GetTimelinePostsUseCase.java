package com.example.fastcampusmysql.application.usercase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.TimelineReadService;
import com.example.fastcampusmysql.domain.util.CursorRequest;
import com.example.fastcampusmysql.domain.util.PageCursor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTimelinePostsUseCase {

	private final FollowReadService followReadService;
	private final PostReadService postReadService;
	private final TimelineReadService timelineReadService;

	public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
		List<Follow> followings = followReadService.getFollowings(memberId);
		List<Long> followingMemberIds = followings.stream().map(Follow::getToMemberId).toList();
		return postReadService.getPosts(followingMemberIds, cursorRequest);
	}

	public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
		PageCursor<Timeline> pagedTimelines = timelineReadService.getTimelines(memberId, cursorRequest);
		List<Long> postIds = pagedTimelines.body().stream().map(Timeline::getPostId).toList();
		var posts = postReadService.getPosts(postIds);
		return new PageCursor<>(pagedTimelines.nextCursorRequest(), posts);
	}

}
