package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.JaxrsActiviator;
import com.hantsylab.example.ee7.blog.api.AuthResource;
import com.hantsylab.example.ee7.blog.api.AuthenticationExceptionMapper;
import com.hantsylab.example.ee7.blog.api.CommentResource;
import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider;
import com.hantsylab.example.ee7.blog.api.JacksonConfig;
import com.hantsylab.example.ee7.blog.api.PasswordMismatchedExceptionMapper;
import com.hantsylab.example.ee7.blog.api.PostResource;
import com.hantsylab.example.ee7.blog.api.ProfileResource;
import com.hantsylab.example.ee7.blog.api.ResourceNotFoundExceptionMapper;
import com.hantsylab.example.ee7.blog.api.UserResource;
import com.hantsylab.example.ee7.blog.api.ValidationError;
import com.hantsylab.example.ee7.blog.api.ValidationExceptionMapper;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.bcrypt.BCryptPasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.plain.PlainPasswordEncoder;
import com.hantsylab.example.ee7.blog.domain.convert.LocalDateConverter;
import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Comment_;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Post_;
import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.model.User_;
import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
import com.hantsylab.example.ee7.blog.domain.repository.UserRepository;
import com.hantsylab.example.ee7.blog.domain.support.AbstractEntity;
import com.hantsylab.example.ee7.blog.security.AuthenticatedUser;
import com.hantsylab.example.ee7.blog.security.AuthenticatedUserLiteral;
import com.hantsylab.example.ee7.blog.security.AuthenticatedUserProducer;
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.security.filter.AuthenticationFilter;
import com.hantsylab.example.ee7.blog.security.filter.AuthorizationFilter;
import com.hantsylab.example.ee7.blog.security.jwt.JwtHelper;
import com.hantsylab.example.ee7.blog.service.AuthenticationException;
import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.CommentDetail;
import com.hantsylab.example.ee7.blog.service.CommentForm;
import com.hantsylab.example.ee7.blog.service.Credentials;
import com.hantsylab.example.ee7.blog.service.IdToken;
import com.hantsylab.example.ee7.blog.service.PasswordForm;
import com.hantsylab.example.ee7.blog.service.PasswordMismatchedException;
import com.hantsylab.example.ee7.blog.service.PostDetail;
import com.hantsylab.example.ee7.blog.service.PostForm;
import com.hantsylab.example.ee7.blog.service.ProfileForm;
import com.hantsylab.example.ee7.blog.service.ResourceNotFoundException;
import com.hantsylab.example.ee7.blog.service.SignupForm;
import com.hantsylab.example.ee7.blog.service.UserDetail;
import com.hantsylab.example.ee7.blog.service.UserForm;
import com.hantsylab.example.ee7.blog.service.UserService;
import com.hantsylab.example.ee7.blog.service.UsernameWasTakenException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hantsy
 */
@RunWith(Arquillian.class)
public class ProfiletResourceTest {

    private static final Logger LOG = Logger.getLogger(ProfiletResourceTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        File[] extraJars = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve(
                "org.projectlombok:lombok:1.16.8",
                "org.modelmapper:modelmapper:0.7.5",
                "org.apache.commons:commons-lang3:3.4",
                "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.6.3",
                "io.jsonwebtoken:jjwt:0.6.0"
            )
            .withTransitivity()
            .asFile();

        final WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addAsLibraries(extraJars)
            .addClasses(DTOUtils.class, Fixtures.class)
            //domain.support package.
            .addPackage(AbstractEntity.class.getPackage())
            //domain.convert package.
            .addPackage(LocalDateConverter.class.getPackage())
            .addClasses(
                Post.class,
                Post_.class,
                PostRepository.class,
                Comment.class,
                Comment_.class,
                CommentRepository.class
            )
            .addClasses(
                Role.class,
                User.class,
                User_.class,
                UserRepository.class//,
            )
            //add service classes
            .addClasses(
                BlogService.class,
                UserService.class,
                ResourceNotFoundException.class,
                UsernameWasTakenException.class,
                PasswordMismatchedException.class,
                UserForm.class,
                UserDetail.class,
                Credentials.class,
                SignupForm.class,
                IdToken.class,
                Credentials.class,
                PostForm.class,
                PostDetail.class,
                CommentForm.class,
                CommentDetail.class,
                ProfileForm.class,
                PasswordForm.class
            )
            //Add JAXRS resources classes
            .addClasses(
                JaxrsActiviator.class,
                PostResource.class,
                ProfileResource.class,
                UserResource.class,
                AuthResource.class,
                CommentResource.class,
                JacksonConfig.class,
                ResourceNotFoundExceptionMapper.class,
                ValidationExceptionMapper.class,
                ValidationError.class,
                CustomBeanParamProvider.class,
                AuthenticationException.class,
                AuthenticationExceptionMapper.class,
                PasswordMismatchedExceptionMapper.class
            )
            .addPackage(PlainPasswordEncoder.class.getPackage())
            .addPackage(BCryptPasswordEncoder.class.getPackage())
            .addPackage(PasswordEncoder.class.getPackage())
            .addClasses(
                AuthenticationFilter.class,
                AuthorizationFilter.class,
                JwtHelper.class,
                //JwtUser.class,
                //UserPrincipal.class,
                AuthenticatedUser.class,
                AuthenticatedUserProducer.class,
                AuthenticatedUserLiteral.class,
                Secured.class
            )
            .addClasses(TestDataInitializer.class
            )
            // .addAsResource("test-log4j.properties", "log4j.properties")
            //Add JPA persistence configration.
            //WARN: In a war package, persistence.xml should be put into /WEB-INF/classes/META-INF/, not /META-INF
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("META-INF/test-orm.xml", "META-INF/orm.xml")
            // Enable CDI
            //WARN: In a war package, persistence.xml should be put into /WEB-INF not /META-INF
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        //  .addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml");

        LOG.log(Level.INFO, "war to string @{0}", war.toString());
        return war;
    }

    @ArquillianResource
    private URL base;

    private Client client;

    @Before
    public void setup() throws MalformedURLException {
        client = ClientBuilder.newClient();
        client.register(JacksonConfig.class);
        client.register(ResourceNotFoundExceptionMapper.class);
        client.register(ValidationExceptionMapper.class);

        final WebTarget targetAuthGetAll = client.target(URI.create(new URL(base, "api/auth/login").toExternalForm()));
        final Response resAuthGetAll = targetAuthGetAll.request()
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(new Credentials("admin", "admin123", true)));
        assertEquals(200, resAuthGetAll.getStatus());
        IdToken token = resAuthGetAll.readEntity(IdToken.class);

        client.register(new JwtTokenAuthentication(token.getToken()));
    }

    @After
    public void teardown() throws MalformedURLException {
        client.close();
    }

    private static final String TITLE = "test_title";
    private static final String CONTENT = "test_content";

    @Test
    @RunAsClient
    public void testWhenAddedNewPostShouldReturnMyPostsSuccessfully() throws MalformedURLException {

        LOG.log(Level.INFO, "base url @{0}", base);

        //create a new post
        PostForm newPostForm = Fixtures.newPostForm(TITLE, CONTENT);
        final WebTarget targetPost = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resPost = targetPost.request().post(Entity.json(newPostForm));
        assertEquals(201, resPost.getStatus());
        String savedPostLocaiton = resPost.getHeaderString("Location");
        LOG.log(Level.INFO, "saved post location @{0}", savedPostLocaiton);

        resPost.close();

        //verify new created post in the findAll result list.
        final WebTarget targetGetAll2 = client.target(URI.create(new URL(base, "api/me/posts").toExternalForm()));
        final Response resGetAll2 = targetGetAll2.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll2.getStatus());
        PostDetail[] results2 = resGetAll2.readEntity(PostDetail[].class);
        assertTrue(results2 != null);
        assertTrue(results2.length == 1);

        resGetAll2.close();
    }

    @Test
    @RunAsClient
    public void testGetMyProfile() throws MalformedURLException {

        String location = "api/me";

        final WebTarget targetGet = client.target(URI.create(new URL(base, location).toExternalForm()));
        Response responseGet = targetGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, responseGet.getStatus());

        UserDetail details = responseGet.readEntity(UserDetail.class);
        assertTrue("firstName should be Foo", "Foo".equals(details.getFirstName()));
        assertTrue("lastName should be Bar" ,"Bar".equals(details.getLastName()));
    }

    @Test
    @RunAsClient
    public void testUpdateProfile() throws MalformedURLException {
        String location = "api/me/profile";

        ProfileForm form = new ProfileForm("foo", "bar");
        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(form));

        assertEquals(204, responsePut.getStatus());
    }

    @Test
    @RunAsClient
    public void testUpdatePassword() throws MalformedURLException {
        String location = "api/me/password";

        PasswordForm form = new PasswordForm("admin123", "admin1234");
        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(form));

        assertEquals(204, responsePut.getStatus());
    }

}
