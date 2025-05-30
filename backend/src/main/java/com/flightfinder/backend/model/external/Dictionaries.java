
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
    "locations",
    "aircraft",
    "currencies",
    "carriers"
})
@Generated("jsonschema2pojo")
public class Dictionaries {

    @JsonProperty("locations")
    private Locations locations;
    @JsonProperty("aircraft")
    private Aircraft__1 aircraft;
    @JsonProperty("currencies")
    private Currencies currencies;
    @JsonProperty("carriers")
    private Carriers carriers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("locations")
    public Locations getLocations() {
        return locations;
    }

    @JsonProperty("locations")
    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    @JsonProperty("aircraft")
    public Aircraft__1 getAircraft() {
        return aircraft;
    }

    @JsonProperty("aircraft")
    public void setAircraft(Aircraft__1 aircraft) {
        this.aircraft = aircraft;
    }

    @JsonProperty("currencies")
    public Currencies getCurrencies() {
        return currencies;
    }

    @JsonProperty("currencies")
    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    @JsonProperty("carriers")
    public Carriers getCarriers() {
        return carriers;
    }

    @JsonProperty("carriers")
    public void setCarriers(Carriers carriers) {
        this.carriers = carriers;
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
