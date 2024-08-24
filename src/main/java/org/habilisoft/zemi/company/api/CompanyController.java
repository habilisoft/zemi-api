package org.habilisoft.zemi.company.api;

import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.company.application.SetCompanyInformation;
import org.habilisoft.zemi.company.application.SetCompanyInformationUseCase;
import org.habilisoft.zemi.company.domain.CompanyInfoRepository;
import org.habilisoft.zemi.company.domain.CompanyInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
