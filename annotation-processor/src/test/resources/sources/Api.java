import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.RolesAllowed;

public class Api {

    @RolesAllowed("viewer")
    @GetMapping("/api/{id}")
    public String getMethod(@PathVariable("id") String id) {
        return id;
    }

}