package com.billboarding.Entity.Advertiser;

import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity that represents a "favorite" connection between
 * an Advertiser user and a Billboard.
 *
 * One advertiser can have many favorites,
 * one billboard can be favorited by many advertisers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "favorite_billboards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_fav_advertiser_billboard",
                        columnNames = {"advertiser_id", "billboard_id"}
                )
        }
)
public class FavoriteBillboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Who favorited this board (must be ADVERTISER)
    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false)
    private User advertiser;

    // 🔹 Which billboard is favorited
    @ManyToOne
    @JoinColumn(name = "billboard_id", nullable = false)
    private Billboard billboard;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
