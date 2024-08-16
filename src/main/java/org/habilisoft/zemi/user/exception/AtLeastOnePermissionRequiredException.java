package org.habilisoft.zemi.user.exception;

import lombok.Getter;
import org.habilisoft.zemi.shared.DomainException;

@Getter
public final class AtLeastOnePermissionRequiredException extends DomainException {
    public AtLeastOnePermissionRequiredException() {
        super("At least one permission is required");
    }
}
