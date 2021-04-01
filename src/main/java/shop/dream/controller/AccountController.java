package shop.dream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import shop.dream.dto.SignUpRequestDto;
import shop.dream.service.AccountService;
import shop.dream.validator.SignUpRequestDtoValidator;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpRequestDtoValidator signUpRequestDtoValidator;

    @InitBinder("signUpRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpRequestDtoValidator);
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute(new SignUpRequestDto());
        return "account/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpRequestDto signUpRequestDto, Errors errors) {
        if(errors.hasErrors()) {
            return "account/signup";
        }
        accountService.saveNewAccount(signUpRequestDto);
        return "redirect:/login";
    }

}
