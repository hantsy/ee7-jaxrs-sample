package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.domain.convert.LocalDateConverter;
import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Comment_;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Post_;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
import com.hantsylab.example.ee7.blog.domain.support.AbstractEntity;

@RunWith(Arquillian.class)
public class CommentRepositoryTest {

    @Deployment(name = "test")
    public static Archive<?> createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            //domain.support package.
            .addPackage(AbstractEntity.class.getPackage())
            //domain.convert package.
            .addPackage(LocalDateConverter.class.getPackage())
            .addClasses(Post.class, Post_.class, Comment.class, Comment_.class)
            .addClasses(PostRepository.class, CommentRepository.class)
            .addClasses(Fixtures.class, StringUtils.class)
            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        // System.out.println(archive.toString(true));
        return archive;
    }

    @Inject
    CommentRepository comments;

    @Inject
    PostRepository posts;

    private static final String CONTENT = "test_content";

    Comment _saved;
    Post _post;

    @Before
    public void setUp() throws Exception {
        Comment comment = Fixtures.newComment(CONTENT);
        Post post = Fixtures.newPost("test title", "test content");
        _post = posts.save(post);
        comment.setPost(_post);
        _saved = comments.save(comment);
    }

    @After
    public void tearDown() throws Exception {
        comments.delete(_saved);
        posts.delete(_post);
    }

    @Test
    public void testFindAll() {
        List<Comment> foundComments = comments.findAll();
        assertEquals(1, foundComments.size());
    }

    @Test
    public void testFindByPost() {
        List<Comment> foundComments = comments.findByPost(_post);
        assertEquals(1, foundComments.size());
    }

    @Test
    public void testFindById() {
        Comment found = comments.findById(_saved.getId());
        assertNotNull(found);
        assertEquals(CONTENT, found.getContent());
        assertNotNull(found.getId());
        assertNotNull(found.getVersion());
    }

    @Test
    public void testCRUD() {
        Comment comment = Fixtures.newComment(CONTENT + "1");
        Comment saved = comments.save(comment);

        assertEquals(CONTENT + "1", saved.getContent());
        assertNotNull(saved.getId());
        assertNotNull(saved.getVersion());

        saved.setContent(CONTENT + "updated");

        Comment updated = comments.save(saved);

        assertEquals(CONTENT + "updated", updated.getContent());

        Long id = updated.getId();
        assertNotNull(id);

        Comment found = comments.findById(id);
        assertEquals(CONTENT + "updated", found.getContent());

        comments.delete(found);

        assertNull(comments.findById(id));
    }

}
