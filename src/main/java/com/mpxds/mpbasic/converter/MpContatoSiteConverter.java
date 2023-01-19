package com.mpxds.mpbasic.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.mpxds.mpbasic.model.MpContatoSite;
import com.mpxds.mpbasic.repository.MpContatoSites;

@FacesConverter(forClass = MpContatoSite.class)
public class MpContatoSiteConverter implements Converter {
	//
	@Inject
	private MpContatoSites mpContatoSites;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		//
		MpContatoSite retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			retorno = this.mpContatoSites.porId(new Long(value));
		}
		//
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		//
		if (value != null) {
			return ((MpContatoSite) value).getId().toString();
		}
		//
		return "";
	}

}