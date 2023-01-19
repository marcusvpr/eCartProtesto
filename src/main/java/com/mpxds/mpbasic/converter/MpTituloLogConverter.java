package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpTituloLog;
import com.mpxds.mpbasic.repository.MpTituloLogs;

@FacesConverter(forClass = MpTituloLog.class)
public class MpTituloLogConverter implements Converter {
	//
	@Inject
	private MpTituloLogs mpTituloLogs;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpTituloLog retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpTituloLogs.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpTituloLog) value).getId().toString();
		}
		//
		return "";
	}

}