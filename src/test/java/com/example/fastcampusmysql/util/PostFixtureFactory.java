package com.example.fastcampusmysql.util;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import com.example.fastcampusmysql.domain.post.entity.Post;

public class PostFixtureFactory {

	public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate) {
		Predicate<Field> memberIdPredicate = named("memberId").and(ofType(Long.class))
			.and(inClass(Post.class));

		Predicate<Field> idPredicate = named("id").and(ofType(Long.class))
			.and(inClass(Post.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.excludeField(idPredicate)
			.dateRange(firstDate, lastDate)
			.randomize(memberIdPredicate, () -> memberId);
		return new EasyRandom(param);
	}
}
