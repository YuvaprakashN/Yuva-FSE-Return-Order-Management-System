package com.roms.microservice.authenticationservice.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.roms.microservice.authenticationservice.AbstractTest;
import com.roms.microservice.authenticationservice.exception.ExceptionResponse;
import com.roms.microservice.authenticationservice.httppayload.AuthResponse;
import com.roms.microservice.authenticationservice.httppayload.LoginRequest;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)

class AuthenticationControllerTest extends AbstractTest {



	@Test
	public void loginValidUserTest() throws Exception {
		String uri = "/auth/login";
		LoginRequest request = new LoginRequest();
		request.setEmail("ganesh@gmail.com");
		request.setPassword("ganeshganesh");
		String inputJson = super.mapToJson(request);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		System.out.println(mvcResult.getResponse());
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		AuthResponse res = super.mapFromJson(content, AuthResponse.class);
		System.out.println(res);
		assertEquals(res.getUserName(), "Ganesh");
	}

	@Test
	public void loginInvalidEmailUserTest() throws Exception {
		String uri = "/auth/login";
		LoginRequest request = new LoginRequest();
		request.setEmail("ganesh123@gmail.com");
		request.setPassword("ganeshganesh");
		String inputJson = super.mapToJson(request);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		System.out.println(mvcResult.getResponse());
//		Exception resolvedException = mvcResult.getResolvedException();
		assertEquals("INVALID_CREDENTIALS",
				mvcResult.getResolvedException().getMessage());

		int status = mvcResult.getResponse().getStatus();
		assertEquals(500, status);
		String content = mvcResult.getResponse().getContentAsString();
		ExceptionResponse res = super.mapFromJson(content, ExceptionResponse.class);
		System.out.println(res);

	}

	@Test
	public void loginInvalidPasswordUserTest() throws Exception {
		String uri = "/auth/login";
		LoginRequest request = new LoginRequest();
		request.setEmail("ganesh@gmail.com");
		request.setPassword("ganeshganesh123");
		String inputJson = super.mapToJson(request);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		System.out.println(mvcResult.getResponse());
//		Exception resolvedException = mvcResult.getResolvedException();
		assertEquals("Bad credentials", mvcResult.getResolvedException().getMessage());

		int status = mvcResult.getResponse().getStatus();
		assertEquals(500, status);
		String content = mvcResult.getResponse().getContentAsString();
		ExceptionResponse res = super.mapFromJson(content, ExceptionResponse.class);
		System.out.println(res);

	}
	
	
	
	@Test
	public void validateAndReturnUserTest() throws Exception {
		String uri = "/auth/login";
		LoginRequest request = new LoginRequest();
		request.setEmail("ganesh@gmail.com");
		request.setPassword("ganeshganesh");
		String inputJson = super.mapToJson(request);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		AuthResponse res = super.mapFromJson(content, AuthResponse.class);
		
		String validationURL="/auth/validate";
		  mvcResult = mvc.perform(MockMvcRequestBuilders.get(validationURL).header("Authorization", "Bearer "+res.getToken())
		         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		      
		System.out.println(mvcResult.getResponse());
		assertEquals("true",mvcResult.getResponse().getContentAsString());

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		System.out.println(res);

	}

}
