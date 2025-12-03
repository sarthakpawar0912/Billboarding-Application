package com.billboarding.Controller.Advertiser;


import com.billboarding.DTO.Booking.CreateBookingRequest;
import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Repository.BillBoard.BillboardRepository;
import com.billboarding.Services.BookingService.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/advertiser")
public class advertiserController {

    private final BillboardRepository billboardRepository;
    private final BookingService bookingService;

    public advertiserController(BillboardRepository billboardRepository, BookingService bookingService) {
        this.billboardRepository = billboardRepository;
        this.bookingService = bookingService;
    }

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
        List<Billboard> boards = billboardRepository.findByAvailableTrue();
        return ResponseEntity.ok(boards);
    }

    /**
     * Create booking for a billboard
     */
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest req,
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
     * Cancel my booking
     */
    @PostMapping("/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id,
                                                 Authentication authentication) {

        User advertiser = (User) authentication.getPrincipal();
        Booking cancelled = bookingService.cancelMyBooking(advertiser, id);
        return ResponseEntity.ok(cancelled);
    }
}