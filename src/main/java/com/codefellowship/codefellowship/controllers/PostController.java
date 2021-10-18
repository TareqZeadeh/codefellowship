package com.codefellowship.codefellowship.controllers;

import com.codefellowship.codefellowship.domain.ApplicationUser;
import com.codefellowship.codefellowship.domain.Post;
import com.codefellowship.codefellowship.infrastructure.ApplicationUserRepository;
import com.codefellowship.codefellowship.infrastructure.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PostController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    PostRepository postRepository;
    @GetMapping("/post")
    public String getProfile(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername()).orElse(null);
        if(applicationUser == null){
            return "error";
        }
        model.addAttribute("user",applicationUser);
        return "profile";
    }
    @PostMapping("/post")
    public RedirectView addPost(Model model, @RequestParam String body){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserRepository.findApplicationUserByUsername(userDetails.getUsername()).orElse(null);
        Post post = new Post(applicationUser,body);
        postRepository.save(post);
        model.addAttribute("user",applicationUser);
        assert applicationUser != null;
        model.addAttribute("posts" , applicationUser.getPosts());
        return new RedirectView("/profile");
    }
    }

