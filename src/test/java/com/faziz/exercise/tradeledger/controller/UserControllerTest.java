package com.faziz.exercise.tradeledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.domain.User;
import com.faziz.exercise.tradeledger.repository.UserRepository;
import java.io.IOException;
import java.io.StringWriter;
import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.forName;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mvc;

    private User user1;
    private User user2;
    private User user3; 
    
    private Filter filter1;
    private Filter filter2;

    @Before
    public void setUp() {
        user1 = new User("1", "User1", "Workstation1");
        user2 = new User("2", "User2", "Workstation2");
        user3 = new User("3", "User3", "Workstation3");
        
        when(userRepository.getAll()).thenReturn(
            asList(user1, user2, user3));
        when(userRepository.getById("1")).thenReturn(of(user1));
        when(userRepository.getById("2")).thenReturn(of(user2));
        when(userRepository.getById("3")).thenReturn(of(user3));
        
        filter1 = filter("user", "eq", "User1");
        filter2 = filter("user", "eq", "User2");
    }

    @Test
    public void testGetUserById() throws Exception {
        mvc.perform(get("/users/{id}", 1).
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("id", is(user1.getId()))).
                            andExpect(jsonPath("user", is(user1.getUser()))).
                                andExpect(jsonPath("workstation", is(user1.getWorkstation())));

        mvc.perform(get("/users/{id}", 2).
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("id", is(user2.getId()))).
                            andExpect(jsonPath("user", is(user2.getUser()))).
                                andExpect(jsonPath("workstation", is(user2.getWorkstation())));

        mvc.perform(get("/users/{id}", 3).
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("id", is(user3.getId()))).
                            andExpect(jsonPath("user", is(user3.getUser()))).
                                andExpect(jsonPath("workstation", is(user3.getWorkstation())));
    }
    
    @Test
    public void testSearchByUser() throws Exception {
        doReturn(asList(user1)).when(userRepository).search(any(List.class));
        mvc.perform(get("/users/search").
            param("filter", jsonFilter(filter1)).
                contentType(APPLICATION_JSON)).
                    andExpect(status().isOk()).
                        andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                            andExpect(jsonPath("[0].id", is(user1.getId()))).
                                andExpect(jsonPath("[0].user", is(user1.getUser()))).
                                    andExpect(jsonPath("[0].workstation", is(user1.getWorkstation())));
        
        doReturn(asList(user1, user2)).when(userRepository).search(any(List.class));
        mvc.perform(get("/users/search").
            param("filter", jsonFilter(filter1), jsonFilter(filter2)).
                contentType(APPLICATION_JSON)).
                    andExpect(status().isOk()).
                        andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                            andExpect(jsonPath("[0].id", is(user1.getId()))).
                                andExpect(jsonPath("[0].user", is(user1.getUser()))).
                                    andExpect(jsonPath("[0].workstation", is(user1.getWorkstation()))).
                                        andExpect(jsonPath("[1].id", is(user2.getId()))).
                                            andExpect(jsonPath("[1].user", is(user2.getUser()))).
                                                andExpect(jsonPath("[1].workstation", is(user2.getWorkstation())));
    }
    
    private Filter filter(String attribute, String operator, String value) {
        Filter f2 = new Filter();
        f2.setAttribute(attribute);
        f2.setOperator(operator);
        f2.setValue(value);
        return f2;
    }
    
    private String jsonFilter(Filter filter) throws IOException {
        ObjectMapper obm = new ObjectMapper();
        StringWriter w = new StringWriter();
        obm.writeValue(w, filter);
        return encode(w.toString(), forName("UTF-8").name());
    }
}
