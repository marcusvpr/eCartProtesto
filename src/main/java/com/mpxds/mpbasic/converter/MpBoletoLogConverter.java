package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpBoletoLog;
import com.mpxds.mpbasic.repository.MpBoletoLogs;

@FacesConverter(forClass = MpBoletoLog.class)
public class MpBoletoLogConverter implements Converter {
	//
	@Inject
	private MpBoletoLogs mpBoletoLogs;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpBoletoLog retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpBoletoLogs.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpBoletoLog) value).getId().toString();
		}
		//
		return "";
	}

}