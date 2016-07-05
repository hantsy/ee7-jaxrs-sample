package com.hantsylab.example.ee7.blog;

import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.service.CommentForm;
import com.hantsylab.example.ee7.blog.service.PostForm;
import com.hantsylab.example.ee7.blog.service.UserForm;

public class Fixtures {

    public static Post newPost(String title, String content) {
        return Post.builder().title(title).content(content).build();
    }

    public static Comment newComment(String content) {
        return Comment.builder().content(content).build();
    }

    public static User newUser(String firstName, String lastName, String username, String password) {
        return User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .build();
    }

    public static PostForm newPostForm(String title, String content) {
        return PostForm.builder().title(title).content(content).build();
    }

    public static CommentForm newCommentForm(String content) {
        return CommentForm.builder().content(content).build();
    }

    public static UserForm newUserForm(String firstName, String lastName, String username, String password) {
        return UserForm.builder().firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .build();
    }
}
