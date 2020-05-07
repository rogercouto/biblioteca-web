package br.com.uabrestingaseca.biblioteca.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public Map index(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("message", "Bem vindo a api de controle de biblioteca");
        return map;
    }

    @GetMapping("/info")
    public Map info(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("message", "Bem vindo a administração de controle de biblioteca");
        return map;
    }

}
