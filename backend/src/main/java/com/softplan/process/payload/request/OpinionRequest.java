package com.softplan.process.payload.request;

import javax.validation.constraints.NotBlank;

public class OpinionRequest {
	@NotBlank
	private String description;

	@NotBlank
	private String aproved;

	@NotBlank
	private String processId;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAproved() {
		return aproved;
	}

	public void setAproved(String aproved) {
		this.aproved = aproved;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}