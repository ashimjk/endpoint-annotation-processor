package io.ashimjk.annotationprocessor.endpoints;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

@Getter
@ToString
@NoArgsConstructor
public class EndpointConfigs {

    private Map<String, RolesByMethodType> rolesByEndpoint = new HashMap<>();

    public EndpointConfigs(Map<String, RolesByMethodType> rolesByEndpoint) {
        this.rolesByEndpoint = rolesByEndpoint;
    }

    public RolesByMethodType getRoles(String endpoint) {
        return ofNullable(this.getRolesByEndpoint().get(endpoint))
                .orElseGet(RolesByMethodType::new);
    }

    public void addEndpoint(String endpoint, RolesByMethodType rolesByMethodType) {
        rolesByEndpoint.put(endpoint, rolesByMethodType);
    }

    public void ifNotEmpty(Consumer<EndpointConfigs> consumer) {
        if (!this.rolesByEndpoint.isEmpty()) {
            consumer.accept(this);
        }
    }

    public void cleanup() {
        this.rolesByEndpoint.clear();
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RolesByMethodType {

        private final Map<String, Set<String>> rolesByMethodType = new HashMap<>();

        public Set<String> getRoles(String methodType) {
            return ofNullable(this.getRolesByMethodType().get(methodType))
                    .orElseGet(Collections::emptySet);
        }

        public RolesByMethodType addRolesByMethodType(String methodType, Set<String> roles) {
            rolesByMethodType.put(methodType, roles);
            return this;
        }

    }

}
