package com.som.in.controller;


import com.som.in.dto.EmailRequest;
import com.som.in.dto.StandardResponse;
import com.som.in.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/email/send")
    public ResponseEntity<StandardResponse> sendEmail(@RequestBody EmailRequest request) {
        return new ResponseEntity<>(emailService.sendEmail(request), HttpStatus.OK);
    }
}
