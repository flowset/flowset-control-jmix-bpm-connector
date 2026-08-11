package io.flowset.controljmixbpm.rest.dto.response;

/**
 * Additional information for entity, entity list, and file description variables.
 */
public class VariableValueInfo {
    private String objectTypeName;
    private String serializationDataFormat;

    private String filename;

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    public String getSerializationDataFormat() {
        return serializationDataFormat;
    }

    public void setSerializationDataFormat(String serializationDataFormat) {
        this.serializationDataFormat = serializationDataFormat;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
