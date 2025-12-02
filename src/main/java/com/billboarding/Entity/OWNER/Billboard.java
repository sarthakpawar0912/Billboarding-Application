package com.billboarding.Entity.OWNER;

import com.billboarding.ENUM.BillboardType;
import com.billboarding.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "billboards")
public class Billboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String location;

    // ⭐ GEO COORDINATES
    private Double latitude;
    private Double longitude;

    private Double pricePerDay;
    private String size;

    private boolean available;

    // ⭐ BILLBOARD TYPE
    @Enumerated(EnumType.STRING)
    @Column(name = "billboard_type", nullable = false)
    private BillboardType type;
    // Owner
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.available = true;
    }

    // ⭐ MULTIPLE IMAGE PATHS
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "billboard_images", joinColumns = @JoinColumn(name = "billboard_id"))
    @Column(name = "image_path", length = 1024)
    private List<String> imagePaths = new ArrayList<>();
}
