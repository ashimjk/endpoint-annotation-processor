package io.ashimjk.annotationprocessor.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

import static java.util.Optional.ofNullable;

@ToString
public class Endpoint {

    private final Map<String, RolesByMethodType> endpoints = new HashMap<>();

    public void addEndpoint(String endpoint, RolesByMethodType rolesByMethodType) {
        endpoints.put(endpoint, rolesByMethodType);
    }

    public RolesByMethodType getRoles(String endpoint) {
        return ofNullable(this.endpoints.get(endpoint))
                .orElseGet(RolesByMethodType::new);
    }

    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RolesByMethodType {

        private final Map<String, List<String>> rolesByMethodType = new HashMap<>();

        public static RolesByMethodType of(String methodType, String[] roles) {
            RolesByMethodType rolesByMethodType = new RolesByMethodType();
            rolesByMethodType.addRolesByMethodType(methodType, Arrays.asList(roles));
            return rolesByMethodType;
        }

        public void addRolesByMethodType(String methodType, List<String> roles) {
            rolesByMethodType.put(methodType, roles);
        }

        public List<String> getRoles(String methodType) {
            return ofNullable(this.rolesByMethodType.get(methodType))
                    .orElseGet(Collections::emptyList);
        }

    }

}
