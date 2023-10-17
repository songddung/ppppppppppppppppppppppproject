package com.example.project.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue

    int id;
    String content;
    Date crDate;

    @ManyToOne
    User user;

    @ManyToOne
    Board board;
}
