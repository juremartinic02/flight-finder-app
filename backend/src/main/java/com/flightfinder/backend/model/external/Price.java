
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
    "currency",
    "total",
    "base",
    "fees",
    "grandTotal",
    "additionalServices"
})
@Generated("jsonschema2pojo")
public class Price {

    @JsonProperty("currency")
    private String currency;
    @JsonProperty("total")
    private String total;
    @JsonProperty("base")
    private String base;
    @JsonProperty("fees")
    private List<Fee> fees;
    @JsonProperty("grandTotal")
    private String grandTotal;
    @JsonProperty("additionalServices")
    private List<AdditionalService> additionalServices;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("total")
    public String getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(String total) {
        this.total = total;
    }

    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    @JsonProperty("fees")
    public List<Fee> getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    @JsonProperty("grandTotal")
    public String getGrandTotal() {
        return grandTotal;
    }

    @JsonProperty("grandTotal")
    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    @JsonProperty("additionalServices")
    public List<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    @JsonProperty("additionalServices")
    public void setAdditionalServices(List<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
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
