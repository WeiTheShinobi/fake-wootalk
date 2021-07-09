package com.weitheshinobi.fakewootalk.websocket.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SecretReceiver {

    @PostMapping("/sendSecret")
    public void receiveSecret(@RequestParam String secret, HttpSession httpSession) {
        if(secret != "") httpSession.setAttribute("secret", secret);
    }

}
