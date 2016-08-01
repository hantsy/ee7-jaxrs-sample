package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.domain.repository.UserRepository;
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
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.model.User_;
import com.hantsylab.example.ee7.blog.domain.support.AbstractEntity;
import com.hantsylab.example.ee7.blog.domain.model.Role;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class UserRepositoryTest {

    @Deployment(name = "test")
    public static Archive<?> createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            //domain.support package.
            .addPackage(AbstractEntity.class.getPackage())
            //domain.convert package.
            .addPackage(LocalDateConverter.class.getPackage())
            .addPackage(User.class.getPackage())
            .addPackage(UserRepository.class.getPackage())
            .addClasses(Fixtures.class, StringUtils.class)
            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        // System.out.println(archive.toString(true));
        return archive;
    }

    @Inject
    UserRepository users;

    private static final String FIRST_NAME = "hantsy";
    private static final String LAST_NAME = "bai";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test123";

    User _saved;

    @Before
    public void setUp() throws Exception {
        User post = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        _saved = users.save(post);
    }

    @After
    public void tearDown() throws Exception {
        users.delete(_saved);
    }

    @Test
    public void testFindAll() {
        List<User> foundUsers = users.findAll();
        assertEquals(1, foundUsers.size());
    }

    @Test
    public void testFindByUsername() {
        User userByUsername = users.findByUsername(USERNAME);
        assertNotNull(userByUsername);
    }

    @Test
    public void testFindByKeyword() {
        List<User> foundUsers = users.findByKeyword("test");
        assertEquals(1, foundUsers.size());
    }

    @Test
    public void testFindByKeywordNotFound() {
        List<User> foundUsers = users.findByKeyword("test123");
        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testFindById() {
        User found = users.findById(_saved.getId());
        assertNotNull(found);
        assertEquals(FIRST_NAME, found.getFirstName());
        assertEquals(LAST_NAME, found.getLastName());
        assertNotNull(found.getId());
        assertNotNull(found.getVersion());
    }

    @Test
    public void testCRUD() {
        User post = Fixtures.newUser(FIRST_NAME + "1", LAST_NAME + "1", USERNAME + "crud", PASSWORD);
        User saved = users.save(post);

        assertEquals(FIRST_NAME + "1", saved.getFirstName());
        assertEquals(LAST_NAME + "1", saved.getLastName());
        assertNotNull(saved.getId());
        assertNotNull(saved.getVersion());

        saved.setFirstName(FIRST_NAME + "updated");
        saved.setLastName(LAST_NAME + "updated");

        User updated = users.save(saved);

        assertEquals(FIRST_NAME + "updated", updated.getFirstName());
        assertEquals(LAST_NAME + "updated", updated.getLastName());

        Long id = updated.getId();
        assertNotNull(id);

        User found = users.findById(id);
        assertEquals(FIRST_NAME + "updated", found.getFirstName());
        assertEquals(LAST_NAME + "updated", found.getLastName());

        users.delete(found);

        assertNull(users.findById(id));
    }

}
