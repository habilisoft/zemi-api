package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NcfType {
    FISCAL_CREDIT("01"), FINAL_CONSUMER("02"), GUBERNATORIAL("15"), SPECIAL("14");

    private final String value;

    public static NcfType of(String value) {
        return Arrays.stream(NcfType.values()).filter(ncfType -> ncfType.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid NcfType value"));
    }
}
