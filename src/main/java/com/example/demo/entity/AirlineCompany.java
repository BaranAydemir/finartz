package com.example.demo.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AirlineCompany")
public class AirlineCompany implements Serializable {

   @Id
   @GeneratedValue
   @JsonIgnore
   private int id;
   @JsonProperty("Code")
   private String code;
   @JsonProperty("Name")
   private String name;
}
