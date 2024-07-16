package com.som.in.repository;

import com.som.in.entity.EmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailServerRepository extends JpaRepository<EmailAccount,String> {
}
