package com.mpxds.mpbasic.security;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.mpxds.mpbasic.model.MpSistemaConfig;
import com.mpxds.mpbasic.repository.MpSistemaConfigs;

@Named
@ViewScoped // @RequestScoped
public class MpSeguranca implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ExternalContext externalContext;
	
	@Inject
	private MpSistemaConfigs mpSistemaConfigs;

	// toDo/Issue ... Colocar em SistemaConfig ...
	// ===========================================
	private String sistemaURL = "www.protestorjcapital.com.br:8080/apps/";

	private String freshdeskURL = "protrjcapital.freshdesk.com"; // "mpcom.freshdesk.com"; // "mpxdsrj.freshdesk.com";
	
	private String sistemaVersao = "1.1.9a";
	
	@SuppressWarnings("unused")
	private String sistemaHora = "00/00/0000 00:00.00";

	private String logoMenu = "logo_protestorj_smallXXX.png"; // "protestorjcapital_small.png";
	private String logoSistema = "logo_protestorj_smallXX.png"; // "protestorjcapital.png";

	// Api SOAP WSDL Intima21 ... Serviço = Intimação !
	// wsimport -keep -p com.mpxds.mpbasic.jaxws.client http://crarj.cra21.com.br/crarj/xml/protestos_cartorio.php?wsdl
	private String intima21DevURL = "http://crarj.cra21.com.br/crarj/xml/protestos_cartorio.php?wsdl";
	private String intima21ProdURL = "???";

	// Cielo Api 3.0 ...
	private String cieloRequisicaoSandboxURL = "https://apisandbox.cieloecommerce.cielo.com.br";
	private String cieloConsultaSandboxURL = "https://apiquerysandbox.cieloecommerce.cielo.com.br";

	private String cieloRequisicaoProducaoURL = "https://api.cieloecommerce.cielo.com.br/";
	private String cieloConsultaProducaoURL = "https://apiquery.cieloecommerce.cielo.com.br/";

	// Sandbox - marcus_vpr@hotmail.com ...
	private String cieloMerchantId = "6ca25a6a-8acc-4e2b-b6fe-523bcfe92716";
	private String cieloMerchantKey = "SGGFEYSQLGUTYBSRPWUQJITOZOHJESJCDXDFWUYN";

	//	Exemplo de um Cartão de teste - 4024.0071.5376.3191
	//	As informações de Cód.Segurança (CVV) e validade podem ser aleatórias, mantendo o formato - CVV (3 dígitos) 
	//	Validade (MM/YYYY).
	private String cieloCartaoTeste = "4024.0071.5376.3191";

	// SaveInCloud (SIC) S3.Minio... Panel: https://s3-protestorjcapital.jelastic.saveincloud.net/ -> PRBWG6g5T2 / G0Rk7Ls5c6 !
	private String sicMinioS3URL = "https://s3-protestorjcapital.jelastic.saveincloud.net/";
	private String sicMinioS3User = "PRBWG6g5T2";
	private String sicMinioS3Passw = "G0Rk7Ls5c6";

	// ===========================================

	private Boolean indMySQL = false; // true;
	private Boolean indSabadoDomingo = true; // AtivaCritica(Sab/Dom) = false;
	
	private String tokenJwt;

	// ===========================================

	private Boolean indLoginCancelamento = false; // true;
	private Boolean indLoginBoleto = false; // true;

	// --- Captura Agencia Conta Bradesco ( MVPR-07112020) !
	
	public String capturaAgenciaContaBradesco() {
		//
		String agenciaContaBradesco = "";
		String paramCartorio = ""; 
		
		if ( this.isUsuarioOf1())
			paramCartorio = "Of1_AgenciaContaBradesco";
		else
			if ( this.isUsuarioOf2())
				paramCartorio = "Of2_AgenciaContaBradesco";
			else
				if ( this.isUsuarioOf3())
					paramCartorio = "Of3_AgenciaContaBradesco";
				else
					if ( this.isUsuarioOf4())
						paramCartorio = "Of4_AgenciaContaBradesco";
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro(paramCartorio);
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		agenciaContaBradesco = mpSistemaConfig.getValorT();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return agenciaContaBradesco;
	}
	
	// ---
	
	public String getLoginUsuario() {
		//
		String login = null;
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (null == mpUsuarioLogado)
			assert(true); // nop
		else {
			if (null == mpUsuarioLogado.getMpUsuario())
				assert(true); // nop
			else
				login = mpUsuarioLogado.getMpUsuario().getLogin();
		}
		//
		return login;
	}	
	
	public String getEmailUsuario() {
		//
		String email = null;
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (null == mpUsuarioLogado)
			assert(true); // nop
		else {
			if (null == mpUsuarioLogado.getMpUsuario())
				assert(true); // nop
			else
				email = mpUsuarioLogado.getMpUsuario().getEmail();
		}
		//
		return email;
	}	
	
	public String getCpfCnpjUsuario() {
		//
		String cpfCnpj = null;
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (null == mpUsuarioLogado)
			assert(true); // nop
		else {
			if (null == mpUsuarioLogado.getMpUsuario())
				assert(true); // nop
			else
				cpfCnpj = mpUsuarioLogado.getMpUsuario().getCpfCnpj();
		}
		//
		return cpfCnpj;
	}	
	
	public String getNomeUsuario() {
		//
		String nome = null;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			nome = usuarioLogado.getMpUsuario().getNome();
		}
		//
		return nome;
	}
	
	public Boolean getIndUserWebUsuario() {
		//
		Boolean idnUserWeb = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			idnUserWeb = usuarioLogado.getMpUsuario().getIndUserWeb();
		}
		//
		return idnUserWeb;
	}

	@Produces
	@MpUsuarioLogado
	public MpUsuarioSistema getMpUsuarioLogado() {
		//
		MpUsuarioSistema mpUsuarioSistema = null;
		
		try {
			//
			UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) 
					FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
			
			if (auth != null && auth.getPrincipal() != null) {
				mpUsuarioSistema = (MpUsuarioSistema) auth.getPrincipal();
			}
			//
		} catch(Exception e) {
			//
			return null;
		}
		//
		return mpUsuarioSistema;
	}
	
	public boolean isConcluirContatoSitePermitido() {
		//
		return externalContext.isUserInRole("ADMINISTRADORES") || externalContext.isUserInRole("ADMIN_SITE");
	}
	
	public boolean isAdministrador() {
		//
		return externalContext.isUserInRole("ADMINISTRADORES");
	}
	
	public boolean isCartorio() {
		//
		return externalContext.isUserInRole("CARTORIOS");
	}
	
	public boolean isUsuario() {
		//
		return  externalContext.isUserInRole("USUARIOS");
	}
	
	public boolean isComarca() {
		//
		return externalContext.isUserInRole("COMARCAS");
	}
	
	public boolean isIndBoleto() {
		//
		boolean indBoleto = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			indBoleto = usuarioLogado.getMpUsuario().getIndBoleto();
		}
		//
		return indBoleto;
	}
	
	public boolean isIndTitulo() {
		//
		boolean indTitulo = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			indTitulo = usuarioLogado.getMpUsuario().getIndTitulo();
		}
		//
		return indTitulo;
	}
			
	public boolean isUsuarioOf1() {
		//
		boolean indUsuarioOf1 = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) { 
			if (usuarioLogado.getMpUsuario().getLogin().indexOf("1of") >= 0)
				indUsuarioOf1 = true;
		}
		//
		return indUsuarioOf1;
	}
	
	public boolean isUsuarioOf2() {
		//
		boolean indUsuarioOf2 = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			if (usuarioLogado.getMpUsuario().getLogin().indexOf("2of") >= 0)
				indUsuarioOf2 = true;
		}
		//
		return indUsuarioOf2;
	}
	
	public boolean isUsuarioOf3() {
		//
		boolean indUsuarioOf3 = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			if (usuarioLogado.getMpUsuario().getLogin().indexOf("3of") >= 0)
				indUsuarioOf3 = true;
		}
		//
		return indUsuarioOf3;
	}
	
	public boolean isUsuarioOf4() {
		//
		boolean indUsuarioOf4 = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			if (usuarioLogado.getMpUsuario().getLogin().indexOf("4of") >= 0)
				indUsuarioOf4 = true;
		}
		//
		return indUsuarioOf4;
	}
	
	public boolean isUsuarioWeb() {
		//
		boolean indUsuarioWeb = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			if (usuarioLogado.getMpUsuario().getIndUserWeb())
				indUsuarioWeb = true;
		}
		//
		return indUsuarioWeb;
	}
	
	public boolean isUsuarioComarca() {
		//
		boolean indUsuarioComarca = false;
		
		MpUsuarioSistema usuarioLogado = getMpUsuarioLogado();
		
		if (usuarioLogado != null) {
			if (usuarioLogado.getMpUsuario().getIndUserComarca())
				indUsuarioComarca = true;
		}
		//
		return indUsuarioComarca;
	}
	
	// ---
	
	public boolean isIndComarca() {
		//
		boolean indComarca = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaComarca");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indComarca = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indComarca;
	}
	
	public boolean isIndCancelamento() {
		//
		boolean indCancelamento = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaCancelamento");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indCancelamento = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indCancelamento;
	}
	
	public boolean isIndEnviaEmailBoleto() {
		//
		boolean indEnviaEmailBoleto = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indEnviaEmailBoleto");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indEnviaEmailBoleto = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indEnviaEmailBoleto;
	}
	
	public boolean isIndEnviaEmailBoleto(String oficio) {
		//
		boolean indEnviaEmailBoleto = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + oficio + "_IndEnviaEmailBoleto");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indEnviaEmailBoleto = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indEnviaEmailBoleto;
	}

	public boolean isIndAtivaCartaoCielo() {
		//
		boolean indAtivaCartaoCielo = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaCartaoCielo");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indAtivaCartaoCielo = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indAtivaCartaoCielo;
	}
	
	public boolean isIndAtivaCartaoCieloBoleto(String oficio) {
		//
		boolean indAtivaCartaoCielo = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + oficio + "_IndAtivaCieloBoleto");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indAtivaCartaoCielo = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indAtivaCartaoCielo;
	}
	
	public boolean isIndAtivaCartaoCieloCancelamento(String oficio) {
		//
		boolean indAtivaCartaoCieloCancelamento = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("Of" + oficio + "_IndAtivaCieloCancelamento");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indAtivaCartaoCieloCancelamento = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indAtivaCartaoCieloCancelamento;
	}

	public boolean isIndAtivaIntima21() {
		//
		boolean indAtivaIntima21 = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaIntima21");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indAtivaIntima21 = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indAtivaIntima21;
	}

	public boolean isIndAtivaCertidaoEletronica() {
		//
		boolean indAtivaCertidaoEletronica = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indAtivaCertidaoEletronica");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indAtivaCertidaoEletronica = mpSistemaConfig.getIndValor();
    	}
		//
//    	System.out.println("MpSeguranca.isIndComarca() ( " + indComarca);
    	
		return indAtivaCertidaoEletronica;
	}
		
	public String capturaNumeroIpServidor() {
		//
		String numeroIpX = "";
		
		InetAddress ip;
		//
		try {
			//
			ip = InetAddress.getLocalHost();
			
			numeroIpX = ip.getHostAddress();
			//
		} catch (UnknownHostException e) {
			//
			e.printStackTrace();
		}
		//
		return numeroIpX;
	}
	
	public String mostraServidorTeste() {
		//
		String txtTeste = "";
		
		String numeroIpX = this.capturaNumeroIpServidor();
		
		if (!numeroIpX.contentEquals("200.150.203.198"))
			txtTeste = " ( TESTES )";
		//
		return txtTeste;
	}
	
	public boolean isIndLoginCancelamento() {
		//
		boolean indLoginCancelamento = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indLoginCancelamento");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indLoginCancelamento = mpSistemaConfig.getIndValor();
    	}
		//
		return indLoginCancelamento;
	}
	
	public boolean isIndLoginBoleto() {
		//
		boolean indLoginBoleto = false;
		
    	MpSistemaConfig mpSistemaConfig = mpSistemaConfigs.porParametro("indLoginBoleto");
    	if (null == mpSistemaConfig)
    		assert(true);
    	else {
    		//
    		indLoginBoleto = mpSistemaConfig.getIndValor();
    	}
		//
		return indLoginBoleto;
	}
	
	
	// ---
	
	public String getSistemaURL() { return sistemaURL; }
	public void setSistemaURL(String sistemaURL) { this.sistemaURL = sistemaURL; }
	
	public String getFreshdeskURL() { return freshdeskURL; }
	public void setFreshdeskURL(String freshdeskURL) { this.sistemaURL = freshdeskURL; }
	
	public String getSistemaVersao() { return sistemaVersao; }
	public void setSistemaVersao(String sistemaVersao) { this.sistemaVersao = sistemaVersao; }
	
	public String getSistemaHora() {
		SimpleDateFormat sdfDMY_HMS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return sdfDMY_HMS.format(new Date());
	}
	public void setSistemaHora(String sistemaHora) { this.sistemaHora = sistemaHora; }
	
	public String getLogoMenu() { return logoMenu; }
	public void setLogoMenu(String logoMenu) { this.logoMenu = logoMenu; }
	
	public String getLogoSistema() { return logoSistema; }
	public void setLogoSistema(String logoSistema) { this.logoSistema = logoSistema; }
	
	public Boolean getIndMySQL() { return indMySQL; }
	public void setIndMySQL(Boolean indMySQL) { this.indMySQL = indMySQL; }
	
	public Boolean getIndSabadoDomingo() { return indSabadoDomingo; }
	public void setIndSabadoDomingo(Boolean indSabadoDomingo) { this.indSabadoDomingo = indSabadoDomingo; }

	public String getTokenJwt() { return tokenJwt; }
	public void setTokenJwt(String tokenJwt) { this.tokenJwt = tokenJwt; }
	
	// CIELO Api 3.0 ..
	
	public String getCieloRequisicaoSandboxURL() { return cieloRequisicaoSandboxURL; }
	public void setCieloRequisicaoSandboxURL(String cieloRequisicaoSandboxURL) { 
														this.cieloRequisicaoSandboxURL = cieloRequisicaoSandboxURL; }

	public String getCieloConsultaSandboxURL() { return cieloConsultaSandboxURL; }
	public void setCieloConsultaSandboxURL(String cieloConsultaSandboxURL) { 
															this.cieloConsultaSandboxURL = cieloConsultaSandboxURL; }

	public String getCieloRequisicaoProducaoURL() { return cieloRequisicaoProducaoURL; }
	public void setCieloRequisicaoProducaoURL(String cieloRequisicaoProducaoURL) {
													this.cieloRequisicaoProducaoURL = cieloRequisicaoProducaoURL; }

	public String getCieloConsultaProducaoURL() { return cieloConsultaProducaoURL; }
	public void setCieloConsultaProducaoURL(String cieloConsultaProducaoURL) { 
														this.cieloConsultaProducaoURL = cieloConsultaProducaoURL; }

	public String getCieloMerchantId() { return cieloMerchantId; }
	public void setCieloMerchantId(String cieloMerchantId) { this.cieloMerchantId = cieloMerchantId; }

	public String getCieloMerchantKey() { return cieloMerchantKey; }
	public void setCieloMerchantKey(String cieloMerchantKey) { this.cieloMerchantKey = cieloMerchantKey; }

	public String getCieloCartaoTeste() { return cieloCartaoTeste; }
	public void setCieloCartaoTeste(String cieloCartaoTeste) { this.cieloCartaoTeste = cieloCartaoTeste; }
	
	// Intima21 API WSDL..	
	public String getIntima21DevURL() { return intima21DevURL; }
	public void setIntima21DevURL(String intima21DevURL) { this.intima21DevURL = intima21DevURL; }
	
	public String getIntima21ProdURL() { return intima21ProdURL; }
	public void setIntima21ProdURL(String intima21ProdURL) { this.intima21ProdURL = intima21ProdURL; }
	
	// SaveInCloud S3.Minio ..	
	public String getSicMinioS3URL() { return sicMinioS3URL; }
	public void setSicMinioS3URL(String sicMinioS3URL) { this.sicMinioS3URL = sicMinioS3URL; }

	public String getSicMinioS3User() { return sicMinioS3User; }
	public void setSicMinioS3User(String sicMinioS3User) { this.sicMinioS3User = sicMinioS3User; }

	public String getSicMinioS3Passw() { return sicMinioS3Passw; }
	public void setSicMinioS3Passw(String sicMinioS3Passw) { this.sicMinioS3Passw = sicMinioS3Passw; }

}