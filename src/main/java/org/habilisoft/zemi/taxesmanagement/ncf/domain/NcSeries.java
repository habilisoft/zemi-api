package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NcSeries {
    E("E"), B("B");
    private final String value;

    static NcSeries of(String value) {
        return Arrays.stream(NcSeries.values())
                .filter(ncSeries -> ncSeries.value.equals(value)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid NcSeries value"));
    }
}
