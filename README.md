# Jmix BPM integration with Flowset Control

A Jmix add-on that connects a [Jmix BPM](https://docs.jmix.io/) application to [Flowset Control](https://flowset.io/control) — a separate web
application for monitoring and operating BPM engines. It exposes the REST endpoints Control needs to read and manage
this engine, and adds *Open in Control* buttons to the standard Jmix BPM screens so users can jump from a process
instance, definition or task straight to the same record in Control.

## Modules

| Starter | What it adds to your application |
|---|---|
| `flowset-control-jmix-bpm-rest-starter` | The Flowable REST API (`/process-api`, `/dmn-api`, `/external-job-api`), plus the `/control/**` endpoints Flowset Control calls on top of it to read and manage this engine — process definitions and instances, history, variables, decisions, DMN deployments and dead-letter jobs |
| `flowset-control-jmix-bpm-flowui-starter` | *Open in Control* / *Evaluate in Control* buttons on the standard Jmix BPM screens, linking straight to the matching record in Control |

## Compatibility

| Integration add-on | Jmix BPM | Flowable |
|---|---|---|
| 1.0.0-SNAPSHOT | 3.0.x, 3.1.x | 8.0.x |

## Installation

Your application must already have Jmix BPM installed — the add-on does not bring it in itself.

Declare the starters you need:

```groovy
implementation 'io.flowset.control:flowset-control-jmix-bpm-rest-starter:1.0.0-SNAPSHOT'
implementation 'io.flowset.control:flowset-control-jmix-bpm-flowui-starter:1.0.0-SNAPSHOT'
```

Adding the dependency is enough — the add-on activates itself, with no configuration class to register.

## Configuration

```properties
# URL of the Flowset Control UI; the Open in Control actions stay disabled while it is blank
flowset.ui.control-url=https://control.example.com
# URL of this engine as registered in Control; defaults to this application's context path when omitted
flowset.ui.engine-url=https://my-jmix-app.example.com
# view ids this add-on must not override, for applications that already override them themselves
flowset.ui.excluded-views=bpm_ProcessInstance.list,bpm_ProcessDefinition.list
```

`flowset.ui.engine-url` is optional — leave it out when Control reaches this application at the same address the
application serves itself on.

While `flowset.ui.control-url` is not set, the *Open in Control* actions are simply disabled. Nothing breaks.

## Actions related to Control in custom Jmix BPM views

The UI starter works by replacing the standard Jmix BPM screens with its own versions that carry the Control buttons.
If your application already replaces one of those screens, the two replacements collide. Fixing it takes two steps:
tell the add-on to stand down for that screen, then add the button to your own screen.

**1. Exclude the view** so the add-on leaves that screen alone:

```properties
flowset.ui.excluded-views=bpm_ProcessInstance.list,bpm_ProcessInstanceData.detail
```

**2. Declare the action** in your own view descriptor.

On list screens, declare the action inside the data grid's `<actions>` element, which binds it to that grid. The button
then references it as `<grid id>.<action id>`:

```xml
<layout>
    <hbox id="buttonsPanel">
        <button id="openInControlBtn" action="processInstancesGrid.openInControl"/>
    </hbox>
    <dataGrid id="processInstancesGrid">
        <actions>
            <action id="openInControl" type="ctrcnt_openProcessInstanceInControl"/>
        </actions>
    </dataGrid>
</layout>
```

On detail screens, declare it in the view-level `<actions>` element and reference the action id directly:

```xml
<actions>
    <action id="openInControl" type="ctrcnt_openProcessInstanceDetailInControl"/>
</actions>
<layout>
    <hbox id="processActionsPanel">
        <button id="openInControlBtn" action="openInControl"/>
    </hbox>
</layout>
```

Which action type belongs to which screen:

| Standard Jmix BPM view id | Action type | Declare in |
|---|---|---|
| `bpm_ProcessInstance.list` | `ctrcnt_openProcessInstanceInControl` | data grid `<actions>` |
| `bpm_ProcessInstanceData.detail` | `ctrcnt_openProcessInstanceDetailInControl` | view `<actions>` |
| `bpm_ProcessDefinition.list` | `ctrcnt_openProcessDefinitionInControl` | data grid `<actions>` |
| `bpm_ProcessDefinition.detail` | `ctrcnt_openProcessDefinitionDetailInControl` | view `<actions>` |
| `bpm_DmnDecisionTableListView` | `ctrcnt_openDmnDecisionInControl`, `ctrcnt_evaluateDmnDecisionInControl` | data grid `<actions>` |
| `bpm_DmnDecisionTableDetailView` | `ctrcnt_openDmnDecisionDetailInControl` | view `<actions>` |
| `bpm_AllTasksView` | `ctrcnt_openAllTasksInControl` | data grid `<actions>` |

What to expect from the actions:

- List actions open the selected row in Control when exactly one row is selected, and the corresponding filtered list
  otherwise.
- Each action supplies its own icon, text and tooltip, and stays disabled while `ControlJmixBpmUiProperties` is unset,
  so it is safe to declare unconditionally. Set `text` or `description` on the `<action>` element to override them.
- The filtered list only carries filter values if the action is given a value provider per filter in the view's
  `InitEvent`, for example
  `openInControlAction.setBusinessKeyFilterValueProvider(() -> businessKeyField.getValue())` on the process-instance
  list. The providers are read on every invocation, so the current user input is always applied. Without them the
  action still works, it just opens an unfiltered list.

`bpm_DecisionTableModelerView` is absent from the table on purpose: it carries no button. The add-on's version of that
screen accepts an inbound `decisionDefId` query parameter so links *from* Control open the right decision in the
modeler. Excluding that view id disables the inbound link, and there is no action to add in its place.

## Security

The add-on ships no security configuration, so its endpoints are open unless your application secures them.

Control talks to the engine machine-to-machine. Pick one of the schemes below — HTTP Basic, an OAuth2 bearer token
Control obtains for itself with the client credentials grant, or, while developing, no authentication at all.
Whichever you choose, cover the Flowable REST APIs the REST starter brings in as well: securing only `/control/**`
would leave `/process-api/**`, `/dmn-api/**` and `/external-job-api/**` open.

These endpoints call the Flowable services directly and carry no permission checks of their own, so the filter chain
is all that stands between a caller and the engine: whoever gets through it can use every operation the endpoints
expose. Implement Flowable's `BpmnRestApiInterceptor` and `DmnRestApiInterceptor` to narrow that down.

### Basic authentication (example)

```java
package com.company.myapp.security;

import io.jmix.core.JmixSecurityFilterChainOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.pathPattern;

@Configuration
public class BpmRestSecurityConfiguration {

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain bpmRestFilterChain(HttpSecurity http) throws Exception {
        http.securityMatchers(requestMatcherConfigurer -> {
                    requestMatcherConfigurer.requestMatchers("/control/**")
                            .requestMatchers(pathPattern("/process-api/**"), // Matcher to filter requests related to Flowable BPMN API
                                    pathPattern("/dmn-api/**"), // Matcher to filter requests related to Flowable DMN API
                                    pathPattern("/external-job-api/**")); // Matcher to filter requests related to Flowable External Task API
                })
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for POST requests
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()); // Gives authenticated access to any endpoint

        return http.build();
    }
}
```

The chain is confined to those paths, so your existing UI security is untouched.

Basic credentials are only base64-encoded, so serve these endpoints over HTTPS. Control authenticates as a regular
application user, so give it a dedicated account rather than reusing a person's.

### OIDC authentication (example)

Control can authenticate with an access token it obtains itself through the **client credentials** grant: it exchanges
its client id and secret at the OIDC provider's token endpoint and sends the resulting token as
`Authorization: Bearer <token>` on every call to the engine. The secret never reaches the engine and no credentials of
yours are stored there — the engine only has to validate the token, which means configuring the application as an
OAuth2 *resource server*.

#### On the Control side

Register this engine with the **OAuth 2 (Client/Secret)** authentication type and fill in:

| Field | Value |
|---|---|
| Issuer URI | the provider's issuer, e.g. `https://keycloak.example.com/realms/flowset`. Control discovers the token endpoint from the issuer's `/.well-known/openid-configuration` document, so there is no separate token-endpoint field — the issuer must serve a valid discovery document |
| Client ID, Client secret | the confidential client Control authenticates as. In Keycloak, a client with *Service accounts* enabled and the standard flow off |
| Scope | optional, separated by spaces or commas. Sent as given; nothing is appended |

Control requests the token with the `client_credentials` grant, reuses it until it expires, and sends it as a bearer
token. It sends no `audience` parameter — if your provider needs one for this engine, configure that on the provider
side.

#### On the engine side

In an application that already uses the Jmix OIDC add-on, secure the same paths as in the Basic example, this time as
a resource server:

```java
package com.company.myapp.security;

import io.jmix.core.JmixSecurityFilterChainOrder;
import io.jmix.oidc.jwt.JmixJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.pathPattern;

@Configuration
public class BpmRestSecurityConfiguration {

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain bpmRestFilterChain(HttpSecurity http,
                                           JmixJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.securityMatchers(requestMatcherConfigurer -> {
                    requestMatcherConfigurer.requestMatchers("/control/**")
                            .requestMatchers(pathPattern("/process-api/**"), // Matcher to filter requests related to Flowable BPMN API
                                    pathPattern("/dmn-api/**"), // Matcher to filter requests related to Flowable DMN API
                                    pathPattern("/external-job-api/**")); // Matcher to filter requests related to Flowable External Task API
                })
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for POST requests
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))) // Validate the bearer token and build a Jmix user from its claims
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()); // Gives authenticated access to any endpoint

        return http.build();
    }
}
```

`JmixJwtAuthenticationConverter` is what makes the token usable by Jmix: it builds a user from the token's claims,
taking the username from `sub` and the role codes from a flat `roles` claim.

### No authentication (development only)

On the Control side, leave *Authentication enabled* unchecked when registering the engine; Control then calls it
without credentials.

On the engine side, let the same paths through unauthenticated:

```java
package com.company.myapp.security;

import io.jmix.core.JmixSecurityFilterChainOrder;
import io.jmix.security.util.JmixHttpSecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.pathPattern;

@Configuration
public class BpmRestSecurityConfiguration {

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain bpmRestFilterChain(HttpSecurity http) throws Exception {
        http.securityMatchers(requestMatcherConfigurer -> {
                    requestMatcherConfigurer.requestMatchers("/control/**")
                            .requestMatchers(pathPattern("/process-api/**"), // Matcher to filter requests related to Flowable BPMN API
                                    pathPattern("/dmn-api/**"), // Matcher to filter requests related to Flowable DMN API
                                    pathPattern("/external-job-api/**")); // Matcher to filter requests related to Flowable External Task API
                })
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for POST requests
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()); // Let every request through unauthenticated

        JmixHttpSecurityUtils.configureAnonymous(http); //Use anonymous user returned by the Jmix UserRepository
        return http.build();
    }
}
```

Unlike the two examples above, this one does need `JmixHttpSecurityUtils.configureAnonymous(http)`: the requests
actually reach the controllers, so Jmix needs its own anonymous user in the security context rather than Spring's
default principal.

This leaves every process instance, definition and decision in the engine readable and writable by anyone who can
reach the application — including suspending definitions, changing activity state and deleting deployments. Keep it
to a local or throwaway environment, and never expose it beyond one.

## License

Control-Jmix BPM integration add-on is an open-source project distributed under the [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) license. 
