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
public class MarketIntelligenceControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private HttpHeaders httpHeaders;

	Long createdMiId;

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
	public void addMarketIntelligence() throws Exception {
		String jsonString = "{\n" + "\"type\" : \"project4\",\n" + " \"name\": \"new project4\",\n"
				+ "  \"status\":\"open\",\n" + "  \"investment\" : 4,\n" + "  \"description\" : \"4444\",\n"
				+ "  \"creatorId\":1113\n" + "}";

		String responseString = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/marketIntelligence").content(jsonString)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.headers(this.httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		Long createdMiId = mapper.readValue(responseString, Long.class);
		this.createdMiId = createdMiId;
	}

	@Test
	public void getMarketIntelligenceById() throws Exception {
		this.mockMvc.perform(get("/marketIntelligence/{id}", this.createdMiId).headers(this.httpHeaders)).andDo(print())
				.andExpect(status().isOk());
	}

}
