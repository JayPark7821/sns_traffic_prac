package com.example.fastcampusmysql.domain.member.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.fastcampusmysql.util.MemberFixtureFactory;

class MemberTest {

	@Test
	void 회원은_닉네임을_변경할_수_있다 () throws Exception{
	    //given
		Member member = MemberFixtureFactory.create();
		String toChangeName = "pnu";
		//when
		member.changeNickname(toChangeName);

	    //then
		Assertions.assertEquals(toChangeName , member.getNickname());
	}

	@Test
	void 회원의_닉네임은_10자를_초과할_수_없다() throws Exception{
	    //given
		Member member = MemberFixtureFactory.create();
		String overMaxLength = "jayjayjayjay";
	    //when
		//then
		Assertions.assertThrows(IllegalArgumentException.class, () -> member.changeNickname(overMaxLength));
	}
}