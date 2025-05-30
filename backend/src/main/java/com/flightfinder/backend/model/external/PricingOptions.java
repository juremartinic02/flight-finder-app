
package com.flightfinder.backend.model.external;

import java.util.LinkedHashMap;
import java.util.List;
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
    "fareType",
    "includedCheckedBagsOnly"
})
@Generated("jsonschema2pojo")
public class PricingOptions {

    @JsonProperty("fareType")
    private List<String> fareType;
    @JsonProperty("includedCheckedBagsOnly")
    private boolean includedCheckedBagsOnly;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("fareType")
    public List<String> getFareType() {
        return fareType;
    }

    @JsonProperty("fareType")
    public void setFareType(List<String> fareType) {
        this.fareType = fareType;
    }

    @JsonProperty("includedCheckedBagsOnly")
    public boolean isIncludedCheckedBagsOnly() {
        return includedCheckedBagsOnly;
    }

    @JsonProperty("includedCheckedBagsOnly")
    public void setIncludedCheckedBagsOnly(boolean includedCheckedBagsOnly) {
        this.includedCheckedBagsOnly = includedCheckedBagsOnly;
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
