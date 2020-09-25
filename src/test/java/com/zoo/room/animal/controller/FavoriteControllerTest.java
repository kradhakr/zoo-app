package com.zoo.room.animal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Favorite;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FavoriteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/favorite";


    @Test
    public void test01GetAllFavorite() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].roomId", is(1)) )
                .andExpect( jsonPath("$[0].animalId", is(4)) )
                .andReturn();
    }

    @Test
    public void test02GetFavoriteById() throws Exception {
        int id = 1;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.roomId", is(1)) )
                .andExpect( jsonPath("$.animalId", is(4)) )
                .andReturn();
    }

    @Test
    public void test03GetFavoriteByIdNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath("$.message", is("Favorite not found :: " + id)) )
                .andReturn();
    }


    @Test
    public void test04CreateFavorite() throws Exception {
        this.mockMvc.perform( post(this.API)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(this.newFavorite() )) )
                .andExpect( status().isOk() )
                .andReturn();
    }

    @Test
    public void test05FavoriteRoomByAnimalId() throws Exception {
        int id = 4;
        this.mockMvc.perform( get(this.API + "/animal/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(1)) )
                .andExpect( jsonPath("$[0]", hasToString("Green")) )
                .andReturn();
    }

    @Test
    public void test06DeleteFavorite() throws Exception {
        String id = "2";
        this.mockMvc.perform( delete(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("Deleted Favorite with id :: " + id)) )
                .andReturn();
    }

    // Helper function
    public Favorite newFavorite() { return new Favorite(3L,8L);
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andExpect(status().isOk())
            .andReturn();
    }
    
    public Animal getFavorite(MockHttpServletResponse res) throws Exception {
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
