package com.billboarding.Controller.Advertiser;

import com.billboarding.DTO.Advertiser.AdvertiserDashboardResponse;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Services.Advertiser.AdvertiserFavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertiser/dashboards")
@RequiredArgsConstructor
public class AdvertiserDashboardController {

    private final AdvertiserFavoritesService favoritesService;

    // ===================== FAVORITES APIs ===================== //

    /**
     * ⭐ Add a billboard to favorites
     * <p>
     * POST /api/advertiser/favorites/{billboardId}
     */
    @PostMapping("/favorites/{billboardId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long billboardId,
                                         Authentication authentication) {

        User advertiser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(favoritesService.addFavorite(advertiser, billboardId));
    }

    /**
     * ⭐ Remove a billboard from favorites
     * <p>
     * DELETE /api/advertiser/favorites/{billboardId}
     */
    @DeleteMapping("/favorites/{billboardId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long billboardId,
                                            Authentication authentication) {

        User advertiser = (User) authentication.getPrincipal();
        favoritesService.removeFavorite(advertiser, billboardId);
        return ResponseEntity.ok("Removed from favorites");
    }

    /**
     * ⭐ Get all my favorite billboards
     * <p>
     * GET /api/advertiser/favorites
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<Billboard>> getFavorites(Authentication authentication) {
        User advertiser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(favoritesService.getMyFavoriteBillboards(advertiser));
    }

    // export to user as CSV

}
