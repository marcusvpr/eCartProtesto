package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpTituloCargaLog;
import com.mpxds.mpbasic.repository.MpTituloCargaLogs;

@FacesConverter(forClass = MpTituloCargaLog.class)
public class MpTituloCargaLogConverter implements Converter {
	//
	@Inject
	private MpTituloCargaLogs mpTituloCargaLogs;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpTituloCargaLog retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpTituloCargaLogs.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpTituloCargaLog) value).getId().toString();
		}
		//
		return "";
	}

}