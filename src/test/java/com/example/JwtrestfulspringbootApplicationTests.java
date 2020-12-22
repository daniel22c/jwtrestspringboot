package com.example;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.example.common.AppConstants;
import com.example.controller.PostController;
import com.example.model.Post;
import com.example.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtrestfulspringbootApplicationTests {
	@LocalServerPort
    int testServerPort;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PostController postController = new PostController();
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();
	
    @MockBean
    private PostService postService;
    @Autowired
    private PostService postRealService;
    private final Logger logger = LoggerFactory.getLogger(JwtrestfulspringbootApplicationTests.class);
	@Test
    public void contextLoads() {
    }
	
	@Before
	public void setup() {
		mockMvc=MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.dispatchOptions(true)
				.build();
	}
	@Test
	@WithMockUser(username="admin", password="password")
    public void testMsg() throws Exception {
		when(postService.testMessage()).thenReturn("test");
        mockMvc.perform(get("/posts/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
        verify(postService, times(1)).testMessage();
    }
	
	@Test                                                                                          
	@WithMockUser(username="admin", password="password")
    public void testPostfindAllController() throws Exception{   
		final String baseUrl = "http://localhost:" + testServerPort + "/posts/all";
	    URI uri = new URI(baseUrl);
	    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        Post post = new Post(1L, 1L, "title","body");  
        mockServer.expect(ExpectedCount.once(), 
          requestTo(uri))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(mapper.writeValueAsString(post))
        );        
        HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    ResponseEntity<Post> response = restTemplate.getForEntity(uri, Post.class);
        mockServer.verify();
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

	@Test
	public void testExtRestReturnsPosts() throws Exception{
		final String extUrl = "http://jsonplaceholder.typicode.com/posts";
	    URI uri = new URI(extUrl);
	    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        Post post = new Post(2L, 2L, "title2","body2");
        List<Post> posts = Arrays.asList(
        		new Post(2L, 2L, "title2","body2"),
        		new Post(3L, 3L, "title3","body3"));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(uri))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(mapper.writeValueAsString(posts))
        );        
	    ResponseEntity<?> response = restTemplate.getForEntity(uri, Post[].class);
        Post[] list =   (Post[]) response.getBody();
        logger.warn(list[0].toString());
        
        Assert.assertEquals(2, list.length);              
	}
	@Test
	@WithMockUser(username="admin", password="password")
    public void testUpdate4thPost() throws Exception {
		List<Post> postStub = Arrays.asList(
				new Post(1L, 1L, "title1","body1"),
        		new Post(2L, 2L, "title2","body2"),
        		new Post(3L, 3L, "title3","body3"),
        		new Post(4L, 4L, "encora","encora"));
		when(postService.update4thPost()).thenReturn(postStub);
        mockMvc.perform(put("/posts/update_4th_to_encora"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[3].title", is("encora")))
                .andExpect(jsonPath("$[3].body", is("encora")));
        verify(postService, times(1)).update4thPost();
    }
	@Test
	@WithMockUser(username="admin", password="password")
    public void testCountUniqueUsers() throws Exception {
		when(postService.coundUniqueUsers()).thenReturn(2);
        mockMvc.perform(get("/posts/count_unique_users"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
        verify(postService, times(1)).coundUniqueUsers();
    }
	@Test
	@WithMockUser(username="admin", password="password")
    public void testGetAllPosts() throws Exception {
		List<Post> postStub = Arrays.asList(
        		new Post(2L, 2L, "title2","body2"),
        		new Post(3L, 3L, "title3","body3"));
		when(postService.findAll()).thenReturn(postStub);
        mockMvc.perform(get("/posts/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$.*", hasSize(2)));
        verify(postService, times(1)).findAll();
    }
	


}
