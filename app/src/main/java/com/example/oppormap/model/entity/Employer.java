package com.example.oppormap.model.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employer implements Serializable {
    private static final long serialVersionUID = -2943944827523557446L;

    private String name;
    private String website;
    private String address;
    private double latitude;
    private double longitude;
}
