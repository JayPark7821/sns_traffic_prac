package com.example.fastcampusmysql.domain.follow.application.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.domain.follow.application.usercase.CreateFollowMemberUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
	private final CreateFollowMemberUsecase createFollowMemberUsecase;

	@PostMapping("/{fromId}/{toId}")
	public void register(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId) {
		createFollowMemberUsecase.execute(fromId, toId);

	}
}
