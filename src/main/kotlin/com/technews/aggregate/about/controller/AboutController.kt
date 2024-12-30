package com.technews.aggregate.about.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/about")
class AboutController {
    @GetMapping
    fun about() = "about"
}
