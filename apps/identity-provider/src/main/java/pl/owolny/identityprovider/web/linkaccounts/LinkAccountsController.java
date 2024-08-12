package pl.owolny.identityprovider.web.linkaccounts;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityAccountService;
import pl.owolny.identityprovider.federation.FederatedAuth;

import java.util.UUID;

@Controller
public class LinkAccountsController {

    public static final String LINK_ACCOUNTS = "link-accounts";
    private final FederatedIdentityAccountService federatedIdentityAccountService;

    public LinkAccountsController(FederatedIdentityAccountService federatedIdentityAccountService) {
        this.federatedIdentityAccountService = federatedIdentityAccountService;
    }

    @GetMapping("/link-accounts")
    public String showLinkAccountsPage(@SessionAttribute String linkingUserEmail, @SessionAttribute FederatedAuth federatedAuth, Model model) {
        model.addAttribute("email", linkingUserEmail);
        model.addAttribute("provider", federatedAuth.getFederatedProvider().name());
        return LINK_ACCOUNTS;
    }

    @PostMapping("/link-accounts")
    public String handleLinkAccounts(@RequestParam String action) {
        if ("link".equals(action)) {
//            userService.sendLinkAccountEmail(email, provider);
            return "redirect:/link-accounts/confirm";
        } else {
            // Użytkownik odmówił połączenia kont
            return "redirect:/login";
        }
    }

    @GetMapping("/link-accounts/confirm")
    public String ConfirmLinkAccounts(HttpServletRequest request, @SessionAttribute UUID linkingUserId, @SessionAttribute FederatedAuth federatedAuth) {

        request.getSession().removeAttribute("linkingUserId");
        request.getSession().removeAttribute("linkingUserEmail");
        request.getSession().removeAttribute("federatedAuth");

        this.federatedIdentityAccountService.create(
                linkingUserId,
                federatedAuth.getFederatedIdentityId(),
                federatedAuth.getFederatedProvider(),
                federatedAuth.getEmail(),
                federatedAuth.getUsername());

        return "redirect:/login";
    }

}