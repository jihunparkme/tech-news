package com.technews.aggregate.about.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutController {

    @GetMapping
    public String about() {
        return "about";
    }
}
