package io.ashimjk.annotationprocessor.endpoints;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import io.ashimjk.annotationprocessor.endpoints.Endpoints.RolesByMethodType;
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
class EndpointProcessorTest {

    private @MonotonicNonNull Endpoints endpoints;
    private @MonotonicNonNull EndpointProcessor endpointProcessor;

    @BeforeEach
    void setup() {
        this.endpoints = spy(Endpoints.class);
        this.endpointProcessor = new EndpointProcessor();
    }

    @Test
    @DisplayName("Run Endpoint processor without error")
    void canRunEndpointProcessor_withoutCompilationError() {
        Truth.assert_()
                .about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("sources/AllHttpMethods.java"))
                .processedWith(endpointProcessor)
                .compilesWithoutError();
    }

    @Test
    @DisplayName("Read @RolesAllowed in All Type of Mapping")
    void canReadRolesAllowedAnnotation_OnAllTypeOfMapping() {
        doNothing().when(this.endpoints).ifNotEmpty(any(Consumer.class));
        ReflectionTestUtils.setField(this.endpointProcessor, "endpoints", this.endpoints);

        canRunEndpointProcessor_withoutCompilationError();

        Map<String, RolesByMethodType> rolesByEndpoint = this.endpoints.getRolesByEndpoint();
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

}