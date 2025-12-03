package com.billboarding.Controller.Owner;

import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import com.billboarding.Services.BillBoard.BillboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/owner/billboards")
@RequiredArgsConstructor
public class OwnerController {

    private final BillboardService billboardService;

    // CREATE BILLBOARD
    @PostMapping
    public ResponseEntity<?> createBillboard(
            @RequestBody Billboard billboard,
            Authentication auth) {

        if (billboard.getType() == null)
            return ResponseEntity.badRequest().body("Billboard type is required");

        if (billboard.getLatitude() == null || billboard.getLongitude() == null)
            return ResponseEntity.badRequest().body("Latitude & Longitude are required");

        User owner = (User) auth.getPrincipal();

        Billboard saved = billboardService.createBillboard(owner.getId(), billboard);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Billboard>> getMyBillboards(Authentication auth) {
        User owner = (User) auth.getPrincipal();
        return ResponseEntity.ok(billboardService.getOwnerBillboards(owner.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBillboard(
            @PathVariable Long id,
            @RequestBody Billboard updated) {

        Billboard saved = billboardService.updateBillboard(id, updated);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBillboard(@PathVariable Long id) {
        billboardService.deleteBillboard(id);
        return ResponseEntity.ok("Billboard deleted");
    }

    @PostMapping("/{id}/upload-images")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> images,
            Authentication auth
    ) throws IOException {

        User owner = (User) auth.getPrincipal();
        Billboard billboard = billboardService.getBillboardById(id);

        if (billboard == null)
            return ResponseEntity.badRequest().body("Billboard not found");

        if (!billboard.getOwner().getId().equals(owner.getId()))
            return ResponseEntity.status(403).body("Not your billboard");

        List<String> paths = billboardService.saveImages(id, images);

        if (billboard.getImagePaths() == null)
            billboard.setImagePaths(new ArrayList<>());

        billboard.getImagePaths().addAll(paths);

        Billboard updated = billboardService.save(billboard);
        return ResponseEntity.ok(updated);
    }
}
