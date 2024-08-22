package org.habilisoft.zemi.company;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.company.command.SetCompanyInformation;
import org.habilisoft.zemi.company.domain.CompanyInformation;
import org.habilisoft.zemi.company.repository.CompanyInfoRepository;
import org.habilisoft.zemi.company.usecase.SetCompanyInformationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {
    private final SetCompanyInformationUseCase useCase;
    private final CompanyInfoRepository repo;

    @GetMapping
    public ResponseEntity<CompanyInformation> getCompanyInformation() {
        return repo.findAll()
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void setCompanyInformation(@RequestBody SetCompanyInformation request) {
        useCase.execute(request);
    }

}
