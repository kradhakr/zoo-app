package com.zoo.room.animal.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoo.room.animal.model.Room;
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private final String API = "/api/room";

    @Test
    public void test01GetAllRooms() throws Exception {
        this.mockMvc.perform( get(this.API) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$[0].id", is(1)) )
                .andExpect( jsonPath("$[0].title", is("Green")) )
                .andExpect( jsonPath("$[0].size", is(100)) )
                .andExpect( jsonPath("$[0].animalList", hasSize(2)) )
                .andReturn();
    }
    
    @Test
    public void test02GetRoomById() throws Exception {
        int id = 2;
        this.mockMvc.perform( get(this.API + "/" + id) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.title", is("Blue")) )
                .andExpect( jsonPath("$.size", is(70)) )
                .andExpect( jsonPath("$.animalList", hasSize(2)) )
                .andReturn();
    }
    
    @Test
    public void test03GetRoomNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform(get(this.API + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect( jsonPath("$.message", is("Room not found :: " + id)) )
                .andReturn();
    }

    @Test
    public void test04AddRoom() throws Exception {
        this.mockMvc.perform( post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(asJsonString(this.newRoom() )) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(4)) )
                .andExpect( jsonPath("$.title", is("Purple")) )
                .andExpect( jsonPath("$.size", is(45)) )
                .andExpect( jsonPath("$.animalList", hasSize(0)) )
                .andReturn();
    }

    @Test
    public void test05UpdateRoom() throws Exception {
        int id = 2;
        Room blueRoom = getRoom(apiGet("/" + id).getResponse());
        blueRoom.setSize(67);
        
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(blueRoom)) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(id)) )
                .andExpect( jsonPath("$.title", is("Blue")) )
                .andExpect( jsonPath("$.size", is(67)) )
                .andExpect( jsonPath("$.animalList", hasSize(2)) )
                .andReturn();
    }
    
    @Test
    public void test06UpdateRoomNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform( put(this.API + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString( new Room("White", new Long(100)) )) )
                .andExpect(status().isNotFound() )
                .andExpect( jsonPath("$.message", is("Room not found for id:: " + id)) )
                .andReturn();
    }

    @Test
    public void test07AddAnimalToRoomByID() throws Exception {
        int roomId = 4;
        int animalId = 8;
        this.mockMvc.perform( put("/api/room/"+roomId+"/animal/"+animalId))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.id", is(4)) )
                .andExpect( jsonPath("$.title", is("Purple")) )
                .andExpect( jsonPath("$.size", is(45)) )
                .andExpect( jsonPath("$.animalList", hasSize(1)) )
                .andReturn();
    }

    @Test
    public void test08MoveAnimalFromRoomByID() throws Exception {
        long roomId1 = 3;
        long roomId2 = 4;
        long animalId = 3;
        this.mockMvc.perform( put("/api/room/"+roomId1+"/"+roomId2 +"/animal/"+animalId))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test09GetAnimalByRoomId() throws Exception {
        int roomId=1;
        this.mockMvc.perform( get("/api/room/"+roomId+"/animals") )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( jsonPath("$[0].title", is("Cat")))
                .andExpect( jsonPath("$[0].preference", is(10)))
                .andExpect( jsonPath("$[0].type", is("<=")))
                .andExpect( jsonPath("$[1].title", is("Cow")))
                .andExpect( jsonPath("$[1].preference", is(90)))
                .andExpect( jsonPath("$[1].type", is(">=")))
                .andReturn();
    }

    @Test
    public void test10GetAllHappyAnimal() throws Exception {
        String url =this.API+"/happyAnimals";
        this.mockMvc.perform( get(url) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) )
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();
    }

    @Test
    public void test11DeleteRoom() throws Exception {
        String id = "4";
        this.mockMvc.perform(delete(this.API + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Deleted Room with id :: " + id)))
                .andReturn();
    }

    @Test
    public void test12DeleteRoomNotFound() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete(this.API + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Room not found for id :: " + id)))
                .andReturn();
    }

    @Test
    public void test13DeleteAllRooms() throws Exception {
        this.mockMvc.perform(delete(this.API))
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$", is("All rooms are deleted successfully")) )
                .andReturn();
    }



    // Helper functions
    public Room newRoom() {
        return new Room("Purple", 45L);
    }
    
    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public MvcResult apiGet(String uri) throws Exception {
        return this.mockMvc.perform(get(this.API + uri))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }



    public Room getRoom(MockHttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getContentAsString(), Room.class);
    }
    
}
