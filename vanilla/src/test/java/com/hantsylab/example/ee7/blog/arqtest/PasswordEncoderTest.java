package com.hantsylab.example.ee7.blog.arqtest;


import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.hantsylab.example.ee7.blog.crypto.Crypto;
import com.hantsylab.example.ee7.blog.crypto.PasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.bcrypt.BCryptPasswordEncoder;
import com.hantsylab.example.ee7.blog.crypto.plain.PlainPasswordEncoder;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class PasswordEncoderTest {

    @Deployment(name = "test")
    public static Archive<?> createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            .addPackage(PlainPasswordEncoder.class.getPackage())
            .addPackage(BCryptPasswordEncoder.class.getPackage())
            .addPackage(PasswordEncoder.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        // System.out.println(archive.toString(true));
        return archive;
    }

    @Inject
    @Crypto(Crypto.Type.PLAIN)
    PasswordEncoder plain;

    @Inject
    @Crypto(Crypto.Type.BCRYPT)
    PasswordEncoder bcrypt;

    @Test
    public void testPlainInjectWorks() {
        assertTrue(plain instanceof PlainPasswordEncoder);
    }

    @Test
    public void testBCryptInjectWorks() {
        assertTrue(bcrypt instanceof BCryptPasswordEncoder);
    }

}
