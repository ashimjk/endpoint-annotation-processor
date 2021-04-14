package io.ashimjk.annotationprocessor.endpoints;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndpointProcessorTest {

    private final EndpointProcessor endpointProcessor = new EndpointProcessor();

    @Test
    void testEndpointProcessor() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("sources/Api.java"))
                .processedWith(endpointProcessor)
                .compilesWithoutError();
    }

}