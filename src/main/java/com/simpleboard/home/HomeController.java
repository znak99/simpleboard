package com.simpleboard.home;

import com.simpleboard.user.security.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal SecurityUser loginUser, Model model) {
        model.addAttribute("loginUser", loginUser);
        return "home";
    }
}