package com.billboarding.Controller.Advertiser;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advertiser")
public class AdvertiserController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdvertiserDashboard() {
        return ResponseEntity.ok("Advertiser dashboard data");
    }

    @GetMapping("/billboards")
    public ResponseEntity<String> browseBillboards() {
        return ResponseEntity.ok("List of available billboards (Advertiser)");
    }
}