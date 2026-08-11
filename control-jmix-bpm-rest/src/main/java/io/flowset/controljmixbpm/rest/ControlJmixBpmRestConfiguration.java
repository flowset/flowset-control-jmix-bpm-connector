/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest;

import io.flowset.controljmixbpm.rest.converter.ControlDmnRestResponseFactory;
import io.flowset.controljmixbpm.rest.converter.ControlRestResponseFactory;
import io.flowset.controljmixbpm.rest.converter.ControlJmixVariableValueConverter;
import org.flowable.common.rest.variable.RestVariableConverter;
import org.flowable.dmn.rest.service.api.DmnRestResponseFactory;
import org.flowable.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@PropertySource(name = "io.flowset.controljmixbpm.rest", value = "classpath:/io/flowset/controljmixbpm/rest/module.properties")
public class ControlJmixBpmRestConfiguration {

    @Bean("ctrint_RestResponseFactory")
    public RestResponseFactory restResponseFactory(ObjectMapper objectMapper,
                                                   ObjectProvider<RestVariableConverter> variableConverters,
                                                   ControlJmixVariableValueConverter converter) {
        ControlRestResponseFactory responseFactory = new ControlRestResponseFactory(objectMapper, converter);
        List<RestVariableConverter> additionalVariableConverters = variableConverters.orderedStream().toList();
        responseFactory.getVariableConverters().addAll(additionalVariableConverters);

        return responseFactory;
    }

    @Bean("ctrint_DmnRestResponseFactory")
    public DmnRestResponseFactory dmnRestResponseFactory(ObjectProvider<RestVariableConverter> variableConverters) {
        ControlDmnRestResponseFactory responseFactory = new ControlDmnRestResponseFactory();
        List<RestVariableConverter> additionalVariableConverters = variableConverters.orderedStream().toList();
        responseFactory.getVariableConverters().addAll(additionalVariableConverters);

        return responseFactory;
    }

}
