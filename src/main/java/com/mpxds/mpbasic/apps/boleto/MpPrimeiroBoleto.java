package com.mpxds.mpbasic.apps.boleto;

//import java.awt.Desktop;
//import java.io.File;
//import java.io.IOException;
import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;

import com.mpxds.mpbasic.model.enums.MpCartorioOficio;
import com.mpxds.mpbasic.security.MpCustomPasswordEncoder;
import com.mpxds.mpbasic.security.MpSenhaUtils;
import com.mpxds.mpbasic.util.MpAppUtil;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
//import org.jrimum.bopepo.view.BoletoViewer;
//import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
//import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
//import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

public class MpPrimeiroBoleto {
	//
	public static void main(String[] args) {
		//
		String password = "teste";
		
		String senhaCripto = MpAppUtil.md5(password);
		
	    MpCustomPasswordEncoder mpCustomPasswordEncoder = new MpCustomPasswordEncoder();
	     
	    String encoded = mpCustomPasswordEncoder.encode(password);	
	    
	    String encodedX = MpSenhaUtils.gerarBCrypt(password);		
		
		MpAppUtil.PrintarLn("Senha = ( " + password + " ( cripto / encoded = ( " + senhaCripto + " / " + encoded + 
							" ) / ( tam = " + senhaCripto.length() + " / "+ encoded.length() +
							" ) / ( encodeX = " + encodedX + " / "+ encodedX.length() +
							" ) / ( geraSenha = " + MpAppUtil.gerarNovaSenha());
		//
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		//
		String ofNomeCedente = MpCartorioOficio.Of3.getNomeCedente();
		String ofCnpj = MpCartorioOficio.Of3.getCnpj();
		Integer ofCarteira = MpCartorioOficio.Of3.getCarteira();
		Integer ofAgenciaCodigoCedente = MpCartorioOficio.Of3.getAgenciaCodigoCedente();
		String ofAgenciaCodigoCedenteDig = MpCartorioOficio.Of3.getAgenciaCodigoCedenteDig();
		Integer ofAgenciaContaCedente = MpCartorioOficio.Of3.getAgenciaContaCedente();
		String ofAgenciaContaCedenteDig = MpCartorioOficio.Of3.getAgenciaContaCedenteDig();
		// Cedente
		Cedente cedente = new Cedente(ofNomeCedente, ofCnpj);
		
		// Sacado
		Sacado sacado = new Sacado("MUNDO NATURAL COMERCIO E REPRESENTA - CNPJ:68.721.893/0001-06");
		
		// Endereço do sacado
		Endereco endereco = new Endereco();
//		endereco.setUF(UnidadeFederativa.MG);
//		endereco.setLocalidade("Belo Horizonte");
//		endereco.setCep(new CEP("10100-000"));
		endereco.setBairro("Protocolo: 09/03/2017-016330");
//		endereco.setLogradouro("Rua São José");
//		endereco.setNumero("1010");
		
		sacado.addEndereco(endereco);
		
		// Criando o título
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
		
		contaBancaria.setAgencia(new Agencia(ofAgenciaCodigoCedente, ofAgenciaCodigoCedenteDig));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(ofAgenciaContaCedente, ofAgenciaContaCedenteDig));
		contaBancaria.setCarteira(new Carteira(ofCarteira));
		
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento("2017 / 016330");
		titulo.setNossoNumero("20117016330"); // Numérico e Tam.max=11?
		titulo.setDigitoDoNossoNumero("1");
		//
		titulo.setValor(BigDecimal.valueOf(9624.67));
		titulo.setAcrecimo(BigDecimal.valueOf(15.65));
		titulo.setValorCobrado(BigDecimal.valueOf(9641.53));
		
//		ParametrosBancariosMap parametrosBancariosMap = new ParametrosBancariosMap(); 

		calendar.set(2017, 3, 14);
		titulo.setDataDoDocumento(calendar.getTime());
//		try {
//			titulo.setDataDoDocumento(sdf.parse("14/03/2017"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		//
		calendar.set(2017, 3, 15);
		titulo.setDataDoVencimento(calendar.getTime());
		
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		
		titulo.setAceite(Aceite.N);
		
		// Dados do boleto
		Boleto boleto = new Boleto(titulo);
		boleto.setLocalPagamento("Preferencialmente nas Agências Bradesco");
		boleto.setInstrucaoAoSacado("Evite multas, pague em dias suas contas.");
		
		boleto.setInstrucao1("1-Guia válida apenas para pagamento em DINHEIRO na data do vencimento");
		boleto.setInstrucao2("2-Não receber apos o vencimento");
		boleto.setInstrucao3("3-Retorne ao Tabelionato, com a guia autenticada, no 1º dia util apos o pagamento");
		boleto.setInstrucao4("para retirar seu titulo 'quitado' a fim de evitar o protesto do titulo");
		boleto.setInstrucao5("4-Dirija-se ao 7º Oficio para evitar a distribuição do protesto");
		boleto.setInstrucao8("                                           GUIA Nº  190210  Hora  16:42:38");
		
//		BoletoViewer boletoViewer = new BoletoViewer(boleto);
//		File arquivoPdf = boletoViewer.getPdfAsFile("meu-primeiro-boleto.pdf");
//		
//		mostrarNaTela(arquivoPdf);
	}
	
//	private static void mostrarNaTela(File arquivo) {
//		Desktop desktop = Desktop.getDesktop();
//		
//		try {
//			desktop.open(arquivo);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
