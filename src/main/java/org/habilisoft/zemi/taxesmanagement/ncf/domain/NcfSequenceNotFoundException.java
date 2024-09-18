package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public class NcfSequenceNotFoundException extends DomainException {
    private final NcfType ncfType;
    public NcfSequenceNotFoundException(NcfType ncfType) {
        super("Ncf %s sequence not found".formatted(ncfType));
        this.ncfType = ncfType;
    }
}

