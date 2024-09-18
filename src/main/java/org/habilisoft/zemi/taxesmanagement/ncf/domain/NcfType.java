package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum NcfType {
    FISCAL_CREDIT("01"), FINAL_CONSUMER("02"), GUBERNATORIAL("15"), SPECIAL("14");

    private final String value;
}
