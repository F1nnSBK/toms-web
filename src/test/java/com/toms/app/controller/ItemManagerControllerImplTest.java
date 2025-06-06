package com.toms.app.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ItemManagerControllerImpl.class)
public class ItemManagerControllerImplTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void updateItem() throws Exception {
		this.mockMvc.perform(post("/admin/items/edit/submit/{itemId}", "abc"))
			.andExpect(status().isOk())
			.andExpect(view().name("abc"))
			.andExpect(model().attributeExists("<name>"))
			.andExpect(model().attribute("<name>", "<value>"))
			.andExpect(content().string(""));
	}
}
