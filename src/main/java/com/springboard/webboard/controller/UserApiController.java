package com.springboard.webboard.controller;

import com.springboard.webboard.entity.Board;
import com.springboard.webboard.entity.User;
import com.springboard.webboard.repository.UserRepository;
import com.springboard.webboard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
class UserApiController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    JdbcTemplate jdbcTemplate;

//    @GetMapping("/users")
//    public String all(Model model) {
//        List<User> users = repository.findAll();
//        model.addAttribute("users",users);
//        return "user/userlist";
//    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());
//                    user.setBoards(newUser.getBoards());
                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for (Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        SecurityContextHolder.clearContext();
        return "/";
    }
}