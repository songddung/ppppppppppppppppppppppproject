package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {

    
}
