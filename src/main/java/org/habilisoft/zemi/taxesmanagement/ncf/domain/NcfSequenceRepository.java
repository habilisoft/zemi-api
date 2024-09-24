package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NcfSequenceRepository extends JpaRepository<NcfSequence, NcfSequence.NcfSequenceId> {
    @Query("from NcfSequence s where s.id.ncfType = :ncfType and s.active = true and s.expirationDate >= current_date")
    Optional<NcfSequence> findByNcfType(NcfType ncfType);
}
