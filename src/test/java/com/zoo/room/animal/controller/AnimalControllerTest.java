package com.zoo.room.animal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoo.room.animal.model.Animal;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDate;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/animals";

    @Test
    public void test01GetAllAnimals() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(8)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].title", is("Cat")) )
                .andExpect( jsonPath("$[0].preference", is(10)) )
                .andExpect( jsonPath("$[0].type", is("<=")) )
                .andReturn();
    }

    @Test
    public void test02GetAnimalById() throws Exception {
        int id = 2;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.title", is("Dog")) )
                .andExpect( jsonPath("$.preference", is(20)) )
                .andExpect( jsonPath("$.type", is(">=")) )
                .andReturn();
    }
    
    @Test
    public void test03GetAnimalNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$.message", is("Animal not found :: " + id)) )
                .andReturn();
    }


    @Test
    public void test04CreateAnimal() throws Exception {
        this.mockMvc.perform( post("/api/animals")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(this.newAnimal() )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(9)) )
                .andExpect( jsonPath("$.title", is("Bear")) )
                .andExpect( jsonPath("$.type", is(">=")) )
                .andExpect( jsonPath("$.preference", is(100)) )
                .andReturn();

    }

    @Test
    public void test05UpdateAnimal() throws Exception {
        int id = 5;
        Animal horse = getAnimal( apiGet("/" + id).getResponse() );
        horse.setPreference(65);

        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString( horse )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.title", is("Horse")) )
                .andExpect( jsonPath("$.type", is(">=")) )
                .andExpect( jsonPath("$.preference", is(65)) )
                .andReturn();
    }

    @Test
    public void test06UpdateAnimalNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content( asJsonString( new Animal("Fox",LocalDate.of(2018,05,5), "<=", new Long(80)) )) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$.message", is("Animal not found :: " + id)) )
                .andReturn();
    }

    @Test
    public void test07GetAllAnimalsInZoo() throws Exception {
        this.mockMvc.perform( get(this.API+"/zoo")
                .param("column","title")
                .param("order","asc"))
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(4)) )
                .andExpect( jsonPath("$[0].title", is("Bear")))
                .andExpect( jsonPath("$[0].type", is(">=")))
                .andExpect( jsonPath("$[0].preference", is(100)))
                .andReturn();

    }

    @Test
    public void test08GetAllAnimalsByRoomId() throws Exception {
        int id = 2;
        this.mockMvc.perform( get(this.API+"/room/"+id)
                .param("column","title")
                .param("order","desc"))
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( jsonPath("$[0].id", is(5)) )
                .andExpect( jsonPath("$[0].title", is("Horse")) )
                .andExpect( jsonPath("$[0].type", is(">=")) )
                .andReturn();

    }

    @Test
    public void test09DeleteAnimal() throws Exception {
        String id = "9";
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("Deleted animal with id  :: " + id)) )
                .andReturn();
    }

    @Test
    public void test10DeleteAnimalNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$.message", is("Animal not found for this id :: " + id)) )
                .andReturn();
    }




    // Helper function
    public Animal newAnimal() {
        return new Animal("Bear",LocalDate.of(2010,05,20), ">=", new Long(100));
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andExpect(status().isOk())
            .andReturn();
    }
    
    public Animal getAnimal(MockHttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getContentAsString(), Animal.class);
    }
    
    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
