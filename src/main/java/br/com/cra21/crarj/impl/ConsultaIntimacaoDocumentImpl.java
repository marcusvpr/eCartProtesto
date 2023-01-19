/*
 * An XML document type.
 * Localname: ConsultaIntimacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaIntimacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaIntimacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaIntimacaoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaIntimacaoDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaIntimacaoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTAINTIMACAO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaIntimacao");
    
    
    /**
     * Gets the "ConsultaIntimacao" element
     */
    public br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao getConsultaIntimacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao target = null;
            target = (br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao)get_store().find_element_user(CONSULTAINTIMACAO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaIntimacao" element
     */
    public void setConsultaIntimacao(br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao consultaIntimacao)
    {
        generatedSetterHelperImpl(consultaIntimacao, CONSULTAINTIMACAO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaIntimacao" element
     */
    public br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao addNewConsultaIntimacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao target = null;
            target = (br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao)get_store().add_element_user(CONSULTAINTIMACAO$0);
            return target;
        }
    }
    /**
     * An XML ConsultaIntimacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaIntimacaoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaIntimacaoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERDADOS$0 = 
            new javax.xml.namespace.QName("", "userDados");
        
        
        /**
         * Gets the "userDados" element
         */
        public java.lang.String getUserDados()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERDADOS$0, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$0, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$0, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERDADOS$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERDADOS$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERDADOS$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERDADOS$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERDADOS$0);
                }
                target.setNil();
            }
        }
    }
}
