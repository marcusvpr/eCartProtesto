/*
 * An XML document type.
 * Localname: ConsultaSlipResponse
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaSlipResponseDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaSlipResponse(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaSlipResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaSlipResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaSlipResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTASLIPRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaSlipResponse");
    
    
    /**
     * Gets the "ConsultaSlipResponse" element
     */
    public br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse getConsultaSlipResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse target = null;
            target = (br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse)get_store().find_element_user(CONSULTASLIPRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaSlipResponse" element
     */
    public void setConsultaSlipResponse(br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse consultaSlipResponse)
    {
        generatedSetterHelperImpl(consultaSlipResponse, CONSULTASLIPRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaSlipResponse" element
     */
    public br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse addNewConsultaSlipResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse target = null;
            target = (br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse)get_store().add_element_user(CONSULTASLIPRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML ConsultaSlipResponse(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaSlipResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaSlipResponseDocument.ConsultaSlipResponse
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaSlipResponseImpl(org.apache.xmlbeans.SchemaType sType)
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
