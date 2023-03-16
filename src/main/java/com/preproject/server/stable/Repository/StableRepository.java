package com.preproject.server.stable.Repository;

import com.preproject.server.stable.entity.Stable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StableRepository extends JpaRepository<Stable, Long> {
}
