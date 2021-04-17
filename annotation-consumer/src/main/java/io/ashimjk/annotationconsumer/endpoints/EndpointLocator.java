package io.ashimjk.annotationconsumer.endpoints;

import io.ashimjk.annotationprocessor.endpoints.EndpointIO;
import io.ashimjk.annotationprocessor.endpoints.Endpoints.RolesByMethodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.web.util.UriTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
public class EndpointLocator {

    private static final String API_BASE_PATH = "";

    private final Map<String, RolesByMethodType> staticEndpoints = new HashMap<>();
    private final List<VariableEndpoint> variableEndpoints = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        Map<String, RolesByMethodType> rolesByEndpoint = EndpointIO.readFromFile().getRolesByEndpoint();
        log.info("{} links has been read", rolesByEndpoint.size());

        for (Map.Entry<String, RolesByMethodType> entry : rolesByEndpoint.entrySet()) {

            UriTemplate uriTemplate = new UriTemplate(entry.getKey());

            if (uriTemplate.getVariableNames().isEmpty()) {
                staticEndpoints.put(entry.getKey(), entry.getValue());
            } else {
                variableEndpoints.add(VariableEndpoint.of(uriTemplate, entry.getValue()));
            }

        }
    }

    public List<String> findEndpointRoles(Link link) {
        final String relativeHref = normalizeHref(link.getHref());
        final String methodType = link.getType();

        return ofNullable(staticEndpoints.get(relativeHref))
                .map(r -> new ArrayList<>(r.getRoles(methodType)))
                .orElseGet(() ->
                        (ArrayList<String>) variableEndpoints
                                .stream()
                                .filter(ve -> ve.uriTemplate.matches(relativeHref))
                                .map(ve -> ve.rolesByMethodType.getRoles(methodType))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())
                );
    }

    private String normalizeHref(String href) {
        String relativeHref = href.substring(href.indexOf(API_BASE_PATH));
        relativeHref = relativeHref.contains("?") ? relativeHref.substring(0, relativeHref.indexOf("?")) : relativeHref;
        return relativeHref;
    }

    @RequiredArgsConstructor
    private static class VariableEndpoint {

        private final UriTemplate uriTemplate;
        private final RolesByMethodType rolesByMethodType;

        public static VariableEndpoint of(UriTemplate uriTemplate, RolesByMethodType rolesByMethodType) {
            return new VariableEndpoint(uriTemplate, rolesByMethodType);
        }

    }

}
