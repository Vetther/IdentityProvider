package pl.owolny.identityprovider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@Slf4j
public class TestController {

    private final RestClient restClient;

    public TestController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request,
                       HttpServletResponse response) {
        return null;
    }

    @GetMapping("/test3")
    public String test3() {
        return restClient.get()
                .uri("http://localhost:8080/test")
                .retrieve()
                .body(String.class);
    }
}
