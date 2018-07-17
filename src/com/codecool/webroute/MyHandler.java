package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static javax.imageio.ImageIO.read;

public class MyHandler implements HttpHandler {

    private String route;

    public MyHandler() {}

    public MyHandler(String route) { this.route = route; }

    @WebRoute("/test")
    void onTest(HttpExchange requestData) throws IOException {
        InputStream isTest = requestData.getRequestBody();
        read(isTest);
        String response = "Testing";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream osTest = requestData.getResponseBody();
        osTest.write(response.getBytes());
        osTest.close();
    }

    @WebRoute("/new-test-page")
    void onTestAgain(HttpExchange requestData) throws IOException {
        InputStream isTestAgain = requestData.getRequestBody();
        read(isTestAgain);
        String response = "Testing Once More";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream osTestAgain = requestData.getResponseBody();
        osTestAgain.write(response.getBytes());
        osTestAgain.close();
    }

    @Override
    public void handle(HttpExchange requestData) throws IOException {
        Class<MyHandler> handlerClass = MyHandler.class;
        for (Method method : handlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                if (webRoute.value().equals(route)) {
                    try {
                        method.invoke(handlerClass.newInstance(), requestData);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}