package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum NcSeries {
    E("E"), B("B");
    private final String value;
}
