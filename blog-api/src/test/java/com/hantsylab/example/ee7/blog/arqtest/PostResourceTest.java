package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.JaxrsActiviator;
import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider;
import com.hantsylab.example.ee7.blog.api.JacksonConfig;
import com.hantsylab.example.ee7.blog.api.PostResource;
import com.hantsylab.example.ee7.blog.api.ResourceNotFoundExceptionMapper;
import com.hantsylab.example.ee7.blog.api.ValidationError;
import com.hantsylab.example.ee7.blog.api.ValidationExceptionMapper;
import com.hantsylab.example.ee7.blog.domain.convert.LocalDateConverter;
import com.hantsylab.example.ee7.blog.domain.model.Comment;
import com.hantsylab.example.ee7.blog.domain.model.Comment_;
import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Post_;
import com.hantsylab.example.ee7.blog.domain.repository.CommentRepository;
import com.hantsylab.example.ee7.blog.domain.repository.PostRepository;
import com.hantsylab.example.ee7.blog.domain.support.AbstractEntity;
import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.CommentDetail;
import com.hantsylab.example.ee7.blog.service.CommentForm;
import com.hantsylab.example.ee7.blog.service.PostDetail;
import com.hantsylab.example.ee7.blog.service.PostForm;
import com.hantsylab.example.ee7.blog.service.ResourceNotFoundException;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hantsy
 */
@RunWith(Arquillian.class)
public class PostResourceTest {

    private static final Logger LOG = Logger.getLogger(PostResourceTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        File[] extraJars = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve(
                "org.projectlombok:lombok:1.16.8",
                "org.modelmapper:modelmapper:0.7.5",
                "org.apache.commons:commons-lang3:3.4",
                "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.6.3"
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
            //add service classes
            .addClasses(BlogService.class,
                ResourceNotFoundException.class,
                PostForm.class,
                PostDetail.class,
                CommentForm.class, 
                CommentDetail.class
            )
            //Add JAXRS resources classes
            .addClasses(
                JaxrsActiviator.class,
                PostResource.class,
                JacksonConfig.class,
                ResourceNotFoundExceptionMapper.class,
                ValidationExceptionMapper.class,
                ValidationError.class,
                CustomBeanParamProvider.class
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

    private static final String TITLE = "test_title";
    private static final String CONTENT = "test_content";

    @Test
    @RunAsClient
    public void testPosts() throws MalformedURLException {

        LOG.log(Level.INFO, "base url @{0}", base);

        //get all posts
        final WebTarget targetGetAll = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resGetAll = targetGetAll.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll.getStatus());
        PostDetail[] results = resGetAll.readEntity(PostDetail[].class);
        assertTrue(results != null);
        assertTrue(results.length == 0);

        //You have to close the response manually... issue# RESTEASY-1120
        //see https://issues.jboss.org/browse/RESTEASY-1120
        resGetAll.close();

        //create a new post
        PostForm newPostForm = Fixtures.newPostForm(TITLE, CONTENT);
        final WebTarget targetPost = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resPost = targetPost.request().post(Entity.json(newPostForm));
        assertEquals(201, resPost.getStatus());
        String location = resPost.getHeaderString("Location");
        LOG.log(Level.INFO, "saved post location @{0}", location);

        resPost.close();

        //verify new created post in the findAll result list.
        final WebTarget targetGetAll2 = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resGetAll2 = targetGetAll2.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll2.getStatus());
        PostDetail[] results2 = resGetAll2.readEntity(PostDetail[].class);
        assertTrue(results2 != null);
        assertTrue(results2.length == 1);

        resGetAll2.close();

        //get the created data
        final WebTarget targetGet = client.target(URI.create(new URL(location).toExternalForm()));
        Response responseGet = targetGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, responseGet.getStatus());
        LOG.log(Level.INFO, "get entity @{0}", responseGet);
        PostDetail result = responseGet.readEntity(PostDetail.class);
        assertNotNull(result.getId());
        assertEquals(TITLE, result.getTitle());
        assertEquals(CONTENT, result.getContent());

        responseGet.close();

        //update post form
        PostForm updatePostForm = Fixtures.newPostForm(TITLE + "updated", CONTENT + "updated");
        final WebTarget targetPut = client.target(URI.create(new URL(location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(updatePostForm));

        assertEquals(204, responsePut.getStatus());

        responsePut.close();

        //verify updated result
        final WebTarget targetVerifyUpdatedGet = client.target(URI.create(new URL(location).toExternalForm()));
        final Response responseVerifyUpdatedGet = targetVerifyUpdatedGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(200, responseVerifyUpdatedGet.getStatus());
        LOG.log(Level.INFO, "verifyUpdateGet entity @{0}", responseVerifyUpdatedGet);
        PostDetail verifyUpdateResult = responseVerifyUpdatedGet.readEntity(PostDetail.class);
        assertNotNull(verifyUpdateResult.getId());
        assertEquals(TITLE + "updated", verifyUpdateResult.getTitle());
        assertEquals(CONTENT + "updated", verifyUpdateResult.getContent());

        responseVerifyUpdatedGet.close();

        //delete post
        final WebTarget targetDelete = client.target(URI.create(new URL(location).toExternalForm()));
        final Response responseDelete = targetDelete
            .request()
            .delete();

        assertEquals(204, responseDelete.getStatus());

        responseDelete.close();
    }

    @Test
    @RunAsClient
    public void testGetPostNotFound() throws MalformedURLException {

        //get the created data
        String location = "api/posts/1000";
        final WebTarget targetGet = client.target(URI.create(new URL(base, location).toExternalForm()));
        Response responseGet = targetGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, responseGet.getStatus());
    }

    @Test
    @RunAsClient
    public void testUpdatePostNotFound() throws MalformedURLException {
        String location = "api/posts/1000";

        PostForm updatePostForm = Fixtures.newPostForm(TITLE + "updated", CONTENT + "updated");
        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(updatePostForm));

        assertEquals(404, responsePut.getStatus());
    }

    @Test
    @RunAsClient
    public void testDeletePostNotFound() throws MalformedURLException {
        String location = "api/posts/1000";

        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .delete();

        assertEquals(404, responsePut.getStatus());
    }

    @Test
    @RunAsClient
    public void testCreatePostFormIsInvalid() throws MalformedURLException {
        //create a new post
        PostForm newPostForm = new PostForm();
        final WebTarget targetPost = client.target(URI.create(new URL(base, "api/posts").toExternalForm()));
        final Response resPost = targetPost.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newPostForm));

        LOG.info("response content string @\n" + resPost.readEntity(String.class));
        assertEquals(400, resPost.getStatus());

        resPost.close();
    }

}
