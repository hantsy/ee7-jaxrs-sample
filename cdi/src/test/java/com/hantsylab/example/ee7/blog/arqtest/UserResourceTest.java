package com.hantsylab.example.ee7.blog.arqtest;

import com.hantsylab.example.ee7.blog.DTOUtils;
import com.hantsylab.example.ee7.blog.Fixtures;
import com.hantsylab.example.ee7.blog.JaxrsActiviator;
import com.hantsylab.example.ee7.blog.api.AuthResource;
import com.hantsylab.example.ee7.blog.api.AuthenticationExceptionMapper;
import com.hantsylab.example.ee7.blog.api.CustomBeanParamProvider;
import com.hantsylab.example.ee7.blog.api.JacksonConfig;
import com.hantsylab.example.ee7.blog.api.UserResource;
import com.hantsylab.example.ee7.blog.api.ResourceNotFoundExceptionMapper;
import com.hantsylab.example.ee7.blog.api.UsernameWasTakenExceptionMapper;
import com.hantsylab.example.ee7.blog.api.ValidationError;
import com.hantsylab.example.ee7.blog.api.ValidationExceptionMapper;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.bcrypt.BCryptPasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.plain.PlainPasswordEncoder;
import com.hantsylab.example.ee7.blog.domain.convert.LocalDateConverter;
import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.model.User_;
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
import com.hantsylab.example.ee7.blog.service.Credentials;
import com.hantsylab.example.ee7.blog.service.IdToken;
import com.hantsylab.example.ee7.blog.service.UserDetail;
import com.hantsylab.example.ee7.blog.service.UserForm;
import com.hantsylab.example.ee7.blog.service.ResourceNotFoundException;
import com.hantsylab.example.ee7.blog.service.SignupForm;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hantsy
 */
@RunWith(Arquillian.class)
public class UserResourceTest {

    private static final Logger LOG = Logger.getLogger(UserResourceTest.class.getName());

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
                Role.class,
                User.class,
                User_.class,
                UserRepository.class//,
            //                Comment.class,
            //                Comment_.class,
            //                CommentRepository.class
            )
            //add service classes
            .addClasses(
                UserService.class,
                ResourceNotFoundException.class,
                UsernameWasTakenException.class,
                UserForm.class,
                UserDetail.class,
                Credentials.class,
                SignupForm.class,
                IdToken.class
            //                CommentForm.class,
            //                CommentDetail.class
            )
            //Add JAXRS resources classes
            .addClasses(
                JaxrsActiviator.class,
                UserResource.class,
                AuthResource.class,
                JacksonConfig.class,
                ResourceNotFoundExceptionMapper.class,
                ValidationExceptionMapper.class,
                UsernameWasTakenExceptionMapper.class,
                AuthenticationException.class,
                AuthenticationExceptionMapper.class,
                ValidationError.class,
                CustomBeanParamProvider.class
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
            .addClasses(
                Initializer.class
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

    private static final String FIRST_NAME = "test_firstname";
    private static final String LAST_NAME = "test_lastname";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";

    @Test
    @RunAsClient
    public void testUsers() throws MalformedURLException {

        LOG.log(Level.INFO, "base url @{0}", base);

        //get all users
        final WebTarget targetGetAll = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resGetAll = targetGetAll.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll.getStatus());
        UserDetail[] results = resGetAll.readEntity(UserDetail[].class);
        
        LOG.log(Level.INFO, "get all users @{0}", results);
        assertTrue(results != null);
        assertTrue(results.length == 2);

        //You have to close the response manually... issue# RESTEASY-1120
        //see https://issues.jboss.org/browse/RESTEASY-1120
        resGetAll.close();

        //create a new user
        UserForm newUserForm = Fixtures.newUserForm(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);
        final WebTarget targetUser = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resUser = targetUser.request().post(Entity.json(newUserForm));
        assertEquals(201, resUser.getStatus());
        String savedUserLocaiton = resUser.getHeaderString("Location");
        LOG.log(Level.INFO, "saved user location @{0}", savedUserLocaiton);

        resUser.close();

        //verify new created user in the findAll result list.
        final WebTarget targetGetAll2 = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resGetAll2 = targetGetAll2.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, resGetAll2.getStatus());
        UserDetail[] results2 = resGetAll2.readEntity(UserDetail[].class);
        assertTrue(results2 != null);
        assertTrue(results2.length == 3);

        resGetAll2.close();

        //get the created data
        final WebTarget targetGet = client.target(URI.create(new URL(savedUserLocaiton).toExternalForm()));
        Response responseGet = targetGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, responseGet.getStatus());
        LOG.log(Level.INFO, "get entity @{0}", responseGet);
        UserDetail result = responseGet.readEntity(UserDetail.class);
        assertNotNull(result.getId());
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());

        responseGet.close();

        //update user form
        UserForm updateUserForm = Fixtures.newUserForm(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);
        final WebTarget targetPut = client.target(URI.create(new URL(savedUserLocaiton).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(updateUserForm));

        assertEquals(204, responsePut.getStatus());

        responsePut.close();

        //verify updated result
        final WebTarget targetVerifyUpdatedGet = client.target(URI.create(new URL(savedUserLocaiton).toExternalForm()));
        final Response responseVerifyUpdatedGet = targetVerifyUpdatedGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(200, responseVerifyUpdatedGet.getStatus());
        LOG.log(Level.INFO, "verifyUpdateGet entity @{0}", responseVerifyUpdatedGet);
        UserDetail verifyUpdateResult = responseVerifyUpdatedGet.readEntity(UserDetail.class);
        assertNotNull(verifyUpdateResult.getId());
        assertEquals(FIRST_NAME + "updated", verifyUpdateResult.getFirstName());
        assertEquals(LAST_NAME + "updated", verifyUpdateResult.getLastName());

        responseVerifyUpdatedGet.close();

        //delete user
        final WebTarget targetDelete = client.target(URI.create(new URL(savedUserLocaiton).toExternalForm()));
        final Response responseDelete = targetDelete
            .request()
            .delete();

        assertEquals(204, responseDelete.getStatus());

        responseDelete.close();
    }

    @Test
    @RunAsClient
    public void testGetUserNotFound() throws MalformedURLException {

        //get the created data
        String location = "api/users/1000";
        final WebTarget targetGet = client.target(URI.create(new URL(base, location).toExternalForm()));
        Response responseGet = targetGet.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, responseGet.getStatus());
    }

    @Test
    @RunAsClient
    public void testUpdateUserNotFound() throws MalformedURLException {
        String location = "api/users/1000";

        UserForm updateUserForm = Fixtures.newUserForm(FIRST_NAME + "updated", LAST_NAME + "updated", USERNAME, PASSWORD);
        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .put(Entity.json(updateUserForm));

        assertEquals(404, responsePut.getStatus());
    }

    @Test
    @RunAsClient
    public void testDeleteUserNotFound() throws MalformedURLException {
        String location = "api/users/1000";

        final WebTarget targetPut = client.target(URI.create(new URL(base, location).toExternalForm()));

        final Response responsePut = targetPut
            .request()
            .delete();

        assertEquals(404, responsePut.getStatus());
    }

    @Test
    @RunAsClient
    public void testCreateUserFormIsInvalid() throws MalformedURLException {
        //create a new user
        UserForm newUserForm = new UserForm();
        final WebTarget targetUser = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resUser = targetUser.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newUserForm));

        LOG.info("response content string @\n" + resUser.readEntity(String.class));
        assertEquals(400, resUser.getStatus());

        resUser.close();
    }

    @Test
    @RunAsClient
    public void testCreateUserFormWhenUsernnameWasTaken() throws MalformedURLException {
        //create a new user
        UserForm newUserForm = Fixtures.newUserForm(FIRST_NAME, LAST_NAME, USERNAME + "test", PASSWORD);
        final WebTarget targetUser = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resUser = targetUser.request().post(Entity.json(newUserForm));
        assertEquals(201, resUser.getStatus());
        String savedUserLocaiton = resUser.getHeaderString("Location");
        LOG.log(Level.INFO, "saved user location @{0}", savedUserLocaiton);

        resUser.close();

        //create a new user
        UserForm newUserForm2 = Fixtures.newUserForm(FIRST_NAME, LAST_NAME, USERNAME + "test", PASSWORD);
        final WebTarget targetUser2 = client.target(URI.create(new URL(base, "api/users").toExternalForm()));
        final Response resUser2 = targetUser2.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newUserForm2));

        LOG.info("response content string @\n" + resUser2.readEntity(String.class));
        assertEquals(409, resUser2.getStatus());

        resUser.close();
    }

}
