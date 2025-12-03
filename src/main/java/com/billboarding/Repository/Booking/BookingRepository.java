package com.billboarding.Repository.Booking;

import com.billboarding.ENUM.BookingStatus;
import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByAdvertiser(User advertiser);

    // ⭐ FIXED METHOD NAME ⭐
    boolean existsByBillboardAndStatusInAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
            Billboard billboard,
            List<BookingStatus> statuses,
            LocalDate start,
            LocalDate end
    );

    List<Booking> findByBillboard_Owner(User owner);

    List<Booking> findByBillboard_Id(Long billboardId);

    // For admin → filter bookings by status
    List<Booking> findByStatus(BookingStatus status);
}
