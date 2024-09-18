package org.habilisoft.zemi.taxesmanagement.ncf.application;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.shared.UseCase;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfSequence;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfSequenceActiveException;
import org.habilisoft.zemi.taxesmanagement.ncf.domain.NcfSequenceRepository;
import org.habilisoft.zemi.user.Username;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNcfSequenceUseCase implements UseCase<AddNcfSequence, Void> {
    private final NcfSequenceRepository ncfSequenceRepository;

    @Override
    public Void execute(AddNcfSequence addNcfSequence) {
        ncfSequenceRepository.findByNcfType(addNcfSequence.ncfType())
                .ifPresent(_ -> {
                    throw new NcfSequenceActiveException(addNcfSequence.ncfType());
                });
        NcfSequence ncfSequence = NcfSequence.newSequence(
                addNcfSequence.ncfType(),
                addNcfSequence.series(),
                addNcfSequence.initialSequence(),
                addNcfSequence.finalSequence(),
                addNcfSequence.time(),
                Username.of(addNcfSequence.user())
        );
        ncfSequenceRepository.save(ncfSequence);
        return null;
    }
}
