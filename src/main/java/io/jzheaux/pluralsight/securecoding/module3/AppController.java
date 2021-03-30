package io.jzheaux.pluralsight.securecoding.module3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Controller
public class AppController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserService users;

	public AppController(UserService users) {
		this.users = users;
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String create(@RequestParam("email") String email,
						 @RequestParam("password") String password,
						 @RequestParam("fullName") String fullName,
						 @RequestParam("photo") MultipartFile multipart) throws Exception {
		File file = new File(multipart.getOriginalFilename());
		file.createNewFile();
		try (OutputStream os = new FileOutputStream(file)) {
			os.write(multipart.getBytes());
		}

		User user = this.users.create(email, password, fullName, multipart.getName());
		if (user == null) {
			return "register";
		}
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam("email") String email,
						@RequestParam("password") String password,
						HttpServletRequest request) {
		User user = this.users.lookup(email, password);
		if (user == null) {
			return "login";
		}
		request.getSession().setAttribute("user", user);
		return "redirect:/";
	}

	@GetMapping("/")
	public String home(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		return "home";
	}
}
