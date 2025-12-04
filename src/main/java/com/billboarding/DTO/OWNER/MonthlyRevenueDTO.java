package com.billboarding.DTO.OWNER;

import lombok.Data;

@Data
public class MonthlyRevenueDTO {

    private int year;
    private int month;          // 1–12
    private double totalRevenue;
}
