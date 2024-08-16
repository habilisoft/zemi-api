package org.habilisoft.zemi.shared.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.IdempotentUseCase;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@Log
@Aspect
@Configuration
@RequiredArgsConstructor
class UseCaseAspect {
    private final ObjectMapper objectMapper;
    private final UseCaseExecutionRepository repository;
    @Around("execution(* org.habilisoft.zemi.shared.UseCase+.execute(..)) && args(command)")
    Object logUseCaseExecution(ProceedingJoinPoint joinPoint, Command command) throws Throwable {
        long start = System.currentTimeMillis();
        String userName = command.user();
        String useCaseName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String serializedCommand = objectMapper.writeValueAsString(command);
        LocalDateTime timeOfExecution = command.time();

        UseCaseExecution useCaseExecution = UseCaseExecution.newExecutionOf(useCaseName, serializedCommand, userName, timeOfExecution);
        var idempotencyKey = getIdempotencyKey(joinPoint, command);
        idempotencyKey.ifPresent(useCaseExecution::setIdempotencyKey);
        try {
            idempotencyKey.ifPresent(key -> verifyIdempotencyKey(useCaseExecution, key));
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            useCaseExecution.succeeded(objectMapper.writeValueAsString(proceed), executionTime);
            log.info("UseCase %s executed in %dms".formatted(useCaseName, executionTime));
            return proceed;
        } catch (Throwable e) {
            useCaseExecution.failed(e.getMessage());
            throw e;
        } finally {
            repository.save(useCaseExecution);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Optional<String> getIdempotencyKey(ProceedingJoinPoint joinPoint, Command command) {
        if (joinPoint.getTarget() instanceof IdempotentUseCase idempotentUseCase) {
            return Optional.of(idempotentUseCase.idempotencyKey(command));
        } else {
            return Optional.empty();
        }
    }

    private void verifyIdempotencyKey(UseCaseExecution useCaseExecution, String idempotencyKey) {
        String useCaseName = useCaseExecution.getUseCaseName();
        repository.findByUseCaseNameAndIdempotencyKey(useCaseName, idempotencyKey)
                .ifPresent(_ -> {
                    throw new IllegalArgumentException("UseCase %s with idempotency key %s already executed".formatted(useCaseName, idempotencyKey));
                });
    }
}
