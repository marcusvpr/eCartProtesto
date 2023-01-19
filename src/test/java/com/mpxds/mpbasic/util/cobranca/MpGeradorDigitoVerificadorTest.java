package com.mpxds.mpbasic.util.cobranca;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mpxds.mpbasic.util.cobranca.MpGeradorDigitoVerificador;

public class MpGeradorDigitoVerificadorTest {
	//
	private MpGeradorDigitoVerificador gerador;

	@Before
	public void init() {
		//
		this.gerador = new MpGeradorDigitoVerificador();
	}

	@Test
	public void deve_completar_com_zeros_a_esquerda() {
		//
		String numero = "1";
		String resultado = gerador.completarComZeros(numero);
		assertEquals("00000000001", resultado);
	}

	@Test
	public void deve_gerar_digito_verificador_entrada_2_saida_9() {
		//
		Integer carteira = 6;
		String nossoNumero = "00000000002";

		String digitoVerificador = gerador.gerarDigito(carteira, nossoNumero);
		assertEquals("9", digitoVerificador);
	}

	@Test
	public void deve_gerar_digito_verificador_0() {
		//
		Integer carteira = 6;
		String nossoNumero = "00071389567";

		String digitoVerificador = gerador.gerarDigito(carteira, nossoNumero);

		assertEquals("0", digitoVerificador);
	}

	@Test
	public void deve_gerar_digito_verificador_P() {
		//
		Integer carteira = 6;
		String nossoNumero = "00079762300";

		String digitoVerificador = gerador.gerarDigito(carteira, nossoNumero);

		assertEquals("P", digitoVerificador);
	}
	
}
