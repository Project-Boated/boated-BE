package com.projectboated.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("/test/kakaologin")
	public String kakaoLoginTest() {
		return "kakaologin/kakao-login-test";
	}

	@GetMapping("/test/kakaologin/redirection")
	public String kakaoLoginTestRedirection() {
		return "kakaologin/kakao-login-test-redirection";
	}
}
