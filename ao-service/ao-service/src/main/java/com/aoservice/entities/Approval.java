package com.aoservice.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Approval {

    private String id;
    private boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

	public Approval(boolean status) {
		super();
		this.status = status;
	}
    @JsonCreator
    public Approval(@JsonProperty("id")String id, @JsonProperty("status")boolean status) {
        this.id = id;
        this.status = status;
    }
}
