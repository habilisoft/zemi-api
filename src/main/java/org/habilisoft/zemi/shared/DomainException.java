package org.habilisoft.zemi.shared;

public abstract class DomainException extends RuntimeException{
    protected DomainException(String message) {
        super(message);
    }
}
