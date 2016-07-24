package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import com.hantsylab.example.ee7.blog.domain.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 *
 * @author hantsy
 */
public class UserServiceMockTest {

    @Mock
    private UserRepository users;

    @Mock
    private CommentRepository comments;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    @Before
    public void setup() {
        initMocks(this);
    }

    @After
    public void teardown() {
        reset(users);
    }

    private static final String FIRST_NAME = "test_firstname";
    private static final String LAST_NAME = "test_lastname";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test123";

    @Test
    public void testGetUserById() {
        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        when(users.findById(1L))
            .thenReturn(returned);

        UserDetail detail = service.findUserById(1L);

        assertNotNull(detail.getId());
        assertEquals(FIRST_NAME, detail.getFirstName());
        assertEquals(LAST_NAME, detail.getLastName());

        verify(users, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserByIdBDD() {

        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        given(users.findById(1L))
            .willReturn(returned);

        UserDetail detail = service.findUserById(1L);

        assertNotNull(detail.getId());
        assertEquals(FIRST_NAME, detail.getFirstName());
        assertEquals(LAST_NAME, detail.getLastName());

        verify(users, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserByUsername() {

        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        given(users.findByUsername(USERNAME))
            .willReturn(returned);

        UserDetail detail = service.findUserByUsername(USERNAME);

        assertEquals(FIRST_NAME, detail.getFirstName());
        assertEquals(LAST_NAME, detail.getLastName());
        verify(users, times(1)).findByUsername(anyString());
    }

    @Test
    public void testUsernameExists() {

        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);
        given(users.findByUsername(USERNAME))
            .willReturn(returned);

        boolean detail = service.usernameExists(USERNAME);

        assertTrue(detail);

        verify(users, times(1)).findByUsername(anyString());
    }

    @Test
    public void testUsernameExistsFalse() {

        given(users.findByUsername(USERNAME))
            .willReturn(null);

        boolean detail = service.usernameExists(USERNAME);

        assertFalse(detail);

        verify(users, times(1)).findByUsername(anyString());
    }

    @Test
    public void testGetAllUsersByKeyword() {

        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        User returned2 = Fixtures.newUser(FIRST_NAME + "2", LAST_NAME + "2", USERNAME, PASSWORD);
        returned2.setId(2L);

        given(users.findByKeyword("test"))
            .willReturn(Arrays.asList(returned, returned2));

        List<UserDetail> detailList = service.findByKeyword("test");

        assertEquals(2, detailList.size());
        verify(users, times(1)).findByKeyword(anyString());
    }

    @Test
    public void testSaveUser() {

        User newUser = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        UserForm form = Fixtures.newUserForm(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        given(users.findByUsername(USERNAME))
            .willReturn(null);

        given(users.save(newUser))
            .willReturn(returned);

        given(encoder.encode(PASSWORD)).willReturn(PASSWORD);

        UserDetail detail = service.createUser(form);

        assertNotNull(detail.getId());
        assertEquals(FIRST_NAME, detail.getFirstName());
        assertEquals(LAST_NAME, detail.getLastName());

        verify(users, times(1)).findByUsername(anyString());
        verify(users, times(1)).save(any(User.class));
        verify(encoder, times(1)).encode(anyString());
    }

    @Test
    public void testUpdateUser() {

        User originalUser = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        originalUser.setId(1L);

        User toUpdate = Fixtures.newUser(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);
        toUpdate.setId(1L);

        User returnedUser = Fixtures.newUser(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);
        returnedUser.setId(1L);

        UserForm form = Fixtures.newUserForm(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);

        given(users.findById(1L))
            .willReturn(originalUser);
        given(users.save(toUpdate))
            .willReturn(returnedUser);

        UserDetail detail = service.updateUser(1L, form);

        assertNotNull(detail.getId());
        assertEquals(FIRST_NAME + "updated", detail.getFirstName());
        assertEquals(LAST_NAME + "updated", detail.getLastName());

        verify(users, times(1)).findById(anyLong());
        verify(users, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser() {

        User originalUser = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        originalUser.setId(1L);

        User toDelete = Fixtures.newUser(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);
        toDelete.setId(1L);

        given(users.findById(1L))
            .willReturn(originalUser);
        doNothing().when(users).delete(toDelete);

        service.deleteUserById(1L);

        verify(users, times(1)).findById(anyLong());
        verify(users, times(1)).delete(any(User.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByIdNotFound() {

        given(users.findById(1L))
            .willReturn(null);

        UserDetail detail = service.findUserById(1L);

        verify(users, times(1)).findById(anyLong());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByUsernameNotFound() {

        given(users.findByUsername(USERNAME))
            .willReturn(null);

        UserDetail detail = service.findUserByUsername(USERNAME);

        verify(users, times(1)).findByUsername(anyString());
    }

    @Test(expected = UsernameWasTakenException.class)
    public void testCreateUserWhenUsernameWasTakenByOthers() {
        User returned = Fixtures.newUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        returned.setId(1L);

        given(users.findByUsername(USERNAME))
            .willReturn(returned);

        UserForm form = Fixtures.newUserForm(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        UserDetail detail = service.createUser(form);

        verify(users, times(1)).findByUsername(anyString());
        verify(users, times(0)).save(any(User.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateUserNotFound() {

        UserForm form = Fixtures.newUserForm(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);

        given(users.findById(1L))
            .willReturn(null);

        UserDetail detail = service.updateUser(1L, form);

        verify(users, times(1)).findById(anyLong());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteUserNotFound() {

        given(users.findById(1L))
            .willReturn(null);

        service.deleteUserById(1L);

        verify(users, times(1)).findById(anyLong());
    }

}
