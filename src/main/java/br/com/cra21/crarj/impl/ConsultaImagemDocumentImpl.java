/*
 * An XML document type.
 * Localname: ConsultaImagem
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaImagemDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one ConsultaImagem(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class ConsultaImagemDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaImagemDocument
{
    private static final long serialVersionUID = 1L;
    
    public ConsultaImagemDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSULTAIMAGEM$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "ConsultaImagem");
    
    
    /**
     * Gets the "ConsultaImagem" element
     */
    public br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem getConsultaImagem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem target = null;
            target = (br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem)get_store().find_element_user(CONSULTAIMAGEM$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConsultaImagem" element
     */
    public void setConsultaImagem(br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem consultaImagem)
    {
        generatedSetterHelperImpl(consultaImagem, CONSULTAIMAGEM$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ConsultaImagem" element
     */
    public br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem addNewConsultaImagem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem target = null;
            target = (br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem)get_store().add_element_user(CONSULTAIMAGEM$0);
            return target;
        }
    }
    /**
     * An XML ConsultaImagem(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class ConsultaImagemImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.ConsultaImagemDocument.ConsultaImagem
    {
        private static final long serialVersionUID = 1L;
        
        public ConsultaImagemImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName NOSSONUMERO$0 = 
            new javax.xml.namespace.QName("", "nossoNumero");
        private static final javax.xml.namespace.QName NUMEROTITULO$2 = 
            new javax.xml.namespace.QName("", "numeroTitulo");
        private static final javax.xml.namespace.QName DOCUMENTODEVEDOR$4 = 
            new javax.xml.namespace.QName("", "documentoDevedor");
        private static final javax.xml.namespace.QName SALDOTITULO$6 = 
            new javax.xml.namespace.QName("", "saldoTitulo");
        private static final javax.xml.namespace.QName VENCIMENTO$8 = 
            new javax.xml.namespace.QName("", "vencimento");
        private static final javax.xml.namespace.QName REMESSA$10 = 
            new javax.xml.namespace.QName("", "remessa");
        
        
        /**
         * Gets the "nossoNumero" element
         */
        public java.lang.String getNossoNumero()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOSSONUMERO$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "nossoNumero" element
         */
        public org.apache.xmlbeans.XmlString xgetNossoNumero()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOSSONUMERO$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "nossoNumero" element
         */
        public boolean isNilNossoNumero()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOSSONUMERO$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "nossoNumero" element
         */
        public void setNossoNumero(java.lang.String nossoNumero)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NOSSONUMERO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NOSSONUMERO$0);
                }
                target.setStringValue(nossoNumero);
            }
        }
        
        /**
         * Sets (as xml) the "nossoNumero" element
         */
        public void xsetNossoNumero(org.apache.xmlbeans.XmlString nossoNumero)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOSSONUMERO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOSSONUMERO$0);
                }
                target.set(nossoNumero);
            }
        }
        
        /**
         * Nils the "nossoNumero" element
         */
        public void setNilNossoNumero()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NOSSONUMERO$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NOSSONUMERO$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "numeroTitulo" element
         */
        public java.lang.String getNumeroTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMEROTITULO$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "numeroTitulo" element
         */
        public org.apache.xmlbeans.XmlString xgetNumeroTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROTITULO$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "numeroTitulo" element
         */
        public boolean isNilNumeroTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROTITULO$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "numeroTitulo" element
         */
        public void setNumeroTitulo(java.lang.String numeroTitulo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NUMEROTITULO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NUMEROTITULO$2);
                }
                target.setStringValue(numeroTitulo);
            }
        }
        
        /**
         * Sets (as xml) the "numeroTitulo" element
         */
        public void xsetNumeroTitulo(org.apache.xmlbeans.XmlString numeroTitulo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROTITULO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMEROTITULO$2);
                }
                target.set(numeroTitulo);
            }
        }
        
        /**
         * Nils the "numeroTitulo" element
         */
        public void setNilNumeroTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NUMEROTITULO$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NUMEROTITULO$2);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "documentoDevedor" element
         */
        public java.lang.String getDocumentoDevedor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "documentoDevedor" element
         */
        public org.apache.xmlbeans.XmlString xgetDocumentoDevedor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "documentoDevedor" element
         */
        public boolean isNilDocumentoDevedor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "documentoDevedor" element
         */
        public void setDocumentoDevedor(java.lang.String documentoDevedor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DOCUMENTODEVEDOR$4);
                }
                target.setStringValue(documentoDevedor);
            }
        }
        
        /**
         * Sets (as xml) the "documentoDevedor" element
         */
        public void xsetDocumentoDevedor(org.apache.xmlbeans.XmlString documentoDevedor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DOCUMENTODEVEDOR$4);
                }
                target.set(documentoDevedor);
            }
        }
        
        /**
         * Nils the "documentoDevedor" element
         */
        public void setNilDocumentoDevedor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOCUMENTODEVEDOR$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DOCUMENTODEVEDOR$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "saldoTitulo" element
         */
        public java.lang.String getSaldoTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SALDOTITULO$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "saldoTitulo" element
         */
        public org.apache.xmlbeans.XmlString xgetSaldoTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SALDOTITULO$6, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "saldoTitulo" element
         */
        public boolean isNilSaldoTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SALDOTITULO$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "saldoTitulo" element
         */
        public void setSaldoTitulo(java.lang.String saldoTitulo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SALDOTITULO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SALDOTITULO$6);
                }
                target.setStringValue(saldoTitulo);
            }
        }
        
        /**
         * Sets (as xml) the "saldoTitulo" element
         */
        public void xsetSaldoTitulo(org.apache.xmlbeans.XmlString saldoTitulo)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SALDOTITULO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SALDOTITULO$6);
                }
                target.set(saldoTitulo);
            }
        }
        
        /**
         * Nils the "saldoTitulo" element
         */
        public void setNilSaldoTitulo()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SALDOTITULO$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SALDOTITULO$6);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "vencimento" element
         */
        public java.lang.String getVencimento()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VENCIMENTO$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "vencimento" element
         */
        public org.apache.xmlbeans.XmlString xgetVencimento()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VENCIMENTO$8, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "vencimento" element
         */
        public boolean isNilVencimento()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VENCIMENTO$8, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "vencimento" element
         */
        public void setVencimento(java.lang.String vencimento)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VENCIMENTO$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VENCIMENTO$8);
                }
                target.setStringValue(vencimento);
            }
        }
        
        /**
         * Sets (as xml) the "vencimento" element
         */
        public void xsetVencimento(org.apache.xmlbeans.XmlString vencimento)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VENCIMENTO$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VENCIMENTO$8);
                }
                target.set(vencimento);
            }
        }
        
        /**
         * Nils the "vencimento" element
         */
        public void setNilVencimento()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VENCIMENTO$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VENCIMENTO$8);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "remessa" element
         */
        public java.lang.String getRemessa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REMESSA$10, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "remessa" element
         */
        public org.apache.xmlbeans.XmlString xgetRemessa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REMESSA$10, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "remessa" element
         */
        public boolean isNilRemessa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REMESSA$10, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "remessa" element
         */
        public void setRemessa(java.lang.String remessa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REMESSA$10, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REMESSA$10);
                }
                target.setStringValue(remessa);
            }
        }
        
        /**
         * Sets (as xml) the "remessa" element
         */
        public void xsetRemessa(org.apache.xmlbeans.XmlString remessa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REMESSA$10, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REMESSA$10);
                }
                target.set(remessa);
            }
        }
        
        /**
         * Nils the "remessa" element
         */
        public void setNilRemessa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REMESSA$10, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REMESSA$10);
                }
                target.setNil();
            }
        }
    }
}
