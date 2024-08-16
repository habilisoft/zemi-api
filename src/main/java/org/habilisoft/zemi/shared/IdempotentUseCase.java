package org.habilisoft.zemi.shared;

public interface IdempotentUseCase<C extends Command, R> extends UseCase<C, R> {
    String idempotencyKey(C command);
}
