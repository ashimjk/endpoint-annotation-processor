package io.ashimjk.annotationprocessor.endpoints;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointBuilder {

    private final Element annotatedElement;
    private final Endpoints endpoints;
    private final String[] roles;

    public static EndpointBuilder of(Element annotatedElement, Endpoints endpoints, String[] roles) {
        return new EndpointBuilder(annotatedElement, endpoints, roles);
    }

    public <T extends Annotation> EndpointBuilder ifMapperAvailableAddEndpoint(Supplier<EndpointTypeMapper<T>> mapperSupplier) {
        EndpointTypeMapper<T> mapper = mapperSupplier.get();

        T annotation = annotatedElement.getAnnotation(mapper.getType());

        ofNullable(annotation)
                .ifPresent((T mapperAnnotation) -> {
                    String[] paths = mapper.getPaths().apply(mapperAnnotation);
                    String methodType = mapper.getMethodType().apply(mapperAnnotation);

                    Arrays.stream(paths).forEach(path -> {
                        Endpoints.RolesByMethodType rolesByMethodType = endpoints.getRoles(path);

                        Set<String> existingRoles = rolesByMethodType.getRoles(methodType);

                        Set<String> mergedRoles = new HashSet<>(existingRoles);
                        mergedRoles.addAll(Arrays.asList(this.roles));

                        endpoints.addEndpoint(path, rolesByMethodType.addRolesByMethodType(methodType, mergedRoles));
                    });
                });

        return this;
    }

}
