package io.ashimjk.annotationconsumer.endpoints;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

@RequestMapping("/parent")
public class ParentApi {

    @RolesAllowed("request")
    @RequestMapping("/api")
    public String requestMethod(@PathVariable("id") String id) {
        return id;
    }

}
