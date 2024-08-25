package pl.owolny.identityprovider.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    public static final String LOGIN_VIEW = "login";

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "success", required = false) String success) {
        return LOGIN_VIEW;
    }

}