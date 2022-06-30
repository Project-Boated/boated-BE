package com.projectboated.backend.security.code;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityHandlerTestController {

    @ResponseBody
    @GetMapping("/test/permit-all")
    public void permitAll() {
    }

    @ResponseBody
    @GetMapping("/test/access-denied")
    public void accessDenied() {
    }

}
