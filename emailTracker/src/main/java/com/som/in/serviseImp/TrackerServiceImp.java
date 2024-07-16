package com.som.in.serviseImp;


import com.som.in.dto.TrackerRequest;
import com.som.in.dto.TrackerResponse;
import com.som.in.entity.EmailRecipient;
import com.som.in.repository.EmailRecipientRepository;
import com.som.in.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrackerServiceImp implements TrackerService {

    @Autowired
    private EmailRecipientRepository emailRecipientRepository;

    public List<TrackerResponse> getEmailAnalytics(TrackerRequest request) {
        List<EmailRecipient> recipients = emailRecipientRepository.findByEmailAddressAndEmailSubject(request.emailId(), request.emailSubject());
        return recipients.stream().map(r -> new TrackerResponse(r.getEmailAddress(), r.getCreationDateTime(), !r.isFailedToSend(), r.getReadCount() > 0)).toList();
    }

    public void updateEmailAnalyticsInfo(String id) {
        try {
            EmailRecipient recipient = emailRecipientRepository.findById(id).get();
            recipient.setReadCount(recipient.getReadCount() + 1);
            emailRecipientRepository.save(recipient);
        } catch (Exception ex) {
            //TODO add logs
            System.out.println(ex);
        }
    }
}
