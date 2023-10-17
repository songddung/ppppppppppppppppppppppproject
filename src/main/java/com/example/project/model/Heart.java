package com.example.project.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Heart {
    @Id
    @GeneratedValue

    private Long id;

    private String boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
