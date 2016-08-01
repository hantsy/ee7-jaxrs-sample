package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
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

    @Inject
    private CommentRepository comments;

    public PostDetail findPostById(Long id) {
        Post post = fetchPostById(id);

        return DTOUtils.map(post, PostDetail.class);
    }

    public List<PostDetail> findByKeyword(String q) {
        List<Post> postlist = posts.findByKeyword(q);

        return DTOUtils.mapList(postlist, PostDetail.class);
    }

    public List<PostDetail> findByUsername(String username) {
        List<Post> postlist = posts.findByCreatedBy(username);
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

    private Comment fetchCommentById(Long id) throws ResourceNotFoundException {
        Comment comment = comments.findById(id);
        if (comment == null) {
            throw new ResourceNotFoundException("comment:" + id + " was not found");
        }
        return comment;
    }

    public List<CommentDetail> getCommentsOfPost(Long postId) {
        Post post = fetchPostById(postId);
        List<Comment> commentsOfPost = comments.findByPost(post);

        return DTOUtils.mapList(commentsOfPost, CommentDetail.class);
    }

    public CommentDetail createCommentOfPost(Long postId, CommentForm form) {
        Post post = fetchPostById(postId);
        Comment comment = Comment.builder()
            .content(form.getContent())
            .post(post)
            .build();

        Comment saved = comments.save(comment);

        return DTOUtils.map(saved, CommentDetail.class);
    }

    public CommentDetail findCommentById(Long id) {
        Comment detail = fetchCommentById(id);
        return DTOUtils.map(detail, CommentDetail.class);
    }

    public CommentDetail updateComment(Long commentId, CommentForm form) {

        Comment comment = fetchCommentById(commentId);
        comment.setContent(form.getContent());
        Comment saved = comments.save(comment);

        return DTOUtils.map(saved, CommentDetail.class);
    }

    public void deleteCommentById(Long commentId) {
        Comment comment = fetchCommentById(commentId);
        comments.delete(comment);
    }

}
