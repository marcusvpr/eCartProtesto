/*
 * An XML document type.
 * Localname: Cancelamento
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.CancelamentoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one Cancelamento(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class CancelamentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.CancelamentoDocument
{
    private static final long serialVersionUID = 1L;
    
    public CancelamentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CANCELAMENTO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "Cancelamento");
    
    
    /**
     * Gets the "Cancelamento" element
     */
    public br.com.cra21.crarj.CancelamentoDocument.Cancelamento getCancelamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.CancelamentoDocument.Cancelamento target = null;
            target = (br.com.cra21.crarj.CancelamentoDocument.Cancelamento)get_store().find_element_user(CANCELAMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Cancelamento" element
     */
    public void setCancelamento(br.com.cra21.crarj.CancelamentoDocument.Cancelamento cancelamento)
    {
        generatedSetterHelperImpl(cancelamento, CANCELAMENTO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Cancelamento" element
     */
    public br.com.cra21.crarj.CancelamentoDocument.Cancelamento addNewCancelamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.CancelamentoDocument.Cancelamento target = null;
            target = (br.com.cra21.crarj.CancelamentoDocument.Cancelamento)get_store().add_element_user(CANCELAMENTO$0);
            return target;
        }
    }
    /**
     * An XML Cancelamento(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class CancelamentoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.CancelamentoDocument.Cancelamento
    {
        private static final long serialVersionUID = 1L;
        
        public CancelamentoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERARQ$0 = 
            new javax.xml.namespace.QName("", "userArq");
        private static final javax.xml.namespace.QName DOWNLOADIMAGEM$2 = 
            new javax.xml.namespace.QName("", "downloadImagem");
        
        
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
         * Gets the "downloadImagem" element
         */
        public java.lang.String getDownloadImagem()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "downloadImagem" element
         */
        public org.apache.xmlbeans.XmlString xgetDownloadImagem()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "downloadImagem" element
         */
        public boolean isNilDownloadImagem()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "downloadImagem" element
         */
        public void setDownloadImagem(java.lang.String downloadImagem)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DOWNLOADIMAGEM$2);
                }
                target.setStringValue(downloadImagem);
            }
        }
        
        /**
         * Sets (as xml) the "downloadImagem" element
         */
        public void xsetDownloadImagem(org.apache.xmlbeans.XmlString downloadImagem)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DOWNLOADIMAGEM$2);
                }
                target.set(downloadImagem);
            }
        }
        
        /**
         * Nils the "downloadImagem" element
         */
        public void setNilDownloadImagem()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DOWNLOADIMAGEM$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DOWNLOADIMAGEM$2);
                }
                target.setNil();
            }
        }
    }
}
