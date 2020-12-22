package com.example.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.common.AppConstants;
import com.example.controller.PostController;
import com.example.exception.NoDataException;
import com.example.model.Post;

@Service
public class PostService {
	@Autowired
	RestTemplate restTemplate;
	private final Logger logger = LoggerFactory.getLogger(PostService.class);
	
	public List<Post> findAll() throws NoDataException{
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
		Post[] response = restTemplate.getForObject(
				  AppConstants.POST_URL,
				  Post[].class);
		List<Post> posts = List.of(response);
        return posts;
	}
	public String testMessage() {
		return "test";
	}
	public int coundUniqueUsers() throws NoDataException {
		List<Post> posts = findAll();
		return posts.stream()
			.filter(distinctByKey(p -> p.getUserId())) 
			.collect(Collectors.toList()).size();
	}
	public static <T> Predicate<T> distinctByKey(
		Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
	}
	public List<Post> update4thPost() throws NoDataException {
		List<Post> posts = findAll();
		if(!posts.isEmpty() && posts.size()>=4) {
			posts.get(3).setBody("encora");
			posts.get(3).setTitle("encora");
		}
		return posts;
	}
}
