
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
    "departure",
    "arrival",
    "carrierCode",
    "number",
    "aircraft",
    "operating",
    "duration",
    "id",
    "numberOfStops",
    "blacklistedInEU"
})
@Generated("jsonschema2pojo")
public class Segment {

    @JsonProperty("departure")
    private Departure departure;
    @JsonProperty("arrival")
    private Arrival arrival;
    @JsonProperty("carrierCode")
    private String carrierCode;
    @JsonProperty("number")
    private String number;
    @JsonProperty("aircraft")
    private Aircraft aircraft;
    @JsonProperty("operating")
    private Operating operating;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("id")
    private String id;
    @JsonProperty("numberOfStops")
    private int numberOfStops;
    @JsonProperty("blacklistedInEU")
    private boolean blacklistedInEU;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("departure")
    public Departure getDeparture() {
        return departure;
    }

    @JsonProperty("departure")
    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    @JsonProperty("arrival")
    public Arrival getArrival() {
        return arrival;
    }

    @JsonProperty("arrival")
    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    @JsonProperty("carrierCode")
    public String getCarrierCode() {
        return carrierCode;
    }

    @JsonProperty("carrierCode")
    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("aircraft")
    public Aircraft getAircraft() {
        return aircraft;
    }

    @JsonProperty("aircraft")
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @JsonProperty("operating")
    public Operating getOperating() {
        return operating;
    }

    @JsonProperty("operating")
    public void setOperating(Operating operating) {
        this.operating = operating;
    }

    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("numberOfStops")
    public int getNumberOfStops() {
        return numberOfStops;
    }

    @JsonProperty("numberOfStops")
    public void setNumberOfStops(int numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    @JsonProperty("blacklistedInEU")
    public boolean isBlacklistedInEU() {
        return blacklistedInEU;
    }

    @JsonProperty("blacklistedInEU")
    public void setBlacklistedInEU(boolean blacklistedInEU) {
        this.blacklistedInEU = blacklistedInEU;
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
