package com.softplan.process.models;

import java.sql.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(	name = "process", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "number")
		})
public class Process {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 4)
	private String number;

	@NotBlank
	private String author;

	@NotBlank
	private String defendant;

	@NotNull
	private Date distributionDate;

	@OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
			mappedBy = "process")
	@JsonIgnore
	private Set<Opinion> opinions;

	public Process() {
	}

	public Process(String number, String author, String defendant, Date distributionDate) {
		this.number = number;
		this.author = author;
		this.defendant = defendant;
		this.distributionDate = distributionDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDefendant() {
		return defendant;
	}

	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}

	public Date getDistributionDate() {
		return distributionDate;
	}

	public void setDistributionDate(Date distributionDate) {
		this.distributionDate = distributionDate;
	}

	public Set<Opinion> getOpinions() {
		return opinions;
	}

	public void setOpinions(Set<Opinion> opinions) {
		this.opinions = opinions;
	}

}