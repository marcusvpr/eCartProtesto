package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpBoleto;
import com.mpxds.mpbasic.repository.MpBoletos;

@FacesConverter(forClass = MpBoleto.class)
public class MpBoletoConverter implements Converter {
	//
	@Inject
	private MpBoletos mpBoletos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpBoleto retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpBoletos.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpBoleto) value).getId().toString();
		}
		//
		return "";
	}

}