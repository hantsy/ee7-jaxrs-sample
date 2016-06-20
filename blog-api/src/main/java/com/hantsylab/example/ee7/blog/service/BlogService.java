package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.model.Post;
import com.hantsylab.example.ee7.blog.repository.PostRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author hantsy
 */
@ApplicationScoped
public class BlogService {

    @Inject
    private PostRepository posts;

    public PostDetail findPostById(Long id) {
        Post post = fetchPostById(id);

        return DTOUtils.map(post, PostDetail.class);
    }

    public List<PostDetail> findByKeyword(String q) {
        List<Post> postlist=posts.findByKeyword(q);
        
        return DTOUtils.mapList(postlist, PostDetail.class);
    }

    public PostDetail createPost(PostForm form) {
        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .build();
        Post saved = posts.save(post);

        return DTOUtils.map(saved, PostDetail.class);
    }

    public PostDetail updatePost(Long id, PostForm form) {
        Post post = fetchPostById(id);

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());

        Post saved = posts.save(post);

        return DTOUtils.map(saved, PostDetail.class);
    }

    public void deletePostById(Long id) {
        Post post = fetchPostById(id);
        posts.delete(post);
    }

    private Post fetchPostById(Long id) throws ResourceNotFoundException {
        Post post = posts.findById(id);
        if (post == null) {
            throw new ResourceNotFoundException("post:" + id + " was not found");
        }
        return post;
    }

}
