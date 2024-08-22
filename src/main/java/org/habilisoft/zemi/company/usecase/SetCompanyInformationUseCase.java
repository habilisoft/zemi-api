package org.habilisoft.zemi.company.usecase;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.company.command.SetCompanyInformation;
import org.habilisoft.zemi.company.domain.CompanyInformation;
import org.habilisoft.zemi.company.repository.CompanyInfoRepository;
import org.habilisoft.zemi.shared.UseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetCompanyInformationUseCase implements UseCase<SetCompanyInformation, CompanyInformation> {
    private final CompanyInfoRepository companyRepository;

    @Override
    public CompanyInformation execute(SetCompanyInformation command) {
        CompanyInformation information = companyRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new CompanyInformation());
        information.setName(command.name());
        command.address().ifPresent(information::setAddress);
        command.phone().ifPresent(information::setPhone);
        command.email().ifPresent(information::setEmail);
        command.website().ifPresent(information::setWebsite);
        command.logo().ifPresent(information::setLogo);
        command.document().ifPresent(information::setDocument);
        return companyRepository.save(information);
    }
}
