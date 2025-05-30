
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
    "travelerId",
    "fareOption",
    "travelerType",
    "price",
    "fareDetailsBySegment"
})
@Generated("jsonschema2pojo")
public class TravelerPricing {

    @JsonProperty("travelerId")
    private String travelerId;
    @JsonProperty("fareOption")
    private String fareOption;
    @JsonProperty("travelerType")
    private String travelerType;
    @JsonProperty("price")
    private Price__1 price;
    @JsonProperty("fareDetailsBySegment")
    private List<FareDetailsBySegment> fareDetailsBySegment;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("travelerId")
    public String getTravelerId() {
        return travelerId;
    }

    @JsonProperty("travelerId")
    public void setTravelerId(String travelerId) {
        this.travelerId = travelerId;
    }

    @JsonProperty("fareOption")
    public String getFareOption() {
        return fareOption;
    }

    @JsonProperty("fareOption")
    public void setFareOption(String fareOption) {
        this.fareOption = fareOption;
    }

    @JsonProperty("travelerType")
    public String getTravelerType() {
        return travelerType;
    }

    @JsonProperty("travelerType")
    public void setTravelerType(String travelerType) {
        this.travelerType = travelerType;
    }

    @JsonProperty("price")
    public Price__1 getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Price__1 price) {
        this.price = price;
    }

    @JsonProperty("fareDetailsBySegment")
    public List<FareDetailsBySegment> getFareDetailsBySegment() {
        return fareDetailsBySegment;
    }

    @JsonProperty("fareDetailsBySegment")
    public void setFareDetailsBySegment(List<FareDetailsBySegment> fareDetailsBySegment) {
        this.fareDetailsBySegment = fareDetailsBySegment;
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
