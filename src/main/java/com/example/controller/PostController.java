package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.common.AppConstants;
import com.example.exception.NoDataException;
import com.example.model.Post;
import com.example.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	RestTemplate restTemplate;
	private final Logger logger = LoggerFactory.getLogger(PostController.class);
	
	@Autowired
	PostService postService;
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
    }
	
	@GetMapping(value="/all",produces = "application/json")
	public ResponseEntity<?> getPosts() {
		logger.warn("posts requested. at /all");
		List<Post> posts;
		try {
			posts = postService.findAll();
			logger.warn("all posts retrieved");
		} catch (NoDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("Error: Failed to retrieve posts");
			return ResponseEntity
					.badRequest()
					.body("Error: Failed to retrieve posts");
		}
		return ResponseEntity.ok()
				.headers(new HttpHeaders())
				.body(posts);
		
	}
	@GetMapping(value="/count_unique_users",produces = "text/plain")
	public ResponseEntity<?> countUniqueUsers() {
		logger.warn("count unique users reached");
		int cnt;
		try {
			cnt = postService.coundUniqueUsers();
			return ResponseEntity.ok()
					.headers(new HttpHeaders())
					.body(String.valueOf(cnt));	
		} catch (NoDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity
				.badRequest()
				.body("Error: Failed to retrieve posts");
		
	}
	
	@PutMapping(value="/update_4th_to_encora",produces = "application/json")
	public ResponseEntity<?> updateThe4thPost() {
		List<Post> posts;
		try {
			posts = postService.update4thPost();
			logger.warn("4th post updated succesfully.");
			return ResponseEntity.ok()
					.headers(new HttpHeaders())
					.body(posts);	
		} catch (NoDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity
				.badRequest()
				.body("Error: Failed to retrieve posts");
	}
	@GetMapping(value="/test",produces = "text/plain")
	public ResponseEntity<?> test() {
		logger.warn("test method reached");
		String msg = postService.testMessage();
		return ResponseEntity.ok()
				.headers(new HttpHeaders())
				.body(msg);	
	}
	public static <T> Predicate<T> distinctByKey(
		Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
	}
}
