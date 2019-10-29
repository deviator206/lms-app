/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.JwtAuthenticationResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private HttpHeaders httpHeaders;

	Long createdUserId;

	@Before
	public void init() throws UnsupportedEncodingException, Exception {
		String jsonString = "{\"userName\":\"syadav1\",\"password\":\"password\"}";

		String responseString = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/login").content(jsonString)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		JwtAuthenticationResponse jwtAuthenticationResponse = mapper.readValue(responseString,
				JwtAuthenticationResponse.class);
		String fullAccessToken = jwtAuthenticationResponse.getTokenType() + " "
				+ jwtAuthenticationResponse.getAccessToken();
		HttpHeaders httpHeadersTemp = new HttpHeaders();
		httpHeadersTemp.add("Authorization", fullAccessToken);
		this.httpHeaders = httpHeadersTemp;
	}

	@Test
	public void addUser() throws Exception {
		this.createUser();
	}

	public Long createUser() throws UnsupportedEncodingException, Exception {
		Random rand = new Random();
		int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
		long time = System.currentTimeMillis();
		String uniqueValue = "TestUser" + randomNum + "" + time;
		System.out.println("Random User Name -- >> " + uniqueValue);
		String jsonString = "{\n" + "		\"userName\" : \"" + uniqueValue + "\",\n"
				+ "		\"password\" : \"password\"\n" + "}";

		String responseString = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/user").content(jsonString)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		Long createdUserId = mapper.readValue(responseString, Long.class);

		return createdUserId;
	}

	@Test
	public void getUsers() throws Exception {
		this.mockMvc.perform(get("/users").headers(this.httpHeaders)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getUserByUserId() throws Exception {
		Long userId = this.createUser();
		this.mockMvc.perform(get("/user/{userId}", userId).headers(this.httpHeaders)).andDo(print())
				.andExpect(status().isOk());
	}

}
