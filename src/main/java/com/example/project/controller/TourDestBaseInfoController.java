package com.example.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.model.Tourdestbaseinfo;
import com.example.project.repository.TourdestbaseinfoRepository;



@Controller
public class TourDestBaseInfoController {

    @Autowired
    TourdestbaseinfoRepository tourdestbaseinfoRepository;

    @GetMapping("/map/tourdestbaseinfo")
    public String map() {
        return "map/tourdestbaseinfo";
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                        * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    private double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }

    private double rad2deg(double rad) {
        return rad * 180 / Math.PI;
    }

    @GetMapping("/map/tourdestbaseinfoPoint")
    @ResponseBody

    public List<Tourdestbaseinfo> tourdestbaseinfoPoint(@RequestParam double lat, @RequestParam  double lng, @RequestParam  int km) {
        List<Tourdestbaseinfo> list = tourdestbaseinfoRepository.findAll();
            System.out.println(list + "입니다");
        for (int i = list.size() - 1; i >= 0; i--) {

            double dist = distance(lat, lng, list.get(i).getLat(), list.get(i).getLng());

            if (dist > km) {
                list.remove(i);
            }

        }
        return list;
    }

}
