package org.habilisoft.zemi.taxesmanagement.api;

import jakarta.validation.constraints.NotNull;
import org.habilisoft.zemi.taxesmanagement.domain.NcfType;

interface Requests {
    record ChangeNcfType(@NotNull NcfType ncfType) {}
}
