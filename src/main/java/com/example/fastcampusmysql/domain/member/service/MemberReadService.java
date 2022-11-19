package com.example.fastcampusmysql.domain.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberReadService {

	private final MemberRepository memberRepository;
	private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;


	public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
		return memberNicknameHistoryRepository
			.findAllByMemberId(memberId)
			.stream().map(this::toDto)
			.collect(Collectors.toList());
	}

	public MemberDto getMember(Long id) {
		return toDto(memberRepository.findById(id).orElseThrow());
	}

	public MemberDto toDto(Member member) {
		return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getBirthday());
	}

	public List<MemberDto> getMembers(List<Long> ids) {
		List<Member> members = memberRepository.findAllByIdIn(ids);
		return members.stream().map(this::toDto).toList();
	}

	private MemberNicknameHistoryDto toDto(MemberNicknameHistory history) {
		return new MemberNicknameHistoryDto(
			history.getId(),
			history.getMemberId(),
			history.getNickname(),
			history.getCreatedAt());
	}

}
