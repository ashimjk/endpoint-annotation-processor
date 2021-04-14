package io.ashimjk.annotationprocessor.endpoints;

import com.google.auto.service.AutoService;
import io.ashimjk.annotationprocessor.endpoints.Endpoint.RolesByMethodType;
import io.ashimjk.annotationprocessor.endpoints.PathMapper.GetPathMapper;
import io.ashimjk.annotationprocessor.endpoints.PathMapper.PostPathMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.processing.*;
import javax.annotation.security.RolesAllowed;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("javax.annotation.security.RolesAllowed")
public class EndpointProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Endpoint endpoint = new Endpoint();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RolesAllowed.class)) {
            String[] roles = annotatedElement.getAnnotation(RolesAllowed.class).value();

            // RequestMapping annotation = annotatedElement.getAnnotation(RequestMapping.class);
            // String methodType = Arrays.stream(annotation.method()).findFirst().map(RequestMethod::name).orElse(Strings.EMPTY);

            Function<RequestMapping, String> mappingFunction = requestMapping ->
                    Arrays.stream(requestMapping.method()).findFirst().map(RequestMethod::name).orElse("");

            addEndpointIfMapperAvailable(GetPathMapper::new, annotatedElement, endpoint, roles);
            addEndpointIfMapperAvailable(PostPathMapper::new, annotatedElement, endpoint, roles);
        }

        System.out.println(endpoint);

        return true;
    }

    private <T extends Annotation> void addEndpointIfMapperAvailable(
            Supplier<PathMapper<T>> mapperSupplier, Element annotatedElement, Endpoint endpoint, String[] roles) {

        PathMapper<T> mapper = mapperSupplier.get();

        T annotation = annotatedElement.getAnnotation(mapper.getType());

        ofNullable(annotation)
                .ifPresent(mapperAnnotation -> {
                    String[] paths = mapper.getPaths().apply(mapperAnnotation);
                    Arrays.stream(paths).forEach(path -> endpoint.addEndpoint(path, RolesByMethodType.of(mapper.getMethodType(), roles)));
                });

    }

}
