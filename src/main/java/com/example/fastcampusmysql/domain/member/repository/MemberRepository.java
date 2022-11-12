package com.example.fastcampusmysql.domain.member.repository;

import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.member.entity.Member;

@Repository
public class MemberRepository {

	public Member save(Member member) {
		/*
		member id를 보고 갱신 또는 삽입을 정함
		반환값은 id를 담아서 반환한다.
		 */
		return Member.builder().build();
	}
}
