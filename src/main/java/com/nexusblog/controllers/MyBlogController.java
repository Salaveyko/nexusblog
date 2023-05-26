package com.nexusblog.controllers;

import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.dao.service.interfaces.PostsService;
import com.nexusblog.persistence.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/blog")
public class MyBlogController {

    private final PostsService postsService;

    @Autowired
    public MyBlogController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("")
    public String mainBlog(Model model) {
        model.addAttribute("posts", postsService.getAll());

        return "index.html";
    }

    @GetMapping("/myblog")
    public String selfBlog(Model model) throws UserPrincipalNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            model.addAttribute("authUserId", ((User) principal).getId());
        }
        model.addAttribute("posts", postsService.getMyPosts());

        return "index.html";
    }

    @GetMapping("/remove/{id}")
    public String removePost(@PathVariable("id") Long id) {
        postsService.removeById(id);

        return "redirect:/blog/myblog";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable("id") Long id, Model model) throws PostNotFoundException {
        model.addAttribute("post", postsService.getPostById(id));

        return "postForm.html";
    }

    @GetMapping("/add")
    public String addPost(Model model) {
        PostDto postDto = new PostDto();
        model.addAttribute("post", postDto);

        return "postForm.html";
    }

    @PutMapping("/update")
    public String updatePosts(@ModelAttribute("post") @Valid PostDto postDto,
                              BindingResult result,
                              Model model) throws UserPrincipalNotFoundException {

        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
            return "/edit";
        }

        postsService.save(postDto);

        return "redirect:/blog/myblog";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
