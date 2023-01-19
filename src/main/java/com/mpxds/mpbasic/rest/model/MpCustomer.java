package com.mpxds.mpbasic.rest.model;

import javax.xml.bind.annotation.XmlRootElement; 

@XmlRootElement 
public class MpCustomer { 
    private String id; 
    private String name; 
    private String address; 
    private String phoneNumber; 
 
    public MpCustomer() { } 
 
    public MpCustomer(String id) { this.id = id; } 
    
    public String getId() { return id; } 
    public void setId(String id) { this.id = id; } 
 
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 
 
    public String getAddress() { return address; } 
    public void setAddress(String address) { this.address = address; } 
 
    public String getPhoneNumber() { return phoneNumber; } 
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; } 
 
}