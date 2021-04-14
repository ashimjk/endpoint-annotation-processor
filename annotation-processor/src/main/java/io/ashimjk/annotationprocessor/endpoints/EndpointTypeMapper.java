package io.ashimjk.annotationprocessor.endpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public abstract class EndpointTypeMapper<T> {

    private final Function<T, String> methodType;
    private final Class<T> type;
    private final Function<T, String[]> paths;

    public EndpointTypeMapper(String methodType, Class<T> type, Function<T, String[]> paths) {
        this.methodType = t -> methodType;
        this.type = type;
        this.paths = paths;
    }

    public static class RequestEndpointTypeMapper extends EndpointTypeMapper<RequestMapping> {

        RequestEndpointTypeMapper() {
            super(RequestEndpointTypeMapper::methodType, RequestMapping.class, RequestMapping::value);
        }

        private static String methodType(RequestMapping requestMapping) {
            return Arrays.stream(requestMapping.method()).findFirst().map(RequestMethod::name).orElse("GET");
        }

    }

    public static class GetEndpointTypeMapper extends EndpointTypeMapper<GetMapping> {

        GetEndpointTypeMapper() {
            super("GET", GetMapping.class, GetMapping::value);
        }

    }

    public static class PostEndpointTypeMapper extends EndpointTypeMapper<PostMapping> {

        PostEndpointTypeMapper() {
            super("POST", PostMapping.class, PostMapping::value);
        }

    }

    public static class PutEndpointTypeMapper extends EndpointTypeMapper<PutMapping> {

        PutEndpointTypeMapper() {
            super("PUT", PutMapping.class, PutMapping::value);
        }

    }

    public static class PatchEndpointTypeMapper extends EndpointTypeMapper<PatchMapping> {

        PatchEndpointTypeMapper() {
            super("PATCH", PatchMapping.class, PatchMapping::value);
        }

    }

    public static class DeleteEndpointTypeMapper extends EndpointTypeMapper<DeleteMapping> {

        DeleteEndpointTypeMapper() {
            super("DELETE", DeleteMapping.class, DeleteMapping::value);
        }

    }

}
