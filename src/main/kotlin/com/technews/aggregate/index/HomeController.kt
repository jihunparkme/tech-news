package com.technews.aggregate.index

import com.technews.aggregate.releases.constant.Categories
import com.technews.common.constant.JavaBlogsSubject
import com.technews.common.constant.SpringBlogsSubject
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun home(model: Model): String {
        model["categories"] = Categories.entries.toTypedArray()
        return "index"
    }

    @GetMapping("/blog/spring")
    fun springPosts(model: Model): String {
        model["categories"] = SpringBlogsSubject.entries.toTypedArray()
        return "blog/spring"
    }

    @GetMapping("/blog/java")
    fun javaPosts(model: Model): String {
        model["categories"] = JavaBlogsSubject.entries.toTypedArray()
        return "blog/java"
    }
}
