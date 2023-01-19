package com.example.fastcampusmysql.domain.post.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostWriteService {

	private final PostRepository postRepository;

	public Long create(PostCommand command) {
		Post post = Post.builder()
			.memberId(command.memberId())
			.contents(command.contents())
			.build();
		return postRepository.save(post).getId();
	}

	public void likePost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow();
		post.increamentLikeCount();
		postRepository.save(post);
	}
}
