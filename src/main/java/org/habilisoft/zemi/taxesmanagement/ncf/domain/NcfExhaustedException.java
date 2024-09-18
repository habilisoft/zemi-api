package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;
@Getter
public final class NcfExhaustedException extends DomainException {
    private final NcfType ncfType;
    public NcfExhaustedException(NcfType ncfType) {
        super("Ncf %s exhausted".formatted(ncfType));
        this.ncfType = ncfType;
    }
}
