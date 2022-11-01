package org.springframework.samples.sevenislands.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {

	@GetMapping({ "/signup" })
	public String signup(Map<String, Object> model) {
		return "views/signup";
	}
}
