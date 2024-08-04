package com.example.PET.controller;

import com.example.PET.domain.LoginForm;
import com.example.PET.domain.Member;
import com.example.PET.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String registForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String insertMember(@Valid @ModelAttribute("membership") Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        memberService.join(member);
        return "redirect:/";
    }

    @RequestMapping("/idcheck")
    @ResponseBody
    public Map<Object, Object> idcheck(@RequestBody String user_id) {
        int count = 0;
        Map<Object, Object> map = new HashMap<Object, Object>();

        count = memberService.idcheck(user_id);
        map.put("cnt", count);
        System.out.println(map.get("cnt"));
        return map;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login";
    }

    @PostMapping("/login")
    public String getMember(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        Member loginMember = memberService.check(loginForm.getUser_id(), loginForm.getUser_pw());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다");
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", loginMember);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("loginMember");
        String user_id = member.getUser_id();
        System.out.println(member.getUser_id());

        if (session != null) {
            if(memberService.login_checkout(user_id)) {
                session.invalidate();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/problem")
    public String redirect() {
        return "redirect:http://localhost:8501/";
    }
}

