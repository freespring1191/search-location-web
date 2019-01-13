package me.freelife.loc.address.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Value("${kakao.javascript-key}")
    String javascriptKey;

    @GetMapping("/")
    public String list(Model model) {

        model.addAttribute("javascriptKey",javascriptKey);
        return "address/list";
    }

}
