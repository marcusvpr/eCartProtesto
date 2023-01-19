package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpAlertaSite;
import com.mpxds.mpbasic.repository.MpAlertaSites;

@FacesConverter(forClass = MpAlertaSite.class)
public class MpAlertaSiteConverter implements Converter {
	//
	@Inject
	private MpAlertaSites alertaSites;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpAlertaSite retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.alertaSites.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpAlertaSite) value).getId().toString();
		}
		//
		return "";
	}

}