package org.soa.activemq.model;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.data.annotation.Id;

public class Contact {
	@Id
	String id;
	
	String vendorName;
	String firstName;
	String lastName;
	String address;
	String city;
	String state;
	String zip;
	String email;
	String phoneNumber;
	String createTimestamp;
	

	private Contact() {
	
	}
	
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getCreateTimestamp() {
		if(id == null)
			this.createTimestamp = Timestamp.from(Instant.now()).toString();
		return createTimestamp;
	}

	@Override
	public String toString() {
		return "Contact [id="+id+",vendorName=" + vendorName + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address=" + address + ", city=" + city + ", state=" + state + ", zip=" + zip + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + "]";
	}




}
