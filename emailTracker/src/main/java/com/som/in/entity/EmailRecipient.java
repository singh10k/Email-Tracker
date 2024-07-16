package com.som.in.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "EmailRecipient")
@Data
public class EmailRecipient implements Serializable {
    @Id
    private String id;
    private String emailAddress;
    private String emailSubject;
    private String pixelId;
    private int readCount = 0;
    private boolean failedToSend;
    private LocalDate creationDateTime;

    public EmailRecipient() {
        // Default constructor
    }

    public EmailRecipient(String id, String emailAddress, String emailSubject, LocalDate creationDateTime) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.emailSubject = emailSubject;
        this.creationDateTime = creationDateTime;
    }
}
