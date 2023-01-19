/*
 * An XML document type.
 * Localname: AutorizacaoResponse
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.AutorizacaoResponseDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one AutorizacaoResponse(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class AutorizacaoResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.AutorizacaoResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public AutorizacaoResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AUTORIZACAORESPONSE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "AutorizacaoResponse");
    
    
    /**
     * Gets the "AutorizacaoResponse" element
     */
    public br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse getAutorizacaoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse target = null;
            target = (br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse)get_store().find_element_user(AUTORIZACAORESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AutorizacaoResponse" element
     */
    public void setAutorizacaoResponse(br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse autorizacaoResponse)
    {
        generatedSetterHelperImpl(autorizacaoResponse, AUTORIZACAORESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "AutorizacaoResponse" element
     */
    public br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse addNewAutorizacaoResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse target = null;
            target = (br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse)get_store().add_element_user(AUTORIZACAORESPONSE$0);
            return target;
        }
    }
    /**
     * An XML AutorizacaoResponse(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class AutorizacaoResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.AutorizacaoResponseDocument.AutorizacaoResponse
    {
        private static final long serialVersionUID = 1L;
        
        public AutorizacaoResponseImpl(org.apache.xmlbeans.SchemaType sType)
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
