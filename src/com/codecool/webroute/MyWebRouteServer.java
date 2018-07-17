package com.codecool.webroute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class MyWebRouteServer {

    public static void main(String[] args) throws Exception {
        Class handler = Class.forName("com.codecool.webroute.MyHandler");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        for (Method method : handler.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                server.createContext(webRoute.value(), new MyHandler(webRoute.value()));
            }
        }

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Running");
    }
}



