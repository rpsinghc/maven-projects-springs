package spring_examples.simple_web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
public class TestController {
	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		//return "Spring boot is working!";
		return "index.";
	}
	

}