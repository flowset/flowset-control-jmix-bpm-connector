/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui;

import io.jmix.bpmflowui.BpmFlowUiConfiguration;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.ActionsConfiguration;
import io.jmix.flowui.sys.ViewControllerDefinition;
import io.jmix.flowui.sys.ViewControllersConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {FlowuiConfiguration.class, BpmFlowUiConfiguration.class})
@PropertySource(name = "io.flowset.controljmixbpm.flowui", value = "classpath:/io/flowset/controljmixbpm/flowui/module.properties")
public class ControlJmixBpmFlowuiConfiguration {

    @Bean("fsc_ControlJmixBpmViewControllers")
    public ViewControllersConfiguration screens(final ApplicationContext applicationContext,
                                                final AnnotationScanMetadataReaderFactory metadataReaderFactory,
                                                final ControlJmixBpmUiProperties controlJmixBpmUiProperties) {
        final ViewControllersConfiguration viewControllers
                = new ViewControllersConfiguration(applicationContext, metadataReaderFactory);

        viewControllers.setBasePackages(Collections.singletonList("io.flowset.controljmixbpm.flowui"));

        List<String> excludedViews = controlJmixBpmUiProperties.getExcludedViews();
        if (CollectionUtils.isNotEmpty(excludedViews)) {
            List<ViewControllerDefinition> foundControllers = viewControllers.getViewControllers();
            List<ViewControllerDefinition> notExcludedControllers = foundControllers.stream()
                    .filter(controller -> !excludedViews.contains(controller.getId()))
                    .collect(Collectors.toList());

            viewControllers.setBasePackages(Collections.emptyList());
            viewControllers.setExplicitDefinitions(notExcludedControllers);
        }

        return viewControllers;
    }

    @Bean("fsc_ControlJmixBpmActions")
    public ActionsConfiguration actions(final ApplicationContext applicationContext,
                                        final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ActionsConfiguration actions
                = new ActionsConfiguration(applicationContext, metadataReaderFactory);
        actions.setBasePackages(Collections.singletonList("io.flowset.controljmixbpm.flowui"));
        return actions;
    }
}
