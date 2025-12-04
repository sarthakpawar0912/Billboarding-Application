package com.billboarding.Controller.Owner;

import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.User;
import com.billboarding.Services.BookingService.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/bookings")
public class OwnerBookingController {

    private final BookingService bookingService;

    public OwnerBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1️⃣ All bookings for owner
    @GetMapping
    public ResponseEntity<List<Booking>> getOwnerBookings(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.getBookingsForOwner(owner));
    }

    // 2️⃣ Pending booking requests
    @GetMapping("/requests")
    public ResponseEntity<List<Booking>> getPendingRequests(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.getPendingRequests(owner));
    }

    // 3️⃣ Approve booking (OWNER)
    @PostMapping("/{id}/approve")
    public ResponseEntity<Booking> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.approveBooking(id));
    }

    // 4️⃣ Reject booking (OWNER)
    @PostMapping("/{id}/reject")
    public ResponseEntity<Booking> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    // 5️⃣ Upcoming bookings
    @GetMapping("/upcoming")
    public ResponseEntity<List<Booking>> getUpcoming(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.getUpcomingBookings(owner));
    }

    // 6️⃣ Completed bookings
    @GetMapping("/completed")
    public ResponseEntity<List<Booking>> getCompleted(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.getCompletedBookings(owner));
    }
}
