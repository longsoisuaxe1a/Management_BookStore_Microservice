package com.example.UserService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.UserService.UserServiceApplication;
import com.example.UserService.entity.User;
import com.example.UserService.repositories.UserRedisRepository;
import com.example.UserService.repositories.UserRepository;
import com.example.UserService.service.UserService;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v2")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userReponsive;
	@Autowired
	UserRedisRepository userRedisReponse;

	// Lấy danh sách user
	@GetMapping("/user")
	@RateLimiter(name = "findAllUser")
	public List<User> getAllUser() {
		List<User> listUser = new ArrayList<>();
		if (userRedisReponse.findAll() == null) {
			listUser = userService.findAllUser();
		} else {
			listUser = userRedisReponse.findAll();
		}
		System.out.println(listUser);
		return listUser;
	}
	
	@PostMapping("/user")
	@RateLimiter(name = "addUser")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		userService.addUser(user);
		return ResponseEntity.ok(user);
	}

	// Tim thong tin theo id
	@GetMapping("/user/{id}")
	@RateLimiter(name = "findUserById")
	public User findByIdUser(@PathVariable String id) {
		Optional<User> optional = userReponsive.findById(id);
		User user = null;
		if (optional.isPresent()) {
			user = optional.get();
		} else {
			new RuntimeException("Khong co user theo id này");
		}

		return user;
	}

	public ResponseEntity<String> rateLimitingFallback(RequestNotPermitted ex) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Retry-After", "60s");

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(responseHeaders) // send retry header
				.body("Too Many Requests - Retry After 1 Minute");
	}

//	@ExceptionHandler(RequestNotPermitted.class)
//	public ResponseEntity<Object> handleRequestNotPermitted(RequestNotPermitted ex, HttpServletRequest request) {
//		logger.warn("Số request bị giới hạn", request.getRequestURI(), ex.getMessage());
//		return new ResponseEntity<>("Vui lòng gửi request lại sau 1 phút", HttpStatus.TOO_MANY_REQUESTS);
//	}
	@ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<Object> handleRequestNotPermitted(RequestNotPermitted ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Retry-After", "60s"); // Thông báo thời gian chờ sau khi vượt quá giới hạn
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .headers(headers)
                             .body("Vượt quá số lượng yêu cầu cho phép, vui lòng thử lại sau."); // Thông báo lỗi
    }
}
