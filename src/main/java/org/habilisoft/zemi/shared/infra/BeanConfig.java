package org.habilisoft.zemi.shared.infra;

import org.habilisoft.zemi.shared.IdGenerator;
import org.habilisoft.zemi.shared.UseCase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

@Configuration
public class BeanConfig {
    @Bean
    BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor() {
        return registry -> {
            ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AssignableTypeFilter(UseCase.class));
            scanner.addIncludeFilter(new AssignableTypeFilter(IdGenerator.class));
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("org.habilisoft.zemi");
            for (BeanDefinition candidateComponent : candidateComponents) {
                try {
                    Class<?> aClass = Class.forName(candidateComponent.getBeanClassName());
                    registry.registerBeanDefinition(aClass.getSimpleName(), candidateComponent);
                } catch (ClassNotFoundException e) {
                    throw new BeansException("Failed to load class for bean registration", e) { };
                }
            }
        };
    }
}
