package com.nexusblog.controllers;

import com.nexusblog.dto.CommentDto;
import com.nexusblog.dto.PostDto;
import com.nexusblog.persistence.service.interfaces.CommentService;
import com.nexusblog.persistence.service.interfaces.PostsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Controller
public class MyBlogController {

    private final PostsService postsService;
    private final CommentService commentService;

    @GetMapping("/")
    public String mainBlog(Model model) {
        model.addAttribute("posts", postsService.getAll());
        return "index";
    }

    @GetMapping("/myblog")
    public String selfBlog(Model model) {
        model.addAttribute("posts", postsService.getMyPosts());
        return "index";
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable("id") Long id, Model model){
        model.addAttribute("post", postsService.getPostById(id));
        model.addAttribute("comment", new CommentDto());

        return "post";
    }
    @PostMapping("/post/{id}")
    public String newComment(@PathVariable("id")Long id,
                             @ModelAttribute("comment")@Valid CommentDto comment,
                             BindingResult result,
                             Model model){

        if(result.hasErrors()){
            model.addAttribute("comment", comment);
            return "/post/" + id;
        }

        commentService.add(comment);

        return "redirect:/post/" + id;
    }

    @GetMapping("/post/{id}/remove")
    public String removePost(@PathVariable("id") Long id) {
        PostDto post = postsService.getPostById(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!post.getUsername().equals(username))
        {
            throw new AccessDeniedException("You do not have permission to delete the post");
        }
        postsService.removeById(id);

        return "redirect:/myblog";
    }

    @GetMapping("/post/{id}/edit")
    public String editPost(@PathVariable("id") Long id, Model model) {
        PostDto post = postsService.getPostById(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!post.getUsername().equals(username))
        {
            throw new AccessDeniedException("You do not have permission to edit the post");
        }

        model.addAttribute("post", post);

        return "postForm";
    }

    @GetMapping("/new-post")
    public String addPost(Model model) {
        PostDto postDto = new PostDto();
        model.addAttribute("post", postDto);

        return "postForm";
    }

    @PostMapping("/")
    public String updatePosts(@ModelAttribute("post") @Valid PostDto postDto,
                              BindingResult result,
                              Model model) throws UserPrincipalNotFoundException {

        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            return "postForm";
        }

        postsService.save(postDto);

        return "redirect:/myblog";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
