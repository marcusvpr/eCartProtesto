/*
 * An XML document type.
 * Localname: ConsultaSlip
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaSlipDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaSlip(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaSlipDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaSlipDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaSlipDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTASLIP$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaSlip");
    
    
    /**
     * Gets the "ConsultaSlip" element
     */
    public br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip getConsultaSlip()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip target = null;
            target = (br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip)get_store().find_element_user(CONSULTASLIP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaSlip" element
     */
    public void setConsultaSlip(br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip consultaSlip)
    {
        generatedSetterHelperImpl(consultaSlip, CONSULTASLIP$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaSlip" element
     */
    public br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip addNewConsultaSlip()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip target = null;
            target = (br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip)get_store().add_element_user(CONSULTASLIP$0);
            return target;
        }
    }
    /**
     * An XML ConsultaSlip(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaSlipImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaSlipDocument.ConsultaSlip
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaSlipImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName PROTOCOLO$0 = 
            new javax.xml.namespace.QName("", "protocolo");
        private static final javax.xml.namespace.QName DATAPROTOCOLO$2 = 
            new javax.xml.namespace.QName("", "dataProtocolo");
        
        
        /**
         * Gets the "protocolo" element
         */
        public java.lang.String getProtocolo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLO$0, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$0, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$0, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROTOCOLO$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROTOCOLO$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROTOCOLO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROTOCOLO$0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAPROTOCOLO$2, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$2, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$2, 0);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATAPROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATAPROTOCOLO$2);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAPROTOCOLO$2);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATAPROTOCOLO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATAPROTOCOLO$2);
                }
                target.setNil();
            }
        }
    }
}
