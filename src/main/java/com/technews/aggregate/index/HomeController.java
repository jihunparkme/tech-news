package com.technews.aggregate.index;

import com.technews.aggregate.posts.java.constant.JavaBlogsSubject;
import com.technews.aggregate.posts.spring.constant.SpringBlogsSubject;
import com.technews.aggregate.releases.constant.Categories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categories", Categories.values());
        return "index";
    }

    @GetMapping("/blog/spring")
    public String springPosts(Model model) {
        model.addAttribute("categories", SpringBlogsSubject.values());
        return "blog/spring";
    }

    @GetMapping("/blog/java")
    public String JavaPosts(Model model) {
        model.addAttribute("categories", JavaBlogsSubject.values());
        return "blog/java";
    }
}