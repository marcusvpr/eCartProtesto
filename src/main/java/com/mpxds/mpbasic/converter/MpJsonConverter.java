package com.mpxds.mpbasic.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class MpJsonConverter implements AttributeConverter<String, Map<String, Object>> {
	//
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToDatabaseColumn(String attribute) {
    	//
        if (attribute == null) {
           return new HashMap<>();
        }
        //
        try {
        	//
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(attribute, HashMap.class);
        }
        catch (IOException e) {
            System.out.println("Convert error while trying to convert string(JSON) to map data structure.");
        }
        //
        return new HashMap<>();
    }

    public String convertToEntityAttribute(Map<String, Object> dbData) {
    	//
        try {
        	//
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(dbData);
            //
        } catch (JsonProcessingException e) {
        	//
        	System.out.println("Could not convert map to json string.");

        	return null;
        }
    }
}