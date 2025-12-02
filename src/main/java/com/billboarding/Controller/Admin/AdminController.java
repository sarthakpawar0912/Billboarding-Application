package com.billboarding.Controller.Admin;

import com.billboarding.Entity.User;
import com.billboarding.Services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Admin dashboard data");
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("List of all users (only admin can see this)");
    }


    private final AdminService adminService;

    // ⭐ 1. Get all users with PENDING KYC
    @GetMapping("/kyc/pending")
    public ResponseEntity<List<User>> getPendingKycUsers(){
        return ResponseEntity.ok(adminService.getPendingKycUsers());
    }
    // ⭐ 2. Approve user KYC
    @PostMapping("/kyc/approve/{userId}")
    public ResponseEntity<String> approveKyc(@PathVariable Long userId){
        adminService.approveKyc(userId);
        return ResponseEntity.ok("KYC approved successfully");
    }
    // ⭐ 3. Reject user KYC
    @PostMapping("/kyc/reject/{userId}")
    public ResponseEntity<String> rejectKyc(@PathVariable Long userId, @RequestParam String reason) {
        adminService.rejectKyc(userId, reason);
        return ResponseEntity.ok("KYC rejected :"+ reason);
    }


}