package com.penszzip.headStarter.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/auth")
    public String postMethodName(@RequestBody String entity) {        
        return entity;
    }
    
    
}
