/*
 * An XML document type.
 * Localname: Desistencia
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.DesistenciaDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj.impl;
/**
 * A document containing one Desistencia(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public class DesistenciaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.DesistenciaDocument
{
    private static final long serialVersionUID = 1L;
    
    public DesistenciaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESISTENCIA$0 = 
        new javax.xml.namespace.QName("urn:crarj.cra21.com.br", "Desistencia");
    
    
    /**
     * Gets the "Desistencia" element
     */
    public br.com.cra21.crarj.DesistenciaDocument.Desistencia getDesistencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.DesistenciaDocument.Desistencia target = null;
            target = (br.com.cra21.crarj.DesistenciaDocument.Desistencia)get_store().find_element_user(DESISTENCIA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Desistencia" element
     */
    public void setDesistencia(br.com.cra21.crarj.DesistenciaDocument.Desistencia desistencia)
    {
        generatedSetterHelperImpl(desistencia, DESISTENCIA$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Desistencia" element
     */
    public br.com.cra21.crarj.DesistenciaDocument.Desistencia addNewDesistencia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            br.com.cra21.crarj.DesistenciaDocument.Desistencia target = null;
            target = (br.com.cra21.crarj.DesistenciaDocument.Desistencia)get_store().add_element_user(DESISTENCIA$0);
            return target;
        }
    }
    /**
     * An XML Desistencia(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public static class DesistenciaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements br.com.cra21.crarj.DesistenciaDocument.Desistencia
    {
        private static final long serialVersionUID = 1L;
        
        public DesistenciaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERARQ$0 = 
            new javax.xml.namespace.QName("", "userArq");
        
        
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
    }
}
