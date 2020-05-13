package br.com.uabrestingaseca.biblioteca.controllers;

import br.com.uabrestingaseca.biblioteca.BibliotecaApplicationTests;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class LivroControllerTest extends BibliotecaApplicationTests {

    @Autowired
    private LivroController livroController;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(livroController).build();
    }

    @Test
    public void firstTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/livros")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
