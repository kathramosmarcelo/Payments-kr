package com.kramomar.Payments.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment implements Serializable{
	@Id
	private String id;
	private double amount;
	private String numberPhoneOrigin;
	private String numberPhoneDestination;
	private String date;
	
}
