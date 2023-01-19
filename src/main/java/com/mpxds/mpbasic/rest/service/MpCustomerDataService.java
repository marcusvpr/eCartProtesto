package com.mpxds.mpbasic.rest.service;

import java.util.ArrayList;
import java.util.List;

import com.mpxds.mpbasic.rest.model.MpCustomer;

public class MpCustomerDataService {
	//
    private List<MpCustomer> mpCustomerList = new ArrayList<>();

    private static MpCustomerDataService ourInstance = new MpCustomerDataService();

    public static MpCustomerDataService getInstance() {
    	//
        return ourInstance;
    }

    public String addMpCustomer(MpCustomer mpCustomer) {
    	//
        String newId = Integer.toString(mpCustomerList.size() + 1);
        mpCustomer.setId(newId);
        mpCustomerList.add(mpCustomer);
        
        return newId;
    }

    public List<MpCustomer> getMpCustomerList() {
    	//
        return mpCustomerList;
    }

    public MpCustomer getMpCustomerById(String id) {
    	//
        for (MpCustomer mpCustomer : mpCustomerList) {
        	//
            if (mpCustomer.getId().equals(id)) {
            	//
                return mpCustomer;
            }
        }
        //
        return null;
    }

}