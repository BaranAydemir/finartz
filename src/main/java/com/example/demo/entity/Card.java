package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Card")
public class Card {
    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @JsonProperty("NameAndSurnameOntheCard")
    private String nameAndSurnameOntheCard;

    @JsonProperty("CardNumber")
    private String cardNumber;

    @JsonProperty("validDate")
    private String validDate;

    @JsonProperty("cvcCode")
    private String cvcCode;

}
