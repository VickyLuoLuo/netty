package com.lhl.netty.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class URLController {

    @RequestMapping("/")
    public String WebsocketChatClient(){
        return "/WebsocketChatClient";
    }


}
