package com.example.oppormap.model.entity;

import java.io.Serializable;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -4530041273936182978L;
    private String id;
    private String name;
    private String address;
    private String location;
    private Set<String> skills;
    private String registered;
    private String updated;
}
