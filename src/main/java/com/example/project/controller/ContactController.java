package com.example.project.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.email.Mailer;
import com.example.project.email.SMTPAuthenticator;
import com.example.project.model.User;
import com.example.project.model.Verifynum;
import com.example.project.repository.UserRepository;
import com.example.project.repository.VerifynumRepository;

@Controller
@RequestMapping("/auth")
public class ContactController {
	@Autowired
	VerifynumRepository verifynumRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	// @GetMapping("/contact")
	// public String contact(@RequestParam String email, Model model) {
	// User opt = userRepository.findByEmail(email);
	// model.addAttribute("user", opt.get(email));

	// return "contact/contact";
	// }

	@GetMapping("/certifi")
	public String contact() {
		return "/auth/certifi";
	}

	@PostMapping("/certifi")
	public String contact2(
			@RequestParam String verNum) {
		User user = (User) session.getAttribute("user_info");
		System.out.println(user);
		String email = user.getEmail();
		Verifynum verdb = verifynumRepository.findByEmail(email);
		if(verdb == null){
			return "redirect:/auth/certifi";
		}
		String ranNum = verdb.getNum();
		
		System.out.println(verNum + " 하고 " + ranNum);
		
		if (verNum.equals(ranNum)) {
			verifynumRepository.delete(verdb);
			return "/index";
		} else {
			System.out.println("다시 해");
		}

		return "redirect:/auth/certifi";
	}

	@ResponseBody
	@GetMapping("/certifi1")
	public String contact1(String ran) {
		Verifynum verifynum = new Verifynum();

		User userinfo = (User) session.getAttribute("user_info");
		String email = userinfo.getEmail();

		Verifynum verdb = verifynumRepository.findByEmail(email);
		if(verdb != null) {
			long verdbid = verdb.getId();
			verifynum.setId(verdbid);
		}

		verifynum.setNum(ran);
		verifynum.setEmail(email);
		verifynumRepository.save(verifynum);

		Mailer mailer = new Mailer();
		mailer.sendMail(
				email, // 수신 이메일(관리자)
				"[" + email + "] 메일인증", // [작성자 이메일]제목
				"인증번호는" + ran + "입니다.", // 본문
				new SMTPAuthenticator()); // 인증
		return "인증번호가 전송되었습니다.";
	}
}