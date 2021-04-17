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

}