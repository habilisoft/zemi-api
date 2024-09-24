package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;

@Getter
public class NcfSequenceExpiredException extends RuntimeException {
    private final NcfType ncfType;
    public NcfSequenceExpiredException(NcfType ncfType) {
        super("The Ncf %s sequence has expired".formatted(ncfType));
        this.ncfType = ncfType;
    }
}
