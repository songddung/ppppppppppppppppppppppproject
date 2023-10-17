package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Verifynum;





@Repository
public interface VerifynumRepository extends JpaRepository<Verifynum, Long>{
    Verifynum findByNum(String num);
    Verifynum findByEmail(String email);
}
