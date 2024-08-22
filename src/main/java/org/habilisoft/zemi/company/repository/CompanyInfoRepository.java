package org.habilisoft.zemi.company.repository;

import org.habilisoft.zemi.company.domain.CompanyInformation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 19/8/24.
 */
public interface CompanyInfoRepository extends JpaRepository<CompanyInformation, Long> {
}
