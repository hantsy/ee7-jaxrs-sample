package com.hantsylab.example.ee7.blog;

import com.hantsylab.example.ee7.blog.model.Post;
import com.hantsylab.example.ee7.blog.service.PostForm;

public class Fixtures {

    public static Post newPost(String title, String content) {
        return Post.builder().title(title).content(content).build();
    }

    public static PostForm newPostForm(String title, String content) {
        return PostForm.builder().title(title).content(content).build();
    }

}
