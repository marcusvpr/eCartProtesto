/*
 * An XML document type.
 * Localname: ConsultaAutorizacaoResponse
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaAutorizacaoResponse(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaAutorizacaoResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaAutorizacaoResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTAAUTORIZACAORESPONSE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaAutorizacaoResponse");
    
    
    /**
     * Gets the "ConsultaAutorizacaoResponse" element
     */
    public br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse getConsultaAutorizacaoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse target = null;
            target = (br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse)get_store().find_element_user(CONSULTAAUTORIZACAORESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaAutorizacaoResponse" element
     */
    public void setConsultaAutorizacaoResponse(br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse consultaAutorizacaoResponse)
    {
        generatedSetterHelperImpl(consultaAutorizacaoResponse, CONSULTAAUTORIZACAORESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaAutorizacaoResponse" element
     */
    public br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse addNewConsultaAutorizacaoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse target = null;
            target = (br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse)get_store().add_element_user(CONSULTAAUTORIZACAORESPONSE$0);
            return target;
        }
    }
    /**
     * An XML ConsultaAutorizacaoResponse(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaAutorizacaoResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaAutorizacaoResponseDocument.ConsultaAutorizacaoResponse
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaAutorizacaoResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public java.lang.String getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "return" element
         */
        public org.apache.xmlbeans.XmlString xgetReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RETURN$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "return" element
         */
        public boolean isNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(java.lang.String xreturn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RETURN$0);
                }
                target.setStringValue(xreturn);
            }
        }
        
        /**
         * Sets (as xml) the "return" element
         */
        public void xsetReturn(org.apache.xmlbeans.XmlString xreturn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(RETURN$0);
                }
                target.set(xreturn);
            }
        }
        
        /**
         * Nils the "return" element
         */
        public void setNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}
