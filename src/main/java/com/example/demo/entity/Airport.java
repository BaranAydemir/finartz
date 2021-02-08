package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(name = "Airport")
public class Airport implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    public int id;
    @JsonProperty("Name")
    public String name;

    @JsonProperty("Code")
    public String code;

    @JsonIgnore
    public Date createTime;

    public void setCreateTime(){
        this.createTime = Calendar.getInstance().getTime();
    }

}
