package com.example.oppormap.model.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobPosting implements Serializable {
    private static final long serialVersionUID = -8904755616356362818L;

    private String id;
    private String url;
    private String description;
    private Employer employer;
}
