package com.technews.aggregate.index;

import com.technews.aggregate.releases.springframework.service.ReleasesSchedulerService;
import com.technews.common.constant.Categories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final ReleasesSchedulerService releasesSchedulerService;

    @GetMapping({"", "/releases"})
    public String home(Model model) {
        model.addAttribute("categories", Categories.values());
        return "index";
    }
}