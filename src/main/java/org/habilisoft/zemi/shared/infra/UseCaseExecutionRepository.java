package org.habilisoft.zemi.shared.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UseCaseExecutionRepository extends JpaRepository<UseCaseExecution, Long> {
    Optional<UseCaseExecution> findByUseCaseNameAndIdempotencyKey(String useCaseName, String idempotencyKey);
}
