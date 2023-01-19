package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpTitulo;
import com.mpxds.mpbasic.repository.MpTitulos;

@FacesConverter(forClass = MpTitulo.class)
public class MpTituloConverter implements Converter {
	//
	@Inject
	private MpTitulos mpTitulos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpTitulo retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpTitulos.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpTitulo) value).getId().toString();
		}
		//
		return "";
	}

}