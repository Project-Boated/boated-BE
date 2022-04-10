package my.sleepydeveloper.projectcompass;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("/test/kakaologin")
	public String kakaoLoginTest() {
		return "kakaologin/kakao-login-test";
	}
}
