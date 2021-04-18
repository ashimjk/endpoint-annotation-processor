package io.ashimjk.annotationprocessor.endpoints;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointBuilder {

    private final Element annotatedElement;
    private final EndpointConfigs endpointConfigs;
    private final String[] roles;

    public static EndpointBuilder of(Element annotatedElement, EndpointConfigs endpointConfigs, String[] roles) {
        return new EndpointBuilder(annotatedElement, endpointConfigs, roles);
    }

    public <T extends Annotation> EndpointBuilder ifMapperAvailableAddToEndpoints(Supplier<EndpointTypeMapper<T>> mapperSupplier) {
        EndpointTypeMapper<T> mapper = mapperSupplier.get();

        String basePath = getBasePath();
        T annotation = findAnnotation(annotatedElement, mapper.getType());

        ofNullable(annotation)
                .ifPresent((T mapperAnnotation) -> {
                    String[] paths = mapper.getPaths().apply(mapperAnnotation);
                    String methodType = mapper.getMethodType().apply(mapperAnnotation);

                    Arrays.stream(paths).forEach(path -> {
                        String fullPath = basePath + path;
                        EndpointConfigs.RolesByMethodType rolesByMethodType = endpointConfigs.getRoles(fullPath);

                        Set<String> existingRoles = rolesByMethodType.getRoles(methodType);

                        Set<String> mergedRoles = new HashSet<>(existingRoles);
                        mergedRoles.addAll(Arrays.asList(this.roles));

                        endpointConfigs.addEndpoint(fullPath, rolesByMethodType.addRolesByMethodType(methodType, mergedRoles));
                    });
                });

        return this;
    }

    @SuppressWarnings("incompatible")
    private <T extends Annotation> T findAnnotation(Element annotatedElement, Class<T> annotationType) {

        return ofNullable(annotatedElement.getAnnotation(Override.class))
                .map(e -> ((TypeElement) annotatedElement.getEnclosingElement()).getInterfaces())
                .stream()
                .flatMap(Collection::stream)
                .map(e -> ((DeclaredType) e).asElement())
                .flatMap(element -> element.getEnclosedElements().stream())
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .filter(e -> e.getSimpleName().contentEquals(annotatedElement.getSimpleName()))
                .findFirst()
                .map(e -> e.getAnnotation(annotationType))
                .orElseGet(() -> annotatedElement.getAnnotation(annotationType));
    }

    private String getBasePath() {
        return ofNullable(annotatedElement.getEnclosingElement().getAnnotation(RequestMapping.class))
                .map(RequestMapping::value)
                .stream()
                .map(r -> String.join("", r))
                .findFirst()
                .orElse("");
    }

}
