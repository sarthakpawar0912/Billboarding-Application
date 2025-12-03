package com.billboarding.Controller.Owner;

import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.User;
import com.billboarding.Services.BookingService.BookingService;
import com.billboarding.Services.BookingService.OwnerBooking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/bookings")
public class OwnerBookingController {
    private final OwnerBooking ownerBooking;

    public OwnerBookingController(OwnerBooking ownerBooking) {
        this.ownerBooking = ownerBooking;
    }

    /**
     * 1️⃣ Get all bookings for owner's billboards
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getOwnerBookings(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(ownerBooking.getBookingsForOwner(owner));
    }

    /**
     * 2️⃣ Approve booking
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Booking> approveBooking(
            @PathVariable Long id,
            Authentication auth) {

        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(ownerBooking.approveBooking(owner, id));
    }

    /**
     * 3️⃣ Reject booking
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Booking> rejectBooking(
            @PathVariable Long id,
            Authentication auth) {

        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(ownerBooking.rejectBooking(owner, id));
    }
}
