package com.billboarding.Services.BookingService;

import com.billboarding.ENUM.BookingStatus;
import com.billboarding.ENUM.KycStatus;
import com.billboarding.ENUM.PaymentStatus;
import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Repository.BillBoard.BillboardRepository;
import com.billboarding.Repository.Booking.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BillboardRepository billboardRepository;

    public BookingService(BookingRepository bookingRepository, BillboardRepository billboardRepository) {
        this.bookingRepository = bookingRepository;
        this.billboardRepository = billboardRepository;
    }

    // ================= CREATE BOOKING =================

    public Booking createBooking(User advertiser,
                                 Long billboardId,
                                 LocalDate startDate,
                                 LocalDate endDate) {

        if (advertiser.getKycStatus() != KycStatus.APPROVED)
            throw new RuntimeException("Your KYC must be APPROVED");

        Billboard billboard = billboardRepository.findById(billboardId)
                .orElseThrow(() -> new RuntimeException("Billboard not found"));

        boolean conflict = bookingRepository
                .existsByBillboardAndStatusInAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
                        billboard,
                        List.of(BookingStatus.PENDING, BookingStatus.APPROVED),
                        startDate,
                        endDate
                );

        if (conflict)
            throw new RuntimeException("Billboard is already booked for these dates");

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double totalPrice = billboard.getPricePerDay() * days;

        Booking booking = Booking.builder()
                .advertiser(advertiser)
                .billboard(billboard)
                .startDate(startDate)
                .endDate(endDate)
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.NOT_PAID)
                .build();

        return bookingRepository.save(booking);
    }

    // ================= ADVERTISER FEATURES =================

    public List<Booking> getMyBookings(User advertiser) {
        return bookingRepository.findByAdvertiser(advertiser);
    }

    public Booking cancelMyBooking(User advertiser, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getAdvertiser().getId().equals(advertiser.getId()))
            throw new RuntimeException("Not your booking!");

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new RuntimeException("Only PENDING bookings can be cancelled");

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    // ================= OWNER FEATURES =================

    public List<Booking> getPendingRequests(User owner) {
        return bookingRepository.findByBillboard_OwnerAndStatus(owner, BookingStatus.PENDING);
    }

    public Booking approveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new RuntimeException("Only pending requests can be approved");

        booking.setStatus(BookingStatus.APPROVED);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new RuntimeException("Only pending requests can be rejected");

        booking.setStatus(BookingStatus.REJECTED);
        booking.setPaymentStatus(PaymentStatus.FAILED);

        return bookingRepository.save(booking);
    }

    public List<Booking> getUpcomingBookings(User owner) {
        return bookingRepository.findByBillboard_OwnerAndStatusAndStartDateGreaterThanEqual(
                owner, BookingStatus.APPROVED, LocalDate.now()
        );
    }

    public List<Booking> getCompletedBookings(User owner) {
        return bookingRepository.findByBillboard_OwnerAndStatusAndEndDateLessThan(
                owner, BookingStatus.APPROVED, LocalDate.now()
        );
    }

    public List<Booking> getBookingsForOwner(User owner) {
        return bookingRepository.findByBillboard_Owner(owner);
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

}
