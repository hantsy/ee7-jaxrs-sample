package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.model.Post;
import com.hantsylab.example.ee7.blog.repository.PostRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

}
