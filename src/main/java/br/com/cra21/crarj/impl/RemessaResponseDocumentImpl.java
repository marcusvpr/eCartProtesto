/*
 * An XML document type.
 * Localname: RemessaResponse
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.RemessaResponseDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one RemessaResponse(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class RemessaResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.RemessaResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public RemessaResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REMESSARESPONSE$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "RemessaResponse");
    
    
    /**
     * Gets the "RemessaResponse" element
     */
    public br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse getRemessaResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse target = null;
            target = (br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse)get_store().find_element_user(REMESSARESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RemessaResponse" element
     */
    public void setRemessaResponse(br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse remessaResponse)
    {
        generatedSetterHelperImpl(remessaResponse, REMESSARESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RemessaResponse" element
     */
    public br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse addNewRemessaResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse target = null;
            target = (br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse)get_store().add_element_user(REMESSARESPONSE$0);
            return target;
        }
    }
    /**
     * An XML RemessaResponse(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class RemessaResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.RemessaResponseDocument.RemessaResponse
    {
        private static final long serialVersionUID = 1L;
        
        public RemessaResponseImpl(org.apache.xmlbeans.SchemaType sType)
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
