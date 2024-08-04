package pl.owolny.identityprovider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import pl.owolny.identityprovider.user.UserClient;

@RestController
@Slf4j
public class TestController {

    private final UserClient userClient;
    private final WebClient webClient;

    public TestController(UserClient userClient, WebClient webClient) {
        this.userClient = userClient;
        this.webClient = webClient;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test3")
    public String test3() {
        return webClient.get()
                .uri("http://localhost:8080/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/test2")
    public String test2() {
        return userClient.test();
    }
}
