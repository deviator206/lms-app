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
public class LeadDetailControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private HttpHeaders httpHeaders;

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
	public void getLeadById() throws Exception {
		this.mockMvc.perform(get("/lead/47").headers(this.httpHeaders)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getleads() throws Exception {
		this.mockMvc.perform(get("/leads").headers(this.httpHeaders)).andExpect(status().isOk());
	}

	@Test
	public void createLead() throws Exception {
		String jsonString = "{\n" + "  \"source\": \"ONLINE\",\n" + "  \"custName\": \"CM TECH\",\n"
				+ "  \"description\": \"CHanges\",\n" + "  \"tenure\": \"LT1Y\",\n" + "  \"leadContact\": {\n"
				+ "    \"name\": \"rohini Mishra\",\n" + "    \"email\": \"976545239\",\n"
				+ "    \"phoneNumber\": \"\",\n" + "    \"country\": \"IND\",\n" + "    \"state\": \"KAR\"\n" + "  },\n"
				+ "  \"leadsSummaryRes\": {\n" + "    \"businessUnits\": [\n" + "      \"SP\"\n" + "    ],\n"
				+ "    \"industry\": \"ENGG\",\n" + "    \"budget\": \"111\",\n" + "    \"currency\": \"INR\"\n"
				+ "  },\n" + "  \"deleted\": false,\n" + "  \"creatorId\": 1113,\n"
				+ "  \"creationDate\": \"2020-11-01\"\n" + "}";

		this.mockMvc.perform(MockMvcRequestBuilders.post("/rootlead").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).headers(this.httpHeaders))
				.andDo(print()).andExpect(status().isCreated());
	}
}
