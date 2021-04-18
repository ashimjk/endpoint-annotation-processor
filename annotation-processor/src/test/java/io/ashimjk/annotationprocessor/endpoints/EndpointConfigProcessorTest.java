package io.ashimjk.annotationprocessor.endpoints;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import io.ashimjk.annotationprocessor.endpoints.EndpointConfigs.RolesByMethodType;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@SuppressWarnings({"unchecked", "nullness"})
class EndpointConfigProcessorTest {

    private @MonotonicNonNull EndpointConfigs endpointConfigs;
    private @MonotonicNonNull EndpointConfigProcessor endpointConfigProcessor;

    @BeforeEach
    void setup() {
        this.endpointConfigs = spy(EndpointConfigs.class);
        this.endpointConfigProcessor = new EndpointConfigProcessor();
    }

    @Test
    @DisplayName("Run Endpoint processor without error")
    void canRunEndpointProcessor_withoutCompilationError() {
        runEndpointProcessor("sources/AllHttpMethods.java");
    }

    @Test
    @DisplayName("Read @RolesAllowed in All Type of Mapping")
    void canReadRolesAllowedAnnotation_OnAllTypeOfMapping() {
        mockEndpoints();

        runEndpointProcessor("sources/AllHttpMethods.java");

        Map<String, RolesByMethodType> rolesByEndpoint = this.endpointConfigs.getRolesByEndpoint();
        assertThat(rolesByEndpoint).containsKeys("/api", "/api/{id}");

        Map<String, Set<String>> rolesByMethodType1 = rolesByEndpoint.get("/api").getRolesByMethodType();
        assertThat(rolesByMethodType1).containsEntry("GET", singleton("request"));
        assertThat(rolesByMethodType1).containsEntry("POST", singleton("post"));

        Map<String, Set<String>> rolesByMethodType2 = rolesByEndpoint.get("/api/{id}").getRolesByMethodType();
        assertThat(rolesByMethodType2).containsEntry("GET", singleton("get"));
        assertThat(rolesByMethodType2).containsEntry("PUT", singleton("put"));
        assertThat(rolesByMethodType2).containsEntry("PATCH", singleton("patch"));
        assertThat(rolesByMethodType2).containsEntry("DELETE", singleton("delete"));
    }

    @Test
    @DisplayName("Read @RolesAllowed in Inherited Mapping")
    void canReadRolesAllowedAnnotation_OnInheritedMapping() {
        mockEndpoints();

        runEndpointProcessor("sources/InheritedMapping.java");

        Map<String, RolesByMethodType> rolesByEndpoint = this.endpointConfigs.getRolesByEndpoint();
        assertThat(rolesByEndpoint).containsKeys("/api", "/api/{id}");

        Map<String, Set<String>> rolesByMethodType1 = rolesByEndpoint.get("/api").getRolesByMethodType();
        assertThat(rolesByMethodType1).containsEntry("GET", singleton("request"));
        assertThat(rolesByMethodType1).containsEntry("POST", singleton("post"));

        Map<String, Set<String>> rolesByMethodType2 = rolesByEndpoint.get("/api/{id}").getRolesByMethodType();
        assertThat(rolesByMethodType2).containsEntry("GET", singleton("get"));
    }

    private void runEndpointProcessor(String resourceName) {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(resourceName))
                .processedWith(endpointConfigProcessor)
                .compilesWithoutError();
    }

    private void mockEndpoints() {
        doNothing().when(this.endpointConfigs).ifNotEmpty(any(Consumer.class));
        ReflectionTestUtils.setField(this.endpointConfigProcessor, "endpoints", this.endpointConfigs);
    }

}