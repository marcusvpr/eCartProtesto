/*
 * An XML document type.
 * Localname: CancelamentoResponse
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.CancelamentoResponseDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one CancelamentoResponse(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class CancelamentoResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.CancelamentoResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public CancelamentoResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CANCELAMENTORESPONSE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "CancelamentoResponse");
    
    
    /**
     * Gets the "CancelamentoResponse" element
     */
    public br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse getCancelamentoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse target = null;
            target = (br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse)get_store().find_element_user(CANCELAMENTORESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CancelamentoResponse" element
     */
    public void setCancelamentoResponse(br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse cancelamentoResponse)
    {
        generatedSetterHelperImpl(cancelamentoResponse, CANCELAMENTORESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "CancelamentoResponse" element
     */
    public br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse addNewCancelamentoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse target = null;
            target = (br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse)get_store().add_element_user(CANCELAMENTORESPONSE$0);
            return target;
        }
    }
    /**
     * An XML CancelamentoResponse(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class CancelamentoResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.CancelamentoResponseDocument.CancelamentoResponse
    {
        private static final long serialVersionUID = 1L;
        
        public CancelamentoResponseImpl(org.apache.xmlbeans.SchemaType sType)
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
