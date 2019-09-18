package com.faziz.exercise.tradeledger.controller;

import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.domain.Event;
import com.faziz.exercise.tradeledger.repository.EventRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class EventController extends BaseController {

    @Autowired
    private EventRepository eventRepository;
    
    @GetMapping(path = "/events/{id}")
    public Event getById(@PathVariable String id) {
        return eventRepository.getById(id).
            orElseThrow(() -> new ResourceNotFoundException());
    }

    @GetMapping(path = "/events/search")
    public List<Event> search(
          @Valid @RequestParam(value = "filter") List<Filter> filters) {
        return eventRepository.search(filters);
    }

    @GetMapping(path = "/events")
    public List<Event> getAll() {
        return eventRepository.getAll();
    }
}
