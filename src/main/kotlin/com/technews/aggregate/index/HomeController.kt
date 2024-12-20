package com.technews.aggregate.index

import com.technews.aggregate.posts.constant.JavaBlogsSubject
import com.technews.aggregate.posts.constant.SpringBlogsSubject
import com.technews.aggregate.releases.constant.Categories
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController {

    @GetMapping
    fun home(model: Model): String {
        model.addAttribute("categories", Categories.entries.toTypedArray())
        return "index"
    }

    @GetMapping("/blog/spring")
    fun springPosts(model: Model): String {
        model.addAttribute("categories", SpringBlogsSubject.entries.toTypedArray())
        return "blog/spring"
    }

    @GetMapping("/blog/java")
    fun JavaPosts(model: Model): String {
        model.addAttribute("categories", JavaBlogsSubject.entries.toTypedArray())
        return "blog/java"
    }
}