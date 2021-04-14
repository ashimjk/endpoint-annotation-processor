package io.ashimjk.annotationprocessor.endpoints;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

@Getter
@ToString
@NoArgsConstructor
public class Endpoints {

    private Map<String, RolesByMethodType> rolesByEndpoint = new HashMap<>();

    public Endpoints(Map<String, RolesByMethodType> rolesByEndpoint) {
        this.rolesByEndpoint = rolesByEndpoint;
    }

    public RolesByMethodType getRoles(String endpoint) {
        return ofNullable(this.rolesByEndpoint.get(endpoint))
                .orElseGet(RolesByMethodType::new);
    }

    public void addEndpoint(String endpoint, RolesByMethodType rolesByMethodType) {
        rolesByEndpoint.put(endpoint, rolesByMethodType);
    }

    public void ifNotEmpty(Consumer<Endpoints> consumer) {
        if (!this.rolesByEndpoint.isEmpty()) {
            consumer.accept(this);
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RolesByMethodType {

        private final Map<String, Set<String>> rolesByMethodType = new HashMap<>();

        public Set<String> getRoles(String methodType) {
            return ofNullable(this.rolesByMethodType.get(methodType))
                    .orElseGet(Collections::emptySet);
        }

        public RolesByMethodType addRolesByMethodType(String methodType, Set<String> roles) {
            rolesByMethodType.put(methodType, roles);
            return this;
        }

    }

}
