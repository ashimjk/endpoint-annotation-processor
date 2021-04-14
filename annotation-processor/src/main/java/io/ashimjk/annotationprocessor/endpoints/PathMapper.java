package io.ashimjk.annotationprocessor.endpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public abstract class PathMapper<T> {

    private final String methodType;
    private final Class<T> type;
    private final Function<T, String[]> paths;

    // public static class RequestPathMapper extends PathMapper<RequestMapping> {
    //
    //     RequestPathMapper() {
    //         super(test(t -> ), RequestMapping.class, RequestMapping::value);
    //     }
    //
    //     static  Function<RequestMapping, String> methodType(Function<RequestMapping, RequestMethod[]> methodTypes) {
    //
    //         methodTypes.apply()
    //     }
    //
    // }

    public static class GetPathMapper extends PathMapper<GetMapping> {

        GetPathMapper() {
            super("GET", GetMapping.class, GetMapping::value);
        }

    }

    public static class PostPathMapper extends PathMapper<PostMapping> {

        PostPathMapper() {
            super("POST", PostMapping.class, PostMapping::value);
        }

    }

    public static class PutPathMapper extends PathMapper<PutMapping> {

        PutPathMapper() {
            super("PUT", PutMapping.class, PutMapping::value);
        }

    }

    public static class DeletePathMapper extends PathMapper<DeleteMapping> {

        DeletePathMapper() {
            super("DELETE", DeleteMapping.class, DeleteMapping::value);
        }

    }

    public static class PatchPathMapper extends PathMapper<PatchMapping> {

        PatchPathMapper() {
            super("PATCH", PatchMapping.class, PatchMapping::value);
        }

    }

}
