package io.ashimjk.annotationprocessor.endpoints;

import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

public class AllHttpMethods {

    @RolesAllowed("request")
    @RequestMapping("/api")
    public String requestMethod(@PathVariable("id") String id) {
        return id;
    }

    @RolesAllowed("get")
    @GetMapping("/api/{id}")
    public String getMethod(@PathVariable("id") String id) {
        return id;
    }

    @RolesAllowed("post")
    @PostMapping("/api")
    public void postMethod(@RequestBody Map<String, Object> requestBody) {
    }

    @RolesAllowed("put")
    @PutMapping("/api/{id}")
    public void putMethod(@PathVariable("id") String id, @RequestBody Map<String, Object> requestBody) {
    }

    @RolesAllowed("patch")
    @PatchMapping("/api/{id}")
    public void patchMethod(@PathVariable("id") String id, @RequestBody Map<String, Object> requestBody) {
    }

    @RolesAllowed("delete")
    @DeleteMapping("/api/{id}")
    public String deleteMethod(@PathVariable("id") String id) {
        return id;
    }

}