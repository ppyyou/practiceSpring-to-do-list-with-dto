package com.ppuu.to_do_list_with_dto.controller;

import com.ppuu.to_do_list_with_dto.dto.LoginDto;
import com.ppuu.to_do_list_with_dto.model.User;
import com.ppuu.to_do_list_with_dto.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;

    @GetMapping({"/", "/login"})
    public String showLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());

        return "login";
    }

    @PostMapping("/login")
    public String password(
            @Valid @ModelAttribute("loginDto") LoginDto loginDto,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = userRepository.findByUsername(loginDto.getUsername());

            if(!user.getPassword().equals(loginDto.getPassword())) {
                model.addAttribute("error", "존재하지 않는 아이디 또는 올바르지 않은 비밀번호입니다.");

                return "login";
            }

            httpSession.setAttribute("user", user);

            return "redirect:/todos";

        } catch(Exception e) {
            model.addAttribute("error", "존재하지 않는 사용자입니다.");

            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/login";
    }

}
