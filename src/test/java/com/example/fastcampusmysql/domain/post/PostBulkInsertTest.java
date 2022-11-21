package com.example.fastcampusmysql.domain.post;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;

@SpringBootTest
public class PostBulkInsertTest {
	@Autowired
	private PostRepository postRepository;
	
	@Test
	void bulkInsert() throws Exception{
	    //given
		EasyRandom easyRandom = PostFixtureFactory.get(3L, LocalDate.of(2022, 1, 1),
			LocalDate.of(2022, 2, 1));
		//when
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		List<Post> posts = IntStream.range(0, 10000 * 100)
			.parallel()
			.mapToObj(i -> easyRandom.nextObject(Post.class))
			.toList();

		stopWatch.stop();
		System.out.println("stopWatch = " + stopWatch.getTotalTimeSeconds());
		// .forEach(x -> {
			// 	// postRepository.save(x);
			// 	System.out.println(x.getMemberId() + " / " + x.getCreatedDate());
			// });
	    //then
	}
}
