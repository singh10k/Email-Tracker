package com.som.in.service;

import com.som.in.dto.EmailRequest;
import com.som.in.dto.StandardResponse;

public interface EmailService {
    StandardResponse sendEmail(EmailRequest request);
}
