package com.billboarding.Repository.BillBoard;
import com.billboarding.Entity.OWNER.Billboard;
import com.billboarding.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillboardRepository extends JpaRepository<Billboard, Long> {

    List<Billboard> findByOwner(User owner);
}