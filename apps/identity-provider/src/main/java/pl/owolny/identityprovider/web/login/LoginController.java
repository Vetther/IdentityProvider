package pl.owolny.identityprovider.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String LOGIN_VIEW = "login";

    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW;
    }

}