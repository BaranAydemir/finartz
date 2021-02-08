package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Ticket")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Surname")
    private String surname;
    @JsonProperty("TC")
    @Column(length = 11,nullable = false)
    private String tc;

    @OneToOne
    @JoinColumn(name="card_id")
    @JsonProperty("Card")
    private Card card;

    @JsonProperty("TicketNumber")
    private String ticketNumber;

    @JsonProperty("TicketStatus")
    @Column(nullable = true)
    private boolean ticketStatus;

    @OneToOne
    @JoinColumn(name="flight_id")
    private Flight flight;


    public void setTicketNumber(){
        this.ticketNumber = this.getFlight().getCode() + "-" + String.valueOf(this.getTc()).substring(0,5);
    }
}
