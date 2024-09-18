package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public class NcfSequenceActiveException extends DomainException {
    private final NcfType ncfType;
    public NcfSequenceActiveException(NcfType ncfType) {
        super("A Ncf %s sequence is active".formatted(ncfType));
        this.ncfType = ncfType;
    }
}
