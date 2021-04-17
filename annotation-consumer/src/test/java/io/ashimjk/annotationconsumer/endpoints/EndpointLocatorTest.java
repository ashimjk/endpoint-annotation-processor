package io.ashimjk.annotationconsumer.endpoints;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EndpointLocatorTest {

    private EndpointLocator endpointLocator;

    @BeforeEach
    @SneakyThrows
    void setup() {
        this.endpointLocator = new EndpointLocator();
        this.endpointLocator.init();
    }

    @Test
    @DisplayName("Test roles using static url")
    void testRolesUsingStaticUrl() {
        Link link = Link.of("/api").withType("GET");

        List<String> endpointRoles = endpointLocator.findEndpointRoles(link);

        assertThat(endpointRoles).containsExactly("request");
    }

    @Test
    @DisplayName("Test roles using variable url")
    void testRolesUsingVariableUrl() {
        Link link = Link.of("/api/1").withType("GET");

        List<String> endpointRoles = endpointLocator.findEndpointRoles(link);

        assertThat(endpointRoles).containsExactly("get");
    }

    @Test
    @DisplayName("Test empty roles using variable url")
    void testEmptyRolesUsingVariableUrl() {
        Link link = Link.of("/api/1/test").withType("GET");

        List<String> endpointRoles = endpointLocator.findEndpointRoles(link);

        assertThat(endpointRoles).isEmpty();
    }

}