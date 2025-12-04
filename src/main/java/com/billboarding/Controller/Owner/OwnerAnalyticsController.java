package com.billboarding.Controller.Owner;

import com.billboarding.DTO.OWNER.OwnerAnalyticsResponse;
import com.billboarding.Entity.User;
import com.billboarding.Services.Owner.OwnerAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/analytics")
@RequiredArgsConstructor
public class OwnerAnalyticsController {

    private final OwnerAnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<OwnerAnalyticsResponse> getAnalytics(Authentication auth) {

        User owner = (User) auth.getPrincipal();

        OwnerAnalyticsResponse res = analyticsService.getAnalytics(owner);

        return ResponseEntity.ok(res);
    }
}
