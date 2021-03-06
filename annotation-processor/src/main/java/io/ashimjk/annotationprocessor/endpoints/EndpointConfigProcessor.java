package io.ashimjk.annotationprocessor.endpoints;

import com.google.auto.service.AutoService;
import io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.GetEndpointTypeMapper;
import io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.PatchEndpointTypeMapper;
import io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.PostEndpointTypeMapper;
import io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.RequestEndpointTypeMapper;

import javax.annotation.processing.*;
import javax.annotation.security.RolesAllowed;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

import static io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.DeleteEndpointTypeMapper;
import static io.ashimjk.annotationprocessor.endpoints.EndpointTypeMapper.PutEndpointTypeMapper;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("javax.annotation.security.RolesAllowed")
public class EndpointConfigProcessor extends AbstractProcessor {

    private final EndpointConfigs endpointConfigs;

    public EndpointConfigProcessor() {
        endpointConfigs = new EndpointConfigs();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RolesAllowed.class)) {
            String[] roles = annotatedElement.getAnnotation(RolesAllowed.class).value();

            EndpointBuilder.of(annotatedElement, endpointConfigs, roles)
                    .ifMapperAvailableAddToEndpoints(RequestEndpointTypeMapper::new)
                    .ifMapperAvailableAddToEndpoints(GetEndpointTypeMapper::new)
                    .ifMapperAvailableAddToEndpoints(PostEndpointTypeMapper::new)
                    .ifMapperAvailableAddToEndpoints(PutEndpointTypeMapper::new)
                    .ifMapperAvailableAddToEndpoints(PatchEndpointTypeMapper::new)
                    .ifMapperAvailableAddToEndpoints(DeleteEndpointTypeMapper::new);
        }

        endpointConfigs.ifNotEmpty(endpoints -> {
            try {
                EndpointConfigIO.writeToFile(endpoints, this.processingEnv.getFiler());
            } catch (IOException ex) {
                Messager messager = this.processingEnv.getMessager();
                messager.printMessage(ERROR, "Failed to write endpoints in the file");
                messager.printMessage(ERROR, String.format("Endpoints : [%s]", endpoints));
            } finally {
                endpoints.cleanup();
            }
        });

        return true;
    }

}
