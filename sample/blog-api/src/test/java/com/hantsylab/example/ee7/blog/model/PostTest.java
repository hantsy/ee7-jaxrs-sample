package com.hantsylab.example.ee7.blog.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.hantsylab.example.ee7.blog.Fixtures;

public class PostTest {
	private static final String TITLE="test_title";
	private static final String CONTENT="test_content";

	@Test
	public void testPost() {
		Post post=Fixtures.newPost(TITLE, CONTENT);
		assertNull(post.getId());
		assertEquals(TITLE, post.getTitle());
		assertEquals(CONTENT, post.getContent());
	}

}
