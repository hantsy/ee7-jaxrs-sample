package com.hantsylab.example.ee7.blog;

import com.hantsylab.example.ee7.blog.model.Post;

public class Fixtures {
	
	public static Post newPost(String title, String content){
		return Post.builder().title(title).content(content).build();
	}

}
