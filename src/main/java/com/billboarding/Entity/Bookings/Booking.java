package com.billboarding.Entity.Bookings;

import com.billboarding.ENUM.BookingStatus;
import com.billboarding.ENUM.PaymentStatus;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Advertiser who booked this board
    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false)
    private User advertiser;

    // 🔹 Billboard that is booked
    @ManyToOne
    @JoinColumn(name = "billboard_id", nullable = false)
    private Billboard billboard;

    // 🔹 Booking period (inclusive)
    private LocalDate startDate;
    private LocalDate endDate;

    // 🔹 Total amount = pricePerDay * number_of_days
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.NOT_PAID;
        }
    }
}
