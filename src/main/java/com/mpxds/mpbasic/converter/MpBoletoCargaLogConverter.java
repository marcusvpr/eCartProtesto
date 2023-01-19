package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpBoletoCargaLog;
import com.mpxds.mpbasic.repository.MpBoletoCargaLogs;

@FacesConverter(forClass = MpBoletoCargaLog.class)
public class MpBoletoCargaLogConverter implements Converter {
	//
	@Inject
	private MpBoletoCargaLogs mpBoletoCargaLogs;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpBoletoCargaLog retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpBoletoCargaLogs.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpBoletoCargaLog) value).getId().toString();
		}
		//
		return "";
	}

}