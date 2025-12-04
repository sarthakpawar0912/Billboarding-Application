package com.billboarding.Services.Owner;
import com.billboarding.DTO.OWNER.OwnerAnalyticsResponse;
import com.billboarding.Entity.Bookings.Booking;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.ENUM.BookingStatus;
import com.billboarding.Repository.BillBoard.BillboardRepository;
import com.billboarding.Repository.Booking.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerAnalyticsService {

    private final BillboardRepository billboardRepository;
    private final BookingRepository bookingRepository;

    public OwnerAnalyticsResponse getAnalytics(User owner) {

        List<Billboard> boards = billboardRepository.findByOwner(owner);
        List<Booking> bookings = bookingRepository.findByBillboard_Owner(owner);

        OwnerAnalyticsResponse response = new OwnerAnalyticsResponse();

        // TOTAL REVENUE
        double totalRevenue = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        response.setTotalRevenue(totalRevenue);

        // BILLBOARD REVENUE LIST
        response.setBillboardRevenues(
                boards.stream().map(b -> {
                    OwnerAnalyticsResponse.BillboardRevenue r =
                            new OwnerAnalyticsResponse.BillboardRevenue();

                    r.setBillboardId(b.getId());
                    r.setTitle(b.getTitle());

                    List<Booking> bb = bookingRepository.findByBillboard_Id(b.getId());

                    r.setTotalBookings(bb.size());

                    r.setRevenue(
                            bb.stream()
                                    .filter(x -> x.getStatus() == BookingStatus.APPROVED)
                                    .mapToDouble(Booking::getTotalPrice).sum()
                    );

                    r.setImageCount(b.getImagePaths().size());
                    return r;
                }).collect(Collectors.toList())
        );

        // TOP PERFORMING BILLBOARD
        response.setTopPerformingBillboard(
                response.getBillboardRevenues().stream()
                        .max(Comparator.comparingDouble(OwnerAnalyticsResponse.BillboardRevenue::getRevenue))
                        .orElse(null)
        );

        // MONTHLY REVENUE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, Double> monthly = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        b -> b.getCreatedAt().format(formatter),
                        Collectors.summingDouble(Booking::getTotalPrice)
                ));

        response.setMonthlyRevenues(monthly);

        return response;
    }
}
