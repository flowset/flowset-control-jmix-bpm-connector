package io.flowset.controljmixbpm.rest.dto.response;

import org.flowable.rest.service.api.runtime.process.ExecutionResponse;

/**
 * Extends Flowable's {@link ExecutionResponse} and includes additional fields:
 * <ol>
 *     <li><code>multiInstanceRoot</code></li>
 * </ol>
 */
public class ControlExecutionResponse extends ExecutionResponse {
    protected Boolean multiInstanceRoot;

    public Boolean getMultiInstanceRoot() {
        return multiInstanceRoot;
    }

    public void setMultiInstanceRoot(Boolean multiInstanceRoot) {
        this.multiInstanceRoot = multiInstanceRoot;
    }
}
