/*
 * An XML document type.
 * Localname: ConsultaAutorizacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaAutorizacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaAutorizacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaAutorizacaoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaAutorizacaoDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaAutorizacaoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTAAUTORIZACAO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaAutorizacao");
    
    
    /**
     * Gets the "ConsultaAutorizacao" element
     */
    public br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao getConsultaAutorizacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao target = null;
            target = (br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao)get_store().find_element_user(CONSULTAAUTORIZACAO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaAutorizacao" element
     */
    public void setConsultaAutorizacao(br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao consultaAutorizacao)
    {
        generatedSetterHelperImpl(consultaAutorizacao, CONSULTAAUTORIZACAO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaAutorizacao" element
     */
    public br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao addNewConsultaAutorizacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao target = null;
            target = (br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao)get_store().add_element_user(CONSULTAAUTORIZACAO$0);
            return target;
        }
    }
    /**
     * An XML ConsultaAutorizacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaAutorizacaoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaAutorizacaoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERARQ$0 = 
            new javax.xml.namespace.QName("", "userArq");
        private static final javax.xml.namespace.QName PROTOCOLO$2 = 
            new javax.xml.namespace.QName("", "protocolo");
        private static final javax.xml.namespace.QName DATAPROTOCOLO$4 = 
            new javax.xml.namespace.QName("", "dataProtocolo");
        private static final javax.xml.namespace.QName MARCARBAIXADO$6 = 
            new javax.xml.namespace.QName("", "marcarBaixado");
        
        
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
         * Gets the "protocolo" element
         */
        public java.lang.String getProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLO$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "protocolo" element
         */
        public org.apache.xmlbeans.XmlString xgetProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "protocolo" element
         */
        public boolean isNilProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "protocolo" element
         */
        public void setProtocolo(java.lang.String protocolo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROTOCOLO$2);
                }
                target.setStringValue(protocolo);
            }
        }
        
        /**
         * Sets (as xml) the "protocolo" element
         */
        public void xsetProtocolo(org.apache.xmlbeans.XmlString protocolo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROTOCOLO$2);
                }
                target.set(protocolo);
            }
        }
        
        /**
         * Nils the "protocolo" element
         */
        public void setNilProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROTOCOLO$2);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "dataProtocolo" element
         */
        public java.lang.String getDataProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "dataProtocolo" element
         */
        public org.apache.xmlbeans.XmlString xgetDataProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "dataProtocolo" element
         */
        public boolean isNilDataProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "dataProtocolo" element
         */
        public void setDataProtocolo(java.lang.String dataProtocolo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAPROTOCOLO$4);
                }
                target.setStringValue(dataProtocolo);
            }
        }
        
        /**
         * Sets (as xml) the "dataProtocolo" element
         */
        public void xsetDataProtocolo(org.apache.xmlbeans.XmlString dataProtocolo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAPROTOCOLO$4);
                }
                target.set(dataProtocolo);
            }
        }
        
        /**
         * Nils the "dataProtocolo" element
         */
        public void setNilDataProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAPROTOCOLO$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "marcarBaixado" element
         */
        public java.lang.String getMarcarBaixado()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MARCARBAIXADO$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "marcarBaixado" element
         */
        public org.apache.xmlbeans.XmlString xgetMarcarBaixado()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MARCARBAIXADO$6, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "marcarBaixado" element
         */
        public boolean isNilMarcarBaixado()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MARCARBAIXADO$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "marcarBaixado" element
         */
        public void setMarcarBaixado(java.lang.String marcarBaixado)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MARCARBAIXADO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MARCARBAIXADO$6);
                }
                target.setStringValue(marcarBaixado);
            }
        }
        
        /**
         * Sets (as xml) the "marcarBaixado" element
         */
        public void xsetMarcarBaixado(org.apache.xmlbeans.XmlString marcarBaixado)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MARCARBAIXADO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MARCARBAIXADO$6);
                }
                target.set(marcarBaixado);
            }
        }
        
        /**
         * Nils the "marcarBaixado" element
         */
        public void setNilMarcarBaixado()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MARCARBAIXADO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MARCARBAIXADO$6);
                }
                target.setNil();
            }
        }
    }
}
