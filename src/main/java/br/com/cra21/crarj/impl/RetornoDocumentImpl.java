/*
 * An XML document type.
 * Localname: Retorno
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.RetornoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one Retorno(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class RetornoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.RetornoDocument
{
    private static final long serialVersionUID = 1L;
    
    public RetornoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RETORNO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "Retorno");
    
    
    /**
     * Gets the "Retorno" element
     */
    public br.com.cra21.crarj.RetornoDocument.Retorno getRetorno()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.RetornoDocument.Retorno target = null;
            target = (br.com.cra21.crarj.RetornoDocument.Retorno)get_store().find_element_user(RETORNO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Retorno" element
     */
    public void setRetorno(br.com.cra21.crarj.RetornoDocument.Retorno retorno)
    {
        generatedSetterHelperImpl(retorno, RETORNO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Retorno" element
     */
    public br.com.cra21.crarj.RetornoDocument.Retorno addNewRetorno()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.RetornoDocument.Retorno target = null;
            target = (br.com.cra21.crarj.RetornoDocument.Retorno)get_store().add_element_user(RETORNO$0);
            return target;
        }
    }
    /**
     * An XML Retorno(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class RetornoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.RetornoDocument.Retorno
    {
        private static final long serialVersionUID = 1L;
        
        public RetornoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERARQ$0 = 
            new javax.xml.namespace.QName("", "userArq");
        private static final javax.xml.namespace.QName USERDADOS$2 = 
            new javax.xml.namespace.QName("", "userDados");
        
        
        /**
         * Gets the "userArq" element
         */
        public java.lang.String getUserArq()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERARQ$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "userArq" element
         */
        public org.apache.xmlbeans.XmlString xgetUserArq()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERARQ$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "userArq" element
         */
        public boolean isNilUserArq()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERARQ$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "userArq" element
         */
        public void setUserArq(java.lang.String userArq)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERARQ$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERARQ$0);
                }
                target.setStringValue(userArq);
            }
        }
        
        /**
         * Sets (as xml) the "userArq" element
         */
        public void xsetUserArq(org.apache.xmlbeans.XmlString userArq)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERARQ$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERARQ$0);
                }
                target.set(userArq);
            }
        }
        
        /**
         * Nils the "userArq" element
         */
        public void setNilUserArq()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERARQ$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERARQ$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "userDados" element
         */
        public java.lang.String getUserDados()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERDADOS$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "userDados" element
         */
        public org.apache.xmlbeans.XmlString xgetUserDados()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "userDados" element
         */
        public boolean isNilUserDados()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "userDados" element
         */
        public void setUserDados(java.lang.String userDados)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERDADOS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERDADOS$2);
                }
                target.setStringValue(userDados);
            }
        }
        
        /**
         * Sets (as xml) the "userDados" element
         */
        public void xsetUserDados(org.apache.xmlbeans.XmlString userDados)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERDADOS$2);
                }
                target.set(userDados);
            }
        }
        
        /**
         * Nils the "userDados" element
         */
        public void setNilUserDados()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERDADOS$2);
                }
                target.setNil();
            }
        }
    }
}
