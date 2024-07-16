package com.som.in.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "EmailAccount")
@Data
public class EmailAccount implements Serializable {

    @Id
    private String id;
    private String emailId;
    private String appPwd;
}
