
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
    "ZAG",
    "BEG",
    "BCN"
})
@Generated("jsonschema2pojo")
public class Locations {

    @JsonProperty("ZAG")
    private Zag zag;
    @JsonProperty("BEG")
    private Beg beg;
    @JsonProperty("BCN")
    private Bcn bcn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("ZAG")
    public Zag getZag() {
        return zag;
    }

    @JsonProperty("ZAG")
    public void setZag(Zag zag) {
        this.zag = zag;
    }

    @JsonProperty("BEG")
    public Beg getBeg() {
        return beg;
    }

    @JsonProperty("BEG")
    public void setBeg(Beg beg) {
        this.beg = beg;
    }

    @JsonProperty("BCN")
    public Bcn getBcn() {
        return bcn;
    }

    @JsonProperty("BCN")
    public void setBcn(Bcn bcn) {
        this.bcn = bcn;
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
