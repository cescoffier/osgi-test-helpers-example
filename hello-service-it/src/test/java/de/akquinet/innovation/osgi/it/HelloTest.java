package de.akquinet.innovation.osgi.it;

import de.akquinet.innovation.osgi.HelloService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.ow2.chameleon.testing.helpers.OSGiAssert;
import org.ow2.chameleon.testing.helpers.OSGiHelper;


import java.io.IOException;

import static org.ops4j.pax.exam.CoreOptions.*;

@RunWith( JUnit4TestRunner.class )
public class HelloTest {

    @Inject
    private BundleContext context;
    private OSGiHelper helper;

    @Configuration
    public Option[] config()
    {
        return options(
            mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.ipojo").version("1.8.0"),

            mavenBundle().groupId("de.akquinet.innovation.osgi").artifactId("hello-service").version("1.0-SNAPSHOT") ,
            mavenBundle().groupId("de.akquinet.innovation.osgi").artifactId("hello-service-impl").version("1.0-SNAPSHOT"),

            mavenBundle().groupId("org.ow2.chameleon.testing").artifactId("osgi-helpers").version("0.3.0-SNAPSHOT")
        );
    }

    @Before
    public void setUp() {
        helper = new OSGiHelper(context);
    }

    @After
    public void tearDown() {
        helper.dispose();
    }

    @Test
    public void testWithoutHelpers() throws Exception {
        HelloService service = null;
        ServiceReference ref = null;
        for (int i = 0; service == null  && i < 10; i++) {
            ref = context.getServiceReference(HelloService.class.getName());
            if (ref == null) {
                // Wait until the service is there...
                Thread.sleep(10);
            } else {
                service = (HelloService) context.getService(ref);
            }
        }

        Assert.assertNotNull(service);

        // We have the service object, run the test
        Assert.assertEquals("hello", service.sayHello());
        Assert.assertEquals("hello John", service.sayHello("John"));

        context.ungetService(ref);
    }

    @Test
    public void testWithHelpers() throws Exception {
        HelloService service = helper.waitForService(HelloService.class, null, 1000);
        Assert.assertEquals("hello", service.sayHello());
        Assert.assertEquals("hello John", service.sayHello("John"));
    }

    @Test
    public void testWithAssertions() throws Exception {
        OSGiAssert assertions = new OSGiAssert(context);

        assertions.assertServiceAvailable(HelloService.class, 1000);

        assertions.assertBundlePresent("hello-service-impl");
        Bundle bundle = helper.getBundle("hello-service-impl");
        bundle.stop();
        assertions.assertBundleState("hello-service-impl", Bundle.RESOLVED);

        assertions.assertServiceUnavailable(HelloService.class);

        bundle.start();
        assertions.assertBundleState("hello-service-impl", Bundle.ACTIVE);
        assertions.assertServiceAvailable(HelloService.class, 1000);

    }
}
