/*
 * An XML document type.
 * Localname: Autorizacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.AutorizacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one Autorizacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class AutorizacaoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.AutorizacaoDocument
{
    private static final long serialVersionUID = 1L;
    
    public AutorizacaoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AUTORIZACAO$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "Autorizacao");
    
    
    /**
     * Gets the "Autorizacao" element
     */
    public br.com.cra21.crarj.AutorizacaoDocument.Autorizacao getAutorizacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.AutorizacaoDocument.Autorizacao target = null;
            target = (br.com.cra21.crarj.AutorizacaoDocument.Autorizacao)get_store().find_element_user(AUTORIZACAO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Autorizacao" element
     */
    public void setAutorizacao(br.com.cra21.crarj.AutorizacaoDocument.Autorizacao autorizacao)
    {
        generatedSetterHelperImpl(autorizacao, AUTORIZACAO$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Autorizacao" element
     */
    public br.com.cra21.crarj.AutorizacaoDocument.Autorizacao addNewAutorizacao()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.AutorizacaoDocument.Autorizacao target = null;
            target = (br.com.cra21.crarj.AutorizacaoDocument.Autorizacao)get_store().add_element_user(AUTORIZACAO$0);
            return target;
        }
    }
    /**
     * An XML Autorizacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class AutorizacaoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.AutorizacaoDocument.Autorizacao
    {
        private static final long serialVersionUID = 1L;
        
        public AutorizacaoImpl(org.apache.xmlbeans.SchemaType sType)
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
