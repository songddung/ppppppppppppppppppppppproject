package com.example.project.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "boards")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String pwd;
    private String email;
    private String name;
    private String birth;
    private String tel;
    private String credate;
    public Object get(String email2) {
        return null;
    }

    @OneToMany(mappedBy = "user")
    List<Board> boards = new ArrayList<>(); 
}