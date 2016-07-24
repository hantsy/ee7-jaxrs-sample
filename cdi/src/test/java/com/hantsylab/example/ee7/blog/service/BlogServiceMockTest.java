package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author hantsy
 */
public class BlogServiceMockTest {

    @Mock
    private PostRepository posts;

    @Mock
    private CommentRepository comments;

    @InjectMocks
    private BlogService service;

    @Before
    public void setup() {
        initMocks(this);
    }

    @After
    public void teardown() {
        reset(posts);
    }

    private static final String TITLE = "test_title";
    private static final String CONTENT = "test_content";

    @Test
    public void testGetPostById() {
        Post returned = Fixtures.newPost(TITLE, CONTENT);
        returned.setId(1L);

        when(posts.findById(1L))
            .thenReturn(returned);

        PostDetail detail = service.findPostById(1L);

        assertNotNull(detail.getId());
        assertEquals(TITLE, detail.getTitle());
        assertEquals(CONTENT, detail.getContent());

        verify(posts, times(1)).findById(anyLong());
    }

    @Test
    public void testGetPostByIdBDD() {

        Post returned = Fixtures.newPost(TITLE, CONTENT);
        returned.setId(1L);

        given(posts.findById(1L))
            .willReturn(returned);

        PostDetail detail = service.findPostById(1L);

        assertNotNull(detail.getId());
        assertEquals(TITLE, detail.getTitle());
        assertEquals(CONTENT, detail.getContent());

        verify(posts, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllPostsByKeyword() {

        Post returned = Fixtures.newPost(TITLE, CONTENT);
        returned.setId(1L);

        Post returned2 = Fixtures.newPost(TITLE + "2", CONTENT + "2");
        returned2.setId(2L);

        given(posts.findByKeyword("test"))
            .willReturn(Arrays.asList(returned, returned2));

        List<PostDetail> detailList = service.findByKeyword("test");

        assertEquals(2, detailList.size());
        verify(posts, times(1)).findByKeyword(anyString());
    }

    @Test
    public void testSavePost() {

        Post newPost = Fixtures.newPost(TITLE, CONTENT);

        Post returned = Fixtures.newPost(TITLE, CONTENT);
        returned.setId(1L);

        PostForm form = Fixtures.newPostForm(TITLE, CONTENT);

        given(posts.save(newPost))
            .willReturn(returned);

        PostDetail detail = service.createPost(form);

        assertNotNull(detail.getId());
        assertEquals(TITLE, detail.getTitle());
        assertEquals(CONTENT, detail.getContent());

        verify(posts, times(1)).save(any(Post.class));
    }

    @Test
    public void testUpdatePost() {

        Post originalPost = Fixtures.newPost(TITLE, CONTENT);
        originalPost.setId(1L);

        Post toUpdate = Fixtures.newPost(TITLE + "updated", CONTENT + "updated");
        toUpdate.setId(1L);

        Post returnedPost = Fixtures.newPost(TITLE + "updated", CONTENT + "updated");
        returnedPost.setId(1L);

        PostForm form = Fixtures.newPostForm(TITLE + "updated", CONTENT + "updated");

        given(posts.findById(1L))
            .willReturn(originalPost);
        given(posts.save(toUpdate))
            .willReturn(returnedPost);

        PostDetail detail = service.updatePost(1L, form);

        assertNotNull(detail.getId());
        assertEquals(TITLE + "updated", detail.getTitle());
        assertEquals(CONTENT + "updated", detail.getContent());

        verify(posts, times(1)).findById(anyLong());
        verify(posts, times(1)).save(any(Post.class));
    }

    @Test
    public void testDeletePost() {

        Post originalPost = Fixtures.newPost(TITLE, CONTENT);
        originalPost.setId(1L);

        Post toDelete = Fixtures.newPost(TITLE + "updated", CONTENT + "updated");
        toDelete.setId(1L);

        given(posts.findById(1L))
            .willReturn(originalPost);
        doNothing().when(posts).delete(toDelete);

        service.deletePostById(1L);

        verify(posts, times(1)).findById(anyLong());
        verify(posts, times(1)).delete(any(Post.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetPostByIdNotFound() {

        given(posts.findById(1L))
            .willThrow(ResourceNotFoundException.class);

        PostDetail detail = service.findPostById(1L);

        verify(posts, times(1)).findById(anyLong());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdatePostNotFound() {

        PostForm form = Fixtures.newPostForm(TITLE + "updated", CONTENT + "updated");

        given(posts.findById(1L))
            .willThrow(ResourceNotFoundException.class);

        PostDetail detail = service.updatePost(1L, form);

        verify(posts, times(1)).findById(anyLong());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeletePostNotFound() {

        given(posts.findById(1L))
            .willThrow(ResourceNotFoundException.class);

        service.deletePostById(1L);

        verify(posts, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllCommentsOfPost() {

        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(2L);
        c2.setPost(post);

        given(posts.findById(1L)).willReturn(post);
        given(comments.findByPost(post))
            .willReturn(Arrays.asList(c1, c2));

        List<CommentDetail> commentlList = service.getCommentsOfPost(1L);

        assertEquals(2, commentlList.size());
        verify(posts, times(1)).findById(anyLong());
        verify(comments, times(1)).findByPost(any(Post.class));
    }

    @Test
    public void testGetCommentById() {

        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        given(comments.findById(1L)).willReturn(c1);

        CommentDetail commentDetail = service.findCommentById(1L);

        assertEquals(CONTENT, commentDetail.getContent());
        assertTrue(1L == commentDetail.getId());
        verify(comments, times(1)).findById(anyLong());
    }

    @Test
    public void testCreateCommentsOfPost() {
        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        CommentForm cf1 = Fixtures.newCommentForm(CONTENT);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(1L);
        c2.setPost(post);

        given(posts.findById(1L)).willReturn(post);
        given(comments.save(c1))
            .willReturn(c2);

        CommentDetail commentDetail = service.createCommentOfPost(1L, cf1);

        assertEquals(CONTENT, commentDetail.getContent());
        assertTrue(1L == commentDetail.getId());
        verify(posts, times(1)).findById(anyLong());
        verify(comments, times(1)).save(any(Comment.class));
    }

    @Test
    public void testUpdateComment() {
        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        CommentForm cf1 = Fixtures.newCommentForm(CONTENT);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(1L);
        c2.setPost(post);

        given(comments.findById(1L)).willReturn(c1);
        given(comments.save(c1))
            .willReturn(c2);

        CommentDetail commentDetail = service.updateComment(1L, cf1);

        assertEquals(CONTENT, commentDetail.getContent());
        assertTrue(1L == commentDetail.getId());
        verify(comments, times(1)).findById(anyLong());
        verify(comments, times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment() {
        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(1L);
        c2.setPost(post);

        given(comments.findById(1L)).willReturn(c1);
        doNothing().when(comments).delete(c1);

        service.deleteCommentById(1L);

        verify(comments, times(1)).findById(anyLong());
        verify(comments, times(1)).delete(any(Comment.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateCommentNotFound() {
        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        CommentForm cf1 = Fixtures.newCommentForm(CONTENT);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(1L);
        c2.setPost(post);

        given(comments.findById(1L)).willReturn(null);

        CommentDetail commentDetail = service.updateComment(1L, cf1);

        verify(comments, times(1)).findById(anyLong());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteCommentNotFound() {
        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        Comment c2 = Fixtures.newComment(CONTENT);
        c2.setId(1L);
        c2.setPost(post);

        given(comments.findById(1L)).willReturn(null);

        service.deleteCommentById(1L);

        verify(comments, times(1)).findById(anyLong());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetCommentByIdNotFound() {

        Post post = Fixtures.newPost(TITLE, CONTENT);
        post.setId(1L);

        Comment c1 = Fixtures.newComment(CONTENT);
        c1.setId(1L);
        c1.setPost(post);

        given(comments.findById(1L)).willReturn(null);

        CommentDetail commentDetail = service.findCommentById(1L);

        verify(comments, times(1)).findById(anyLong());
    }

}
