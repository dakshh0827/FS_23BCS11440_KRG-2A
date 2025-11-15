package com.example.postapp.controller;

import com.example.postapp.model.Post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    @PostMapping("/posts")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
    System.out.println("Received: " + post.getTitle() + " | " + post.getBody());

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Post saved successfully!");
    } 
}
