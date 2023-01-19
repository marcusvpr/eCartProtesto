/*
 * An XML document type.
 * Localname: BuscarApresentante
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.BuscarApresentanteDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one BuscarApresentante(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class BuscarApresentanteDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.BuscarApresentanteDocument
{
    private static final long serialVersionUID = 1L;
    
    public BuscarApresentanteDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BUSCARAPRESENTANTE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "BuscarApresentante");
    
    
    /**
     * Gets the "BuscarApresentante" element
     */
    public br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante getBuscarApresentante()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante target = null;
            target = (br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante)get_store().find_element_user(BUSCARAPRESENTANTE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "BuscarApresentante" element
     */
    public void setBuscarApresentante(br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante buscarApresentante)
    {
        generatedSetterHelperImpl(buscarApresentante, BUSCARAPRESENTANTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "BuscarApresentante" element
     */
    public br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante addNewBuscarApresentante()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante target = null;
            target = (br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante)get_store().add_element_user(BUSCARAPRESENTANTE$0);
            return target;
        }
    }
    /**
     * An XML BuscarApresentante(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class BuscarApresentanteImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.BuscarApresentanteDocument.BuscarApresentante
    {
        private static final long serialVersionUID = 1L;
        
        public BuscarApresentanteImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        
    }
}
