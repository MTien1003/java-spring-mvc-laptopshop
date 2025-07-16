package vn.hoidanit.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @GetMapping("/")
	public String index() {
		return "Xin chao";
	}

	@GetMapping("/user")
	public String userPage() {
		return "Only user can see this";
	}

	@GetMapping("/admin")
	public String adminPage() {
		return "Only admin can see this";
	}
	

}
