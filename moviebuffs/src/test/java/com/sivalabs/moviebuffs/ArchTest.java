package com.sivalabs.moviebuffs;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchTest {

    JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.sivalabs.moviebuffs");

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        noClasses()
                .that()
                .resideInAnyPackage("com.sivalabs.moviebuffs.core.service..")
                .or()
                .resideInAnyPackage("com.sivalabs.moviebuffs.core.repository..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("com.sivalabs.moviebuffs.web..")
                .because("Services and repositories should not depend on web layer")
                .check(importedClasses);


    }

    @Test
    void shouldNotUseFieldInjection() {
        noFields().should().beAnnotatedWith(Autowired.class).check(importedClasses);
    }

    @Test
    void shouldFollowLayeredArchitecture() {
        layeredArchitecture()
                .layer("Config").definedBy("..config..")
                .layer("Jobs").definedBy("..jobs..")
                .layer("Importer").definedBy("..importer..")
                .layer("Web").definedBy("..web..")
                .layer("Service").definedBy("..service..")
                .layer("Persistence").definedBy("..repository..")

                .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Config", "Importer", "Jobs", "Web")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
        .check(importedClasses);
    }

    @Test
    void shouldFollowNamingConvention() {
        classes().that().resideInAPackage("com.sivalabs.moviebuffs.core.repository")
                .should().haveSimpleNameEndingWith("Repository")
                .check(importedClasses);

        classes().that().resideInAPackage("com.sivalabs.moviebuffs.core.service")
                .should().haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }
}
