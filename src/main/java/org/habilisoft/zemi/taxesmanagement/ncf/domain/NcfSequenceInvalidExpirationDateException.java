package org.habilisoft.zemi.taxesmanagement.ncf.domain;

public class NcfSequenceInvalidExpirationDateException extends RuntimeException{
    public NcfSequenceInvalidExpirationDateException() {
        super("The expiration date must be greater than the current date");
    }
}
