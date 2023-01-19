package com.mpxds.mpbasic.apps;

import java.text.ParseException;

import com.mpxds.mpbasic.util.MpAppUtil;

public class MpMeuTeste {
	//
	public static void main(String[] args) {
		//
		try {
			System.out.println("MpMeuTeste() - 000 ( At = " + MpAppUtil.formatarString("64146715768", 
																						"###.###.###-##"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");
//		
//    	Calendar calendarX = Calendar.getInstance();	    	
//
//		calendarX.setTime(new Date());
//		calendarX.set(Calendar.HOUR_OF_DAY, 16);
//		calendarX.set(Calendar.MINUTE, 0);
//		calendarX.set(Calendar.SECOND, 0);
//		calendarX.set(Calendar.MILLISECOND, 0);
//		//
//		Date dtBoleto = calendarX.getTime();
//		//
//		Date dtAtual = new Date();	    	
//		calendarX.setTime(dtAtual);
//		calendarX.add(Calendar.MINUTE, 3);	    	
//		Date dtAtualF = calendarX.getTime();
//		//
//		System.out.println("MpMeuTeste() - 000 ( At = " + sdf.format(dtAtual) +
//					" / Bol = " + sdf.format(dtBoleto) + " / Bol+1 = " + sdf.format(dtAtualF));
//		//
//		if ((dtBoleto.after(dtAtual) && dtBoleto.before(dtAtualF))) {
			//
//			System.out.println("MpMeuTeste() - 001");
//		}
//		//
//		Date dataHoraAtual = new Date();
//		try {
//			System.out.println("MpMeuTeste() - 002 ( " + sdf.format(dataHoraAtual) + " " + sdfHMS.parse("13:50:00"));
//
//			if (dataHoraAtual.after(sdfHMS.parse("13:50:00"))) {
//				System.out.println("MpMeuTeste() - 003 ( " + sdf.format(dataHoraAtual) + " " + sdfHMS.parse("13:50:00"));
//			}
//		} catch (ParseException e) {
//			System.out.println("MpMeuTeste() - 004 (e = " + e);
//		}
//		
	}
	
}
