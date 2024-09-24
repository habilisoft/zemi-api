package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;

@Getter
public class NcfSequenceInactiveException extends RuntimeException{
    private final NcfType ncfType;

    public NcfSequenceInactiveException(NcfType ncfType) {
        super("The Ncf %s sequence is inactive".formatted(ncfType));
        this.ncfType = ncfType;
    }
}
