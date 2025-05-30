
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
    "description",
    "isChargeable",
    "amenityType",
    "amenityProvider"
})
@Generated("jsonschema2pojo")
public class Amenity {

    @JsonProperty("description")
    private String description;
    @JsonProperty("isChargeable")
    private boolean isChargeable;
    @JsonProperty("amenityType")
    private String amenityType;
    @JsonProperty("amenityProvider")
    private AmenityProvider amenityProvider;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("isChargeable")
    public boolean isIsChargeable() {
        return isChargeable;
    }

    @JsonProperty("isChargeable")
    public void setIsChargeable(boolean isChargeable) {
        this.isChargeable = isChargeable;
    }

    @JsonProperty("amenityType")
    public String getAmenityType() {
        return amenityType;
    }

    @JsonProperty("amenityType")
    public void setAmenityType(String amenityType) {
        this.amenityType = amenityType;
    }

    @JsonProperty("amenityProvider")
    public AmenityProvider getAmenityProvider() {
        return amenityProvider;
    }

    @JsonProperty("amenityProvider")
    public void setAmenityProvider(AmenityProvider amenityProvider) {
        this.amenityProvider = amenityProvider;
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
