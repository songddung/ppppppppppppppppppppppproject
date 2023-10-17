package com.example.project.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.User;





@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByPwdAndEmail(String pwd, String email);

    User findByPwd(String pwd);
    User findByEmail(String email);
    User findByName(String name);
    
}
