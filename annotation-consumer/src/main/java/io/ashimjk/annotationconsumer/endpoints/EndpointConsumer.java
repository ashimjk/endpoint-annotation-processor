package io.ashimjk.annotationconsumer.endpoints;

import io.ashimjk.annotationprocessor.endpoints.EndpointIO;
import io.ashimjk.annotationprocessor.endpoints.Endpoints;

import java.io.IOException;

public class EndpointConsumer {

    public static void main(String[] args) throws IOException {
        Endpoints endpoints = EndpointIO.readFromFile();
        System.out.println(endpoints);
    }

}
