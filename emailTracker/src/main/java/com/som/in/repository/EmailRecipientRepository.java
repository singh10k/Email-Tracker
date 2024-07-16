package com.som.in.repository;


import com.som.in.entity.EmailRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRecipientRepository extends JpaRepository<EmailRecipient,String> {
    List<EmailRecipient> findByEmailAddressAndEmailSubject(String emailAddress, String emailSubject);

}
