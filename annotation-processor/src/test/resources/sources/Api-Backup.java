import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

public class Api {

    @RolesAllowed("creator")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api")
    public String postMethod(@RequestBody String s) {
        return s;
    }

    @RolesAllowed("verifier")
    @PutMapping("/api")
    @ResponseStatus(HttpStatus.CREATED)
    public String putMethod(@RequestBody String s) {
        return s;
    }

    @RolesAllowed("viewer")
    @GetMapping("/api/{id}")
    public String getMethod(@PathVariable("id") String id) {
        return id;
    }
}

class Api2 {

    @RolesAllowed("creator")
    @PostMapping("/api2")
    public String api2PostMethod(@RequestBody String s) {
        return s;
    }

}