package io.ashimjk.annotationconsumer.endpoints;

import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

interface Api {

    @RequestMapping("/inherited")
    String requestMethod();

    @GetMapping("/inherited/{id}")
    String getMethod(@PathVariable("id") String id);

    @PostMapping("/inherited")
    void postMethod(@RequestBody Map<String, Object> requestBody);

    @PutMapping("/inherited/{id}")
    String putMethod(@PathVariable("id") String id);

}

public class InheritedMapping implements Api {

    @Override
    @RolesAllowed("inherited-request")
    public String requestMethod() {
        return "";
    }

    @Override
    @RolesAllowed("inherited-get")
    public String getMethod(String id) {
        return id;
    }

    @Override
    @RolesAllowed("inherited-post")
    public void postMethod(Map<String, Object> requestBody) {
    }

    @Override
    @RolesAllowed("inherited-put")
    public String putMethod(String id) {
        return id;
    }

}