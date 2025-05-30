
package com.flightfinder.backend.model.external;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "320",
    "AT7"
})
@Generated("jsonschema2pojo")
public class Aircraft__1 {

    @JsonProperty("320")
    private String _320;
    @JsonProperty("AT7")
    private String at7;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("320")
    public String get320() {
        return _320;
    }

    @JsonProperty("320")
    public void set320(String _320) {
        this._320 = _320;
    }

    @JsonProperty("AT7")
    public String getAt7() {
        return at7;
    }

    @JsonProperty("AT7")
    public void setAt7(String at7) {
        this.at7 = at7;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
