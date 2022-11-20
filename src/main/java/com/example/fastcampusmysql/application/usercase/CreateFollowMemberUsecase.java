package com.example.fastcampusmysql.application.usercase;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.service.FollowWriteService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateFollowMemberUsecase {
	private final MemberReadService memberReadService;
	private final FollowWriteService followWriteService;

	public void execute(Long fromMemberId, Long toMemberId) {

		MemberDto fromMember = memberReadService.getMember(fromMemberId);
		MemberDto toMember = memberReadService.getMember(toMemberId);

		followWriteService.create(fromMember, toMember);
	}
}
