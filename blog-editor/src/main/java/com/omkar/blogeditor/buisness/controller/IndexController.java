package com.omkar.blogeditor.buisness.controller;

import com.omkar.blogeditor.buisness.service.IndexService;
import com.omkar.blogeditor.infra.model.BaseResponse;
import com.omkar.blogeditor.infra.model.request.LoginRequest;
import com.omkar.blogeditor.infra.model.request.RegisterRequest;
import com.omkar.blogeditor.infra.model.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class IndexController {
    @Autowired
    private IndexService indexService;

    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterRequest request) {return indexService.register(request);}

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {return indexService.login(request);}
}
