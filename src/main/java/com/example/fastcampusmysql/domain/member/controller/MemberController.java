package com.example.fastcampusmysql.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;

	@PostMapping("/members")
	public Member register(@RequestBody RegisterMemberCommand command) {
		return memberWriteService.register(command);
	}

	@GetMapping("/members/{id")
	public MemberDto getMember(@PathVariable("id") Long id) {
		return memberReadService.getMember(id);
	}
}
