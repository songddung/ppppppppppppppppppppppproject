package com.example.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.Point;

public interface PointRepository extends JpaRepository<Point, Long> {


}
