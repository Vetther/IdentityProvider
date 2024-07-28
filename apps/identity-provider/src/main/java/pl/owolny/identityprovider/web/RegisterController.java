package pl.owolny.identityprovider.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.owolny.identityprovider.web.register.RegisterDto;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return ViewNames.REGISTER;
    }

    @PostMapping("/register-success")
    public String registerUser(@ModelAttribute("registerDto") @Valid RegisterDto registerDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return ViewNames.REGISTER;
        }
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("email", "passwords.not.matching");
        }
        model.addAttribute("error", "testAttribute");
        return ViewNames.REGISTER;
    }
}