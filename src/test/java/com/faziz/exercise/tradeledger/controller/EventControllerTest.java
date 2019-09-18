package com.faziz.exercise.tradeledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.domain.Event;
import static com.faziz.exercise.tradeledger.domain.EventType.LOGIN;
import static com.faziz.exercise.tradeledger.domain.EventType.LOGOUT;
import com.faziz.exercise.tradeledger.repository.EventRepository;
import java.io.IOException;
import java.io.StringWriter;
import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.forName;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {
    
    @MockBean
    private EventRepository eventRepository;
    @Autowired
    private MockMvc mvc;
    
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;
    private Event event5;
    private Event event6;
    private Event event7;
    private Event event8;
    
    private Filter filter1;
    private Filter filter2;
    
    @Before
    public void setUp() {
        event1 = new Event("1", LOGIN, 1262340660000l, "user1@sample.io", "192.168.1.10");
        event2 = new Event("2", LOGIN, 1262427060000l, "user2@sample.io", "192.168.1.11");
        event3 = new Event("3", LOGIN, 1262513460000l, "user3@sample.io", "192.168.1.11");
        event4 = new Event("3", LOGIN, 1262513460000l, "user4@sample.io", "192.168.1.13");
        event5 = new Event("4", LOGOUT, 1262369460000l, "user1@sample.io", "192.168.1.10");
        event6 = new Event("5", LOGOUT, 1262455860000l, "user2@sample.io", "192.168.1.11");
        event7 = new Event("6", LOGOUT, 1262542260000l, "user3@sample.io", "192.168.1.11");
        event8 = new Event("7", LOGOUT, 1262628660000l, "user4@sample.io", "192.168.1.13");
        
        when(eventRepository.getAll()).thenReturn(
            asList(event1, event2, event3, event4, event5, event6, event7, event8));
        
        when(eventRepository.getById("1")).thenReturn(of(event1));
        when(eventRepository.getById("2")).thenReturn(of(event2));
        when(eventRepository.getById("3")).thenReturn(of(event3));
        when(eventRepository.getById("4")).thenReturn(of(event4));
        when(eventRepository.getById("5")).thenReturn(of(event5));
        when(eventRepository.getById("6")).thenReturn(of(event6));
        when(eventRepository.getById("7")).thenReturn(of(event7));
        when(eventRepository.getById("8")).thenReturn(of(event8));
        
        filter1 = filter("user", "eq", "user1@sample.io");
        filter2 = filter("user", "eq", "user2@sample.io");
    }

    @Test
    public void testGetById() throws Exception {
        mvc.perform(get("/events/{id}", 1).
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("id", is(event1.getId()))).
                            andExpect(jsonPath("user", is(event1.getUser()))).
                                andExpect(jsonPath("ip", is(event1.getIp())));

        mvc.perform(get("/events/{id}", 2).
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("id", is(event2.getId()))).
                            andExpect(jsonPath("user", is(event2.getUser()))).
                                andExpect(jsonPath("ip", is(event2.getIp())));
    }

    @Test
    public void testSearch() throws Exception {
        doReturn(asList(event1)).when(eventRepository).search(any(List.class));
        mvc.perform(get("/events/search").
            param("filter", jsonFilter(filter1)).
                contentType(APPLICATION_JSON)).
                    andExpect(status().isOk()).
                        andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                            andExpect(jsonPath("[0].id", is(event1.getId()))).
                                andExpect(jsonPath("[0].user", is(event1.getUser()))).
                                    andExpect(jsonPath("[0].ip", is(event1.getIp())));
        
        doReturn(asList(event1, event2, event3)).when(eventRepository).search(any(List.class));
        mvc.perform(get("/events/search").
            param("filter", jsonFilter(filter1)).
                contentType(APPLICATION_JSON)).
                    andExpect(status().isOk()).
                        andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                            andExpect(jsonPath("[0].id", is(event1.getId()))).
                                andExpect(jsonPath("[0].user", is(event1.getUser()))).
                                    andExpect(jsonPath("[0].ip", is(event1.getIp()))).
                                        andExpect(jsonPath("[1].id", is(event2.getId()))).
                                            andExpect(jsonPath("[1].user", is(event2.getUser()))).
                                                andExpect(jsonPath("[1].ip", is(event2.getIp())));
    }

    @Test
    public void testGetAll() throws Exception {
        mvc.perform(get("/events").
            contentType(APPLICATION_JSON)).
                andExpect(status().isOk()).
                    andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON)).
                        andExpect(jsonPath("[0].id", is(event1.getId()))).
                            andExpect(jsonPath("[0].user", is(event1.getUser()))).
                                andExpect(jsonPath("[0].ip", is(event1.getIp())));
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
