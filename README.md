OSGi Helper Examples
====================

This projects demonstrates the simplification brought to OSGi integration tests using OW2 Chameleon OSGi Helpers. Thanks
to this library, tests focus on the functional logic and is not being drown by OSGi code.

Building the project
--------------------

* Check out the project
* Run _mvn clean install_ from the root

Did you say simpler ?
---------------------

An OSGi integration tests may look like:

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

To just test the availability of a service and the behavior, OSGi requires a lof of code to wait and get the service.
Using the OSGi Helper, the test focuses only on the functional behavior:


    @Test
    public void testWithHelpers() throws Exception {
        HelloService service = helper.waitForService(HelloService.class, null, 1000);
        Assert.assertEquals("hello", service.sayHello());
        Assert.assertEquals("hello John", service.sayHello("John"));
    }

The OSGi Helper Library also provides an assertion class to check OSGi-aspect:

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

Getting the OSGi Helpers
------------------------

The OSGi Helper library is developped by the [OW2 Chameleon project](http://chameleon.ow2.org). The bundle is available
from the Maven:

    <dependency>
        <groupId>org.ow2.chameleon.testing</groupId>
        <artifactId>osgi-helpers</artifactId>
        <version>0.4.0</version>
    </dependency>

If you have any question, just send a mail to the [OW2 Chameleon mailing lists](http://wiki.chameleon.ow2.org/xwiki/bin/view/Main/MailingLists).



