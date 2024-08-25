package pl.owolny.identityprovider.web.linkaccounts;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.owolny.identityprovider.domain.federatedidentity.FederatedIdentityAccountService;
import pl.owolny.identityprovider.domain.otp.OTP;
import pl.owolny.identityprovider.domain.otp.OTPService;
import pl.owolny.identityprovider.federation.FederatedAuth;
import pl.owolny.identityprovider.mail.MailService;

import java.util.UUID;

import static pl.owolny.identityprovider.web.login.LoginController.LOGIN_VIEW;

@Controller
public class LinkAccountsController {

    public static final String LINK_ACCOUNTS_VIEW = "link-accounts";
    private final FederatedIdentityAccountService federatedIdentityAccountService;
    private final MailService mailService;
    private final OTPService otpService;

    public LinkAccountsController(FederatedIdentityAccountService federatedIdentityAccountService,
                                  MailService mailService,
                                  OTPService otpService) {
        this.federatedIdentityAccountService = federatedIdentityAccountService;
        this.mailService = mailService;
        this.otpService = otpService;
    }

    @GetMapping("/link-accounts")
    public String showLinkAccountsPage(
            @SessionAttribute String userEmail,
            @SessionAttribute String userName,
            @SessionAttribute FederatedAuth federatedAuth,
            Model model) {
        model.addAttribute("email", userEmail);
        model.addAttribute("username", userName);
        model.addAttribute("provider", federatedAuth.getFederatedProvider().name());
        return LINK_ACCOUNTS_VIEW;
    }

    @PostMapping("/link-accounts")
    public String handleLinkAccounts(
            HttpServletRequest request,
            @SessionAttribute String userEmail,
            @SessionAttribute String userName,
            @RequestParam String action) {
        if (!action.equalsIgnoreCase("link")) {
            removeSessionAttributes(request);
            return LOGIN_VIEW;
        }
        if (this.otpService.isSendAttemptsLimitExceeded(userEmail)) {
            return "redirect:/login?error=otp_send_limit_exceeded";
        }
        OTP otp = this.otpService.generateOTP(userEmail);
        this.mailService.sendMail(this.mailService.createLinkAccountEmail(userName, userEmail, otp));
        return "redirect:/link-accounts/code";
    }

    @GetMapping("/link-accounts/code")
    public String showLinkAccountsCodePage(@SessionAttribute String userEmail, Model model) {
        model.addAttribute("email", userEmail);
        return "link-accounts-code";
    }

    @PostMapping("/link-accounts/code")
    public String verifyCode(HttpServletRequest request,
                             @SessionAttribute UUID userId,
                             @SessionAttribute String userEmail,
                             @SessionAttribute FederatedAuth federatedAuth,
                             @RequestParam String code,
                             Model model) {
        if (this.otpService.isVerifyAttemptsLimitExceeded(userEmail)) {
            return "redirect:/login?error=otp_verify_limit_exceeded";
        }
        if (this.otpService.isVerified(userEmail, code)) {
            removeSessionAttributes(request);
            createFederatedIdentityAccount(userId, federatedAuth);
            model.addAttribute("success", "Your account has been successfully linked. You can now log in using your federated account.");
            return LOGIN_VIEW;
        }
        return "link-accounts-code";
    }

    private void createFederatedIdentityAccount(UUID linkingUserId, FederatedAuth federatedAuth) {
        this.federatedIdentityAccountService.create(
                linkingUserId,
                federatedAuth.getFederatedIdentityId(),
                federatedAuth.getFederatedProvider(),
                federatedAuth.getEmail(),
                federatedAuth.getUsername());
    }

    private void removeSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute("linkingUserId");
        request.getSession().removeAttribute("linkingUserEmail");
        request.getSession().removeAttribute("federatedAuth");
    }

}