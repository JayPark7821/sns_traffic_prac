package com.example.fastcampusmysql.domain.follow.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.domain.follow.application.usercase.CreateFollowMemberUsecase;
import com.example.fastcampusmysql.domain.follow.application.usercase.GetFollowingMemberUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
	private final CreateFollowMemberUsecase createFollowMemberUsecase;
	private final GetFollowingMemberUsecase getFollowingMemberUsecase;

	@PostMapping("/{fromId}/{toId}")
	public void create(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId) {
		createFollowMemberUsecase.execute(fromId, toId);

	}

	@GetMapping("/members/{fromId}")
	public List<MemberDto> create(@PathVariable("fromId") Long fromId) {
		return getFollowingMemberUsecase.execute(fromId);

	}
}
