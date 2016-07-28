package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.JaxrsActiviator;
import com.hantsylab.example.ee7.blog.api.AuthResource;
import com.hantsylab.example.ee7.blog.api.AuthenticationExceptionMapper;
import com.hantsylab.example.ee7.blog.api.CommentResource;
import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider;
import com.hantsylab.example.ee7.blog.api.JacksonConfig;
import com.hantsylab.example.ee7.blog.api.PostResource;
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
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.security.UserPrincipal;
import com.hantsylab.example.ee7.blog.security.filter.AuthenticationFilter;
import com.hantsylab.example.ee7.blog.security.filter.AuthorizationFilter;
import com.hantsylab.example.ee7.blog.security.jwt.JwtHelper;
import com.hantsylab.example.ee7.blog.security.jwt.JwtUser;
import com.hantsylab.example.ee7.blog.service.AuthenticationException;
import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.CommentDetail;
import com.hantsylab.example.ee7.blog.service.CommentForm;
import com.hantsylab.example.ee7.blog.service.Credentials;
import com.hantsylab.example.ee7.blog.service.IdToken;
import com.hantsylab.example.ee7.blog.service.PostDetail;
import com.hantsylab.example.ee7.blog.service.PostForm;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hantsy
 */
@RunWith(Arquillian.class)
public class JwtAuthTest {

    private static final Logger LOG = Logger.getLogger(JwtAuthTest.class.getName());

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
                ResourceNotFoundException.class,
                PostForm.class,
                PostDetail.class,
                CommentForm.class,
                CommentDetail.class
            )
            .addClasses(
                UserService.class,
                ResourceNotFoundException.class,
                UsernameWasTakenException.class,
                UserForm.class,
                UserDetail.class,
                Credentials.class,
                SignupForm.class,
                IdToken.class,
                Credentials.class
            )
            //Add JAXRS resources classes
            .addClasses(
                JaxrsActiviator.class,
                PostResource.class,
                UserResource.class,
                CommentResource.class,
                JacksonConfig.class,
                ResourceNotFoundExceptionMapper.class,
                ValidationExceptionMapper.class,
                ValidationError.class,
                CustomBeanParamProvider.class,
                AuthenticationException.class,
                AuthenticationExceptionMapper.class,
                AuthResource.class
            )
            .addPackage(PlainPasswordEncoder.class.getPackage())
            .addPackage(BCryptPasswordEncoder.class.getPackage())
            .addPackage(PasswordEncoder.class.getPackage())
            .addClasses(
                AuthenticationFilter.class,
                AuthorizationFilter.class,
                JwtHelper.class,
                JwtUser.class,
                UserPrincipal.class,
                Secured.class
            )
            .addClasses(
                Initializer.class
            )
            // .addAsResource("test-log4j.properties", "log4j.properties")
            //Add JPA persistence configration.
            //WARN: In a war package, persistence.xml should be put into /WEB-INF/classes/META-INF/, not /META-INF
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
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
    }

    @After
    public void teardown() throws MalformedURLException {
        client.close();
    }

    @Test
    @RunAsClient
    public void testGetPostsWithoutAuthentication() throws MalformedURLException {

        LOG.log(Level.INFO, "base url @{0}", base);

        //get all posts
        final WebTarget targetGetAll = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resGetAll = targetGetAll.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(401, resGetAll.getStatus());

        //You have to close the response manually... issue# RESTEASY-1120
        //see https://issues.jboss.org/browse/RESTEASY-1120
        resGetAll.close();

    }

    @Test
    @RunAsClient
    public void testGetPostsWithAuthentication() throws MalformedURLException {
        LOG.log(Level.INFO, "base url @{0}", base);

        final WebTarget targetAuthGetAll = client.target(URI.create(new URL(base, "api/auth/login").toExternalForm()));
        final Response resAuthGetAll = targetAuthGetAll.request()
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(new Credentials("testuser", "test123", true)));
        assertEquals(200, resAuthGetAll.getStatus());
        IdToken token = resAuthGetAll.readEntity(IdToken.class);

        client.register(new JwtTokenAuthentication(token.getToken()));

        //get all posts
        final WebTarget targetGetAll = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resGetAll = targetGetAll.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll.getStatus());

        PostDetail[] posts = resGetAll.readEntity(PostDetail[].class);

        assertTrue(posts.length == 1);

        Long id = posts[0].getId();

        //You have to close the response manually... issue# RESTEASY-1120
        //see https://issues.jboss.org/browse/RESTEASY-1120
        resGetAll.close();

        //get all posts
        final WebTarget targetDelAll = client.target(URI.create(new URL(base, "api/posts/"+id).toExternalForm()));
        final Response resDelAll = targetDelAll.request().accept(MediaType.APPLICATION_JSON_TYPE).delete();
        assertEquals(403, resDelAll.getStatus());
    }

}
