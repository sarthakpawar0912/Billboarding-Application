package com.billboarding.Repository.Advertiser;

import com.billboarding.Entity.Advertiser.FavoriteBillboard;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for favorites CRUD.
 */
public interface FavoriteBillboardRepository extends JpaRepository<FavoriteBillboard, Long> {

    // Get all favorites of an advertiser
    List<FavoriteBillboard> findByAdvertiser(User advertiser);

    // Check if a billboard is already favorited by this advertiser
    boolean existsByAdvertiserAndBillboard(User advertiser, Billboard billboard);

    // Remove a favorite by advertiser + billboard
    void deleteByAdvertiserAndBillboard(User advertiser, Billboard billboard);

    // Optional: find a specific favorite
    Optional<FavoriteBillboard> findByAdvertiserAndBillboard(User advertiser, Billboard billboard);

}