package com.example.fastcampusmysql.domain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowReadService {
	private final FollowRepository followRepository;

	public List<Follow> getFollowings(Long memberId) {
		return followRepository.findAllByFromMemberId(memberId);
	}

	public List<Follow> getFollowers(Long memberId) {
		return followRepository.findAllByToMemberId(memberId);
	}


}
