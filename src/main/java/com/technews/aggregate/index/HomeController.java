package com.technews.aggregate.index;

import com.technews.aggregate.releases.springframework.constant.Categories;
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
}