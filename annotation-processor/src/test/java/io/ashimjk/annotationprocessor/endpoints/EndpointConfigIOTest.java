package io.ashimjk.annotationprocessor.endpoints;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

class EndpointConfigIOTest {

    @Test
    @SneakyThrows
    @SuppressWarnings("nullness")
    @DisplayName("Read endpoint processor output file")
    void readEndpointProcessorOutputFile() {
        EndpointConfigs endpointConfigs = EndpointConfigIO.readFromFile();

        Map<String, EndpointConfigs.RolesByMethodType> rolesByEndpoint = endpointConfigs.getRolesByEndpoint();
        assertThat(rolesByEndpoint).containsKeys("/api");

        Map<String, Set<String>> rolesByMethodType1 = rolesByEndpoint.get("/api").getRolesByMethodType();
        assertThat(rolesByMethodType1).containsEntry("GET", singleton("request"));
        assertThat(rolesByMethodType1).containsEntry("POST", singleton("post"));
    }

}