package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Data
@Entity
@Table(name = "Flight")
public class Flight implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @JsonProperty("DepartureTime")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date departureTime;

    @JsonProperty("Arrivaltime")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date arrivalTime;

    @ManyToOne
    @JoinColumn(name="route_Id")
    @JsonProperty("Route")
    private Route route;

    @JsonProperty("Quota")
    private int quota;

    @JsonProperty("TicketPrice")
    @Column(nullable = true)
    private double ticketPrice;

    @JsonIgnore
    @JsonProperty("TicketCount")
    @Column(nullable = true)
    private int ticketCount;

    @JsonProperty("Code")
    private String code;

    public void setCode(){
        this.code = this.route.getCode() + "-" + getRandomNumber();
    }

    private String getRandomNumber(){
        Random random = new Random();
        random.nextInt(10000000);
        return String.valueOf(random.nextInt(10000000));
    }

    public void setTicketCount(){
        this.ticketCount = this.getQuota();
    }

}
