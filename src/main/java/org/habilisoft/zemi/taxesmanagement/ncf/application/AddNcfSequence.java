package org.habilisoft.zemi.taxesmanagement.ncf.application;

import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcSeries;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfType;

import java.time.LocalDateTime;

public record AddNcfSequence(NcSeries series, NcfType ncfType, Long initialSequence, Long finalSequence, LocalDateTime time, String user) implements Command {
}
