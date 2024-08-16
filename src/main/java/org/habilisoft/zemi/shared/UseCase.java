package org.habilisoft.zemi.shared;

public interface UseCase<C extends Command, R> {
    R execute(C command);
}
