package com.faziz.exercise.tradeledger.controller;

import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.domain.User;
import com.faziz.exercise.tradeledger.repository.UserRepository;
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
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping(path = "/users/{id}")
    public User getById(@PathVariable String id) {
        return userRepository.getById(id).
            orElseThrow(() -> new ResourceNotFoundException());
    }

    @GetMapping(path = "/users/search")
    public List<User> search(
            @Valid @RequestParam(value = "filter") final List<Filter> filters) {
        return userRepository.search(filters);
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return userRepository.getAll();
    }
}
