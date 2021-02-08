package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Route")
public class Route implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn(name="placeOfDeparture_Id")
    @JsonProperty("PlaceOfDeparture")
    private Airport placeOfDeparture;

    @ManyToOne
    @JoinColumn(name="destination_Id")
    @JsonProperty("Destination")
    private Airport destination;

    @ManyToOne
    @JoinColumn(name="airlineCompany_Id")
    @JsonProperty("AirlineCompany")
    private AirlineCompany airlineCompany;

    @JsonProperty("Code")
    private String code;

    public void setCode(){
        this.code = this.airlineCompany.getCode() + "-" +this.placeOfDeparture.getCode() + "-" + this.destination.getCode();
    }
}
