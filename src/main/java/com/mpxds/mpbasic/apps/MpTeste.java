package com.mpxds.mpbasic.apps;

//import com.mpxds.mpbasic.model.enums.MpEspecieTituloBradesco;

public class MpTeste {

	public static void main(String[] args) {
		//
		System.out.println("MpMeuTeste() - 001");

		String guia = "600/020" + 
				" | HORÁRIO DE ATENDIMENTO: DAS 10:00 H ATÉ 14:00 H" + 
				" | A este Ofício de Protesto de Títulos foi solicitada a apresentação e a intimação de V.Sª. para pagamento" + 
				" | do título/documento de dívida, com as características abaixo, sendo consequente a lavratura do" + 
				" | instrumento de protesto, caso não ocorra seu pagamento, neste Tabelionato, até 03 dias úteis da data da" + 
				" | assinatura da intimação. Por este instrumento que expedi e remeti na forma do art. 1º do Provimento 97" + 
				" | do CNJ (Conselho Nacional de Justiça), INTIMO-O(A) a efetuar o aludido pagamento, sob pena de" + 
				" | protesto de cujo instrumento, a ser entregue ao apresentante, constará a resposta eventualmente" + 
				" | oferecida, por escrito no mesmo prazo.";

		String[] wordsNumeroGuia = guia.split("|");
		
		System.out.println("MpMeuTeste() - 001 ( " + wordsNumeroGuia[wordsNumeroGuia.length - 1]);
		
		try {
			Integer numeroGuiaGerado = Integer.parseInt(wordsNumeroGuia[wordsNumeroGuia.length - 1]);

			System.out.println("MpMeuTeste() - 002 ( numeroGuia = " + numeroGuiaGerado);
			//
		} catch (Exception e) {
			//
			System.out.println("( " + "Error! Controle GUIA/Instrução8 ! Contactar SUPORTE = " + 
																						guia + " / e = " + e + " )");
		}
		
//		try {
//			MpEspecieTituloBradesco mpEspecieTituloBradesco = MpEspecieTituloBradesco.valueOf("DM3");
//			if (null==mpEspecieTituloBradesco)
//				System.out.println("MpMeuTeste() - 001 ( NULL");
//			else
//				System.out.println("MpMeuTeste() - 001 ( " + mpEspecieTituloBradesco.getDescricao());
//			}
//		catch(Exception e) {
//			System.out.println("MpMeuTeste() - 001 ( " + e);			
//		}

	}

}
