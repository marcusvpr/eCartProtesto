/*
 * An XML document type.
 * Localname: Confirmacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConfirmacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one Confirmacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConfirmacaoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConfirmacaoDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConfirmacaoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONFIRMACAO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "Confirmacao");
    
    
    /**
     * Gets the "Confirmacao" element
     */
    public br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao getConfirmacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao target = null;
            target = (br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao)get_store().find_element_user(CONFIRMACAO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Confirmacao" element
     */
    public void setConfirmacao(br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao confirmacao)
    {
        generatedSetterHelperImpl(confirmacao, CONFIRMACAO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Confirmacao" element
     */
    public br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao addNewConfirmacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao target = null;
            target = (br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao)get_store().add_element_user(CONFIRMACAO$0);
            return target;
        }
    }
    /**
     * An XML Confirmacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConfirmacaoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConfirmacaoDocument.Confirmacao
    {
        private static final long serialVersionUID = 1L;
        
        public ConfirmacaoImpl(org.apache.xmlbeans.SchemaType sType)
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
