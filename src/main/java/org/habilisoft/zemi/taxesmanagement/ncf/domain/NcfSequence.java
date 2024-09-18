package org.habilisoft.zemi.taxesmanagement.ncf.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.habilisoft.zemi.user.AuditableProperties;
import org.habilisoft.zemi.user.Username;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ncf_sequences")
@EqualsAndHashCode(of = "id")
public class NcfSequence implements Persistable<NcfSequence.NcfSequenceId> {
    @EmbeddedId
    private NcfSequenceId id;
    private Long initialSequence;
    private Long finalSequence;
    private Long currentSequence;
    private boolean active;
    @Version
    private Long version;
    @Column(name = "ncf_series")
    @Enumerated(EnumType.STRING)
    private NcSeries series;
    @Transient
    private boolean isNew;

    @Embedded
    @AttributeOverride(name = "createdBy.value", column = @Column(name = "created_by"))
    @AttributeOverride(name = "updatedBy.value", column = @Column(name = "updated_by"))
    private AuditableProperties auditableProperties;

    public NcfType getNcfType() {
        return id.ncfType;
    }

    public static NcfSequence newSequence(NcfType ncfType, NcSeries series, Long initialSequence, Long finalSequence, LocalDateTime createdAt, Username createdBy) {
        NcfSequence ncfSequence = new NcfSequence();
        ncfSequence.id = NcfSequenceId.of(ncfType, createdAt);
        ncfSequence.initialSequence = initialSequence;
        ncfSequence.finalSequence = finalSequence;
        ncfSequence.currentSequence = initialSequence;
        ncfSequence.active = true;
        ncfSequence.series = series;
        ncfSequence.isNew = true;
        ncfSequence.auditableProperties = AuditableProperties.of(createdAt, createdBy);
        return ncfSequence;
    }

    public Ncf increment(LocalDateTime createdAt, Username createdBy) {
        if (currentSequence >= finalSequence)
            throw new NcfExhaustedException(getNcfType());
        Ncf ncf = new Ncf(String.format("%s%s%08d", series.getValue(), getNcfType().getValue(), currentSequence++));
        if (currentSequence >= finalSequence)
            active = false;
        auditableProperties = auditableProperties.update(createdAt, createdBy);
        return ncf;
    }
    @Data
    @Embeddable
    static class NcfSequenceId implements Serializable {
        @Enumerated(EnumType.STRING)
        private NcfType ncfType;
        private LocalDateTime date;

        static NcfSequenceId of(NcfType ncfType, LocalDateTime date) {
            NcfSequenceId id = new NcfSequenceId();
            id.ncfType = ncfType;
            id.date = date;
            return id;
        }
    }

}
