package ru.bratushkadan.bratushkadan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryParam {

    @GetMapping("/param")
    public String getQueryParam(@RequestParam("name") String name) {
        return "Hello, " + name + "!";
    }

}
