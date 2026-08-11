package io.flowset.controljmixbpm.rest.dto.response;

import org.flowable.common.rest.util.DateToStringSerializer;
import org.flowable.rest.service.api.history.HistoricVariableInstanceResponse;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Extends Flowable's {@link HistoricVariableInstanceResponse} and includes additional fields:
 * <ol>
 *     <li><code>time</code></li>
 *     <li><code>createTime</code></li>
 *     <li><code>lastUpdateTime</code></li>
 *     <li><code>valueInfo</code> - additional information for entity, entity list, and file description variables</li>
 * </ol>
 */
public class ControlHistoricVariableInstanceResponse extends HistoricVariableInstanceResponse
        implements HasValueInfo {

    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date time;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date createTime;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date lastUpdateTime;

    protected VariableValueInfo valueInfo;

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public VariableValueInfo getValueInfo() {
        return valueInfo;
    }

    @Override
    public void setValueInfo(VariableValueInfo variableValueInfo) {
        this.valueInfo = variableValueInfo;
    }
}
