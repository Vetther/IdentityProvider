package pl.owolny.identityprovider.web.register;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private static final String REGISTER_VIEW = "register";

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDto") @Valid RegisterDto registerDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("email", "passwords.not.matching");
        }
        model.addAttribute("error", "testAttribute");
        return REGISTER_VIEW;
    }
}
