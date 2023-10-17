package com.example.project.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue

    Integer id;
    String name;
    String title;
    String content;
    Date creDate;
    
    @Column(columnDefinition = "integer defalte 0",nullable = false)
    Long view = 0L;
    
    
    
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @Column(columnDefinition = "integer defalte 0",nullable = false) 
    Long heart = 0L;
    
    
    
    
    
    
    
    
    @ManyToOne
    @JoinColumn(name="userId")
    User user;
                                    // 게시글 삭제시 댓글도 같이 삭제
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();

    
}
