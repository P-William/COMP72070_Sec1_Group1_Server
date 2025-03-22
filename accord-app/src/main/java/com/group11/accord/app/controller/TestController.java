package com.group11.accord.app.controller;

import com.group11.accord.api.server.ServerEdit;
import com.group11.accord.api.user.Account;
import com.group11.accord.app.websockets.ServerPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ServerPublisher serverPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/test")
    public void test() {
        var owner = new Account(1L, "Username", "Bio", "pic");
        var edit = new ServerEdit(5L, "Test", owner);

        messagingTemplate.convertAndSend("/server/123", edit);
//        serverPublisher.publishServerEdit(edit);
    }


}
