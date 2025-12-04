package com.billboarding.Controller.Advertiser;

import com.billboarding.DTO.Booking.CreateBookingRequest;
import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Services.Advertiser.AdvertiserFavoritesService;
import com.billboarding.Repository.BillBoard.BillboardRepository;
import com.billboarding.Services.BookingService.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertiser")
@RequiredArgsConstructor
public class advertiserController {

    private final BillboardRepository billboardRepository;
    private final BookingService bookingService;
    private final AdvertiserFavoritesService favoritesService;

    /**
     * Simple advertiser dashboard text
     */
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Advertiser dashboard - bookings & available billboards");
    }

    /**
     * Browse all available billboards (available = true)
     */
    @GetMapping("/billboards")
    public ResponseEntity<List<Billboard>> getAvailableBillboards() {
        return ResponseEntity.ok(billboardRepository.findByAvailableTrue());
    }

    /**
     * Create booking for a billboard
     */
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(
            @RequestBody CreateBookingRequest req,
            Authentication authentication) {

        User advertiser = (User) authentication.getPrincipal();

        Booking booking = bookingService.createBooking(
                advertiser,
                req.getBillboardId(),
                req.getStartDate(),
                req.getEndDate()
        );

        return ResponseEntity.ok(booking);
    }

    /**
     * Get all bookings of logged-in advertiser
     */
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> myBookings(Authentication authentication) {
        User advertiser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getMyBookings(advertiser));
    }

    /**
     * Cancel my booking (only PENDING)
     */
    @PostMapping("/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        User advertiser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.cancelMyBooking(advertiser, id));
    }

    // ===================== FAVORITES APIs ===================== //

    /**
     * ⭐ Add a billboard to favorites
     */
    @PostMapping("/favorites/{billboardId}")
    public ResponseEntity<?> addFavorite(
            @PathVariable Long billboardId,
            Authentication auth) {

        User advertiser = (User) auth.getPrincipal();
        return ResponseEntity.ok(favoritesService.addFavorite(advertiser, billboardId));
    }

    /**
     * ⭐ Remove a favorite
     */
    @DeleteMapping("/favorites/{billboardId}")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Long billboardId,
            Authentication auth) {

        User advertiser = (User) auth.getPrincipal();
        favoritesService.removeFavorite(advertiser, billboardId);
        return ResponseEntity.ok("Removed from favourites");
    }

    /**
     * ⭐ Get all my favorite billboards
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<Billboard>> getFavorites(Authentication auth) {
        User advertiser = (User) auth.getPrincipal();
        return ResponseEntity.ok(favoritesService.getMyFavoriteBillboards(advertiser));
    }

    // export to user as CSV


}
