package de.akquinet.innovation.osgi.impl;

import de.akquinet.innovation.osgi.HelloService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloServiceImplTest {

    @Test
    public void testHello() {
        HelloService hello = new HelloServiceImpl();
        assertEquals("hello", hello.sayHello());
    }

    @Test
    public void testHelloWithUser() {
        HelloService hello = new HelloServiceImpl();
        assertEquals("hello John", hello.sayHello("John"));
    }

}
