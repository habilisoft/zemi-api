package org.habilisoft.zemi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ZemiApiApplicationTests extends AbstractIt {
    ApplicationModules modules = ApplicationModules.of(ZemiApiApplication.class);

    @Test
    @DisplayName("Verify modules circular dependencies")
    void verifyModules() {
        modules.verify();
    }

    @Test
    void writeDocumentationSnippets() {
        new Documenter(modules)
                .writeModulesAsPlantUml();
        Assertions.assertTrue(true);
    }

}
