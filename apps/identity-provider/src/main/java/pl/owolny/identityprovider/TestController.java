package pl.owolny.identityprovider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.owolny.identityprovider.client.UserClient;

@RestController
@Slf4j
public class TestController {

    private final UserClient userClient;

    public TestController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test3")
    public String test3() {
        return "test3";
    }

    @GetMapping("/test2")
    public String test2() {
        return userClient.test();
    }
}
