package com.mpxds.mpbasic.util;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class MpProgressBarView implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private double progress = 0d;
	private String message = "ready";

	public String longOperation() throws InstantiationException, IllegalAccessException, InterruptedException {
		//
		for (int i = 0; i < 100; i++) {
			// simulate a heavy operation
			progress++;
			message = "processing [" + i + "]";
			//
			Thread.sleep(1000);
		}

		message = "completed";
		//
		return "result";
	}

	public double getProgress() { return progress; }
	public void setProgress(double progress) { this.progress = progress; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
}
