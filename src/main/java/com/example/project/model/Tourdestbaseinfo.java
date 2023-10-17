package com.example.project.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Tourdestbaseinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String tourdestnm;
    private String opertaionrulenm;
    private String addrroad;
    private String addrjibun;
    private Double lat;
    private Double lng;
    private long area;
    private String publicconvfcinfo;
    private String accominfo;
    private String sportsenterfcltinfo;
    private String recreationalcultfcltinfo;
    private String hospitalityfcitinfo;
    private String supportfcltinfo;
    private Date dsgndate;
    private int capacity;
    private int availparkingcnt;
    private String tourdestintro;
    private String mngagctel;
    private String mngagcnm;
    private Date synctime;
}
