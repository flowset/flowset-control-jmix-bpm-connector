package io.flowset.controljmixbpm.rest;

import io.jmix.core.annotation.JmixModule;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@SpringBootConfiguration
// The BPMN (process) and DMN engines are intentionally kept out of the test context: they would boot a real
// Flowable engine and run DB schema creation (Flowable 8 has no HSQL DDL for DMN). The REST controllers only
// need the Flowable service beans, which are provided as mocks below.
@EnableAutoConfiguration(excludeName = {
        "org.flowable.spring.boot.ProcessEngineAutoConfiguration",
        "org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration",
        "org.flowable.spring.boot.dmn.DmnEngineAutoConfiguration",
        "org.flowable.spring.boot.dmn.DmnEngineServicesAutoConfiguration"
})
@Import(ControlJmixBpmRestConfiguration.class)
@PropertySource("classpath:/io/flowset/controljmixbpm/rest/test-app.properties")
@JmixModule(id = "io.flowset.controljmixbpm.rest.test", dependsOn = ControlJmixBpmRestConfiguration.class)
public class ControlJmixBpmRestTestConfiguration {

    @Bean
    @Primary
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    RepositoryService repositoryService() {
        return Mockito.mock(RepositoryService.class);
    }

    @Bean
    RuntimeService runtimeService() {
        return Mockito.mock(RuntimeService.class);
    }

    @Bean
    HistoryService historyService() {
        return Mockito.mock(HistoryService.class);
    }

    @Bean
    ManagementService managementService() {
        return Mockito.mock(ManagementService.class);
    }

    @Bean
    DmnRepositoryService dmnRepositoryService() {
        return Mockito.mock(DmnRepositoryService.class);
    }
}
