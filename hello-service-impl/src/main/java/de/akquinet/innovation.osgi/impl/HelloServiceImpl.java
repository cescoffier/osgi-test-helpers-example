package de.akquinet.innovation.osgi.impl;

import de.akquinet.innovation.osgi.HelloService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

@Component
@Provides
@Instantiate
public class HelloServiceImpl implements HelloService {

    public String sayHello() {
        return "hello";
    }

    public String sayHello(String name) {
        return "hello " + name;
    }
}
