package com.example.oppormap.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Job implements Serializable {
    private static final long serialVersionUID = -8904755616356362818L;

    private String id;

    @JsonProperty("webpage_url")
    private String url;
}
