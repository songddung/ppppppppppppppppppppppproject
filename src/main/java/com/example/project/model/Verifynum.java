package com.example.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Verifynum {
    @Id
    @GeneratedValue
    long id;
    String num;
    String email;
}