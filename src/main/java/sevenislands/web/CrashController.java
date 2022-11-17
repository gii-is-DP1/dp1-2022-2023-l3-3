package sevenislands.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrashController {

	@GetMapping({"/oups", "/error"})
	public String triggerException() {
		return "exception";
	}
}
