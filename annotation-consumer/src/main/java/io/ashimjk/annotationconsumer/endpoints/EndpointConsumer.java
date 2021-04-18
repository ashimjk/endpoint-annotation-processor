package io.ashimjk.annotationconsumer.endpoints;

import io.ashimjk.annotationprocessor.endpoints.EndpointConfigIO;
import io.ashimjk.annotationprocessor.endpoints.EndpointConfigs;

import java.io.IOException;

public class EndpointConsumer {

    public static void main(String[] args) throws IOException {
        EndpointConfigs endpointConfigs = EndpointConfigIO.readFromFile();
        System.out.println(endpointConfigs);
    }

}
