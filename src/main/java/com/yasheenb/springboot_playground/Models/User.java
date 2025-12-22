package com.yasheenb.springboot_playground.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"User\"") // user is a keyword in H2.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long userID;

    public String username;

    public String email;
}
