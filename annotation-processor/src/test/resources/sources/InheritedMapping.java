package sources;

import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

interface Api {

    @RequestMapping("/api")
    String requestMethod();

    @GetMapping("/api/{id}")
    String getMethod(@PathVariable("id") String id);

    @PostMapping("/api")
    void postMethod(@RequestBody Map<String, Object> requestBody);

}

public class InheritedMapping implements Api {

    @Override
    @RolesAllowed("request")
    public String requestMethod() {
        return "";
    }

    @Override
    @RolesAllowed("get")
    public String getMethod(String id) {
        return id;
    }

    @Override
    @RolesAllowed("post")
    public void postMethod(Map<String, Object> requestBody) {
    }

}