package com.example.project.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession session;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signin")
    public String signin() {
        return "auth/signin";
    }

    @PostMapping("/signin")
    public String signinPost(@ModelAttribute User user) {

        String email = user.getEmail();
        User result = userRepository.findByEmail(email);

        if (result != null) {
            String pwd = result.getPwd();
            boolean isMatch = passwordEncoder.matches(user.getPwd(), pwd);

            if (isMatch) {
                session.setAttribute("user_info", result);
                session.setAttribute("email", email);
                session.setAttribute("id", result.getId());

                return "redirect:/";
            } else

                return "auth/resignin";
        }
        else {
            return "auth/resignin";
        }
    }

    @GetMapping("/signout")
    public String signout() {

        session.removeAttribute("user_info"); // 세션에서 사용자 정보 제거
        System.out.println("로그아웃됐습니다");
        return "auth/signout"; // 로그아웃 후 홈 페이지로 리다이렉트
    }

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute User user) {
        User email = userRepository.findByEmail(user.getEmail());
        String hashedPassword = passwordEncoder.encode(user.getPwd());
        user.setPwd(hashedPassword);
        if(email==null){
            userRepository.save(user);
            return "/auth/signin";
        }else{
            return "redirect:/";
        }

    }

    @GetMapping("/updateUserInfo")
    public String updateUserInfo(
            Model model) {

        // updateUserInfo.html은 비밀번호 암호화 작업 후 비밀번호 수정 파일로 나눌 필요가 있음.
        User userInfo = (User) session.getAttribute("user_info");
        Long id = userInfo.getId();
        Optional<User> data = userRepository.findById(id);
        User user = data.get();
        model.addAttribute("user", user);

        return "auth/updateUserInfo";
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfoPost(
            @ModelAttribute User user) {
        System.out.println(user);
        User userInfo = (User) session.getAttribute("user_info");
        Long id = userInfo.getId();
        Optional<User> data = userRepository.findById(id);

        user.setPwd(data.get().getPwd());
        user.setId(id);
        user.setEmail(data.get().getEmail());
        user.setCredate(data.get().getCredate());

        userRepository.save(user);

        return "redirect:/";
    }

    @GetMapping("/email-check")
    @ResponseBody
    public String emailCheck(@ModelAttribute User user) {
        String email = user.getEmail();

        User opt = userRepository.findByEmail(email);
        if (opt != null) {
            return "가입불가";
        } else {
            return "가입가능";
        }
    }

}
