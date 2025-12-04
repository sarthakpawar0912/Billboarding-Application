package com.billboarding.Services.Advertiser;

import com.billboarding.Entity.Advertiser.FavoriteBillboard;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Repository.Advertiser.FavoriteBillboardRepository;
import com.billboarding.Repository.BillBoard.BillboardRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertiserFavoritesService {

    private final FavoriteBillboardRepository favoriteRepo;
    private final BillboardRepository billboardRepository;

    /**
     * Add favorite billboard
     */
    public FavoriteBillboard addFavorite(User advertiser, Long billboardId) {

        Billboard billboard = billboardRepository.findById(billboardId)
                .orElseThrow(() -> new RuntimeException("Billboard not found"));

        if (favoriteRepo.existsByAdvertiserAndBillboard(advertiser, billboard)) {
            throw new RuntimeException("This billboard is already in your favorites");
        }

        FavoriteBillboard fav = FavoriteBillboard.builder()
                .advertiser(advertiser)
                .billboard(billboard)
                .build();

        return favoriteRepo.save(fav);
    }

    /**
     * ⭐ FIX: DELETE must run inside a transaction
     */
    @Transactional
    public void removeFavorite(User advertiser, Long billboardId) {

        Billboard billboard = billboardRepository.findById(billboardId)
                .orElseThrow(() -> new RuntimeException("Billboard not found"));

        if (!favoriteRepo.existsByAdvertiserAndBillboard(advertiser, billboard)) {
            throw new RuntimeException("This billboard is not in your favorites");
        }

        favoriteRepo.deleteByAdvertiserAndBillboard(advertiser, billboard);
    }

    /**
     * Get all favorite billboards for advertiser
     */
    public List<Billboard> getMyFavoriteBillboards(User advertiser) {

        return favoriteRepo.findByAdvertiser(advertiser)
                .stream()
                .map(FavoriteBillboard::getBillboard)
                .collect(Collectors.toList());
    }
}
