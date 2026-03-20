package com.simpleboard.user.controller;

import com.simpleboard.user.dto.UserSignUpForm;
import com.simpleboard.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userSignUpForm", new UserSignUpForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Valid UserSignUpForm userSignUpForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.signUp(userSignUpForm);
        } catch (IllegalArgumentException e) {
            model.addAttribute("signupError", e.getMessage());
            return "signup";
        }

        return "redirect:/login";
    }
}