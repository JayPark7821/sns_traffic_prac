package com.example.fastcampusmysql.domain.follow.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowWriteService {

	private final FollowRepository followRepository;

	public void create(MemberDto fromMemberId, MemberDto toMemberId) {
		Assert.isTrue(fromMemberId.id().equals(toMemberId.id()), "From, to 회원이 동일합니다.");

		Follow follow = Follow.builder()
			.fromMemberId(fromMemberId.id())
			.toMemberId(toMemberId.id())
			.build();

		followRepository.save(follow);
	}
}
