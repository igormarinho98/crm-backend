package com.crm.repository;

import com.crm.dto.DealDetailsDTO;
import java.util.Optional;

public interface DealRepositoryCustom {
    Optional<DealDetailsDTO> findDealWithRelations(String id);
}
