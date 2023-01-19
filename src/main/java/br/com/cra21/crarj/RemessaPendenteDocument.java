/*
 * An XML document type.
 * Localname: RemessaPendente
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.RemessaPendenteDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj;


/**
 * A document containing one RemessaPendente(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public interface RemessaPendenteDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(RemessaPendenteDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("remessapendentef625doctype");
    
    /**
     * Gets the "RemessaPendente" element
     */
    br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente getRemessaPendente();
    
    /**
     * Sets the "RemessaPendente" element
     */
    void setRemessaPendente(br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente remessaPendente);
    
    /**
     * Appends and returns a new empty "RemessaPendente" element
     */
    br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente addNewRemessaPendente();
    
    /**
     * An XML RemessaPendente(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public interface RemessaPendente extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(RemessaPendente.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("remessapendentedabeelemtype");
        
        /**
         * Gets the "userArq" element
         */
        java.lang.String getUserArq();
        
        /**
         * Gets (as xml) the "userArq" element
         */
        org.apache.xmlbeans.XmlString xgetUserArq();
        
        /**
         * Tests for nil "userArq" element
         */
        boolean isNilUserArq();
        
        /**
         * Sets the "userArq" element
         */
        void setUserArq(java.lang.String userArq);
        
        /**
         * Sets (as xml) the "userArq" element
         */
        void xsetUserArq(org.apache.xmlbeans.XmlString userArq);
        
        /**
         * Nils the "userArq" element
         */
        void setNilUserArq();
        
        /**
         * Gets the "downloadImagem" element
         */
        java.lang.String getDownloadImagem();
        
        /**
         * Gets (as xml) the "downloadImagem" element
         */
        org.apache.xmlbeans.XmlString xgetDownloadImagem();
        
        /**
         * Tests for nil "downloadImagem" element
         */
        boolean isNilDownloadImagem();
        
        /**
         * Sets the "downloadImagem" element
         */
        void setDownloadImagem(java.lang.String downloadImagem);
        
        /**
         * Sets (as xml) the "downloadImagem" element
         */
        void xsetDownloadImagem(org.apache.xmlbeans.XmlString downloadImagem);
        
        /**
         * Nils the "downloadImagem" element
         */
        void setNilDownloadImagem();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente newInstance() {
              return (br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.com.cra21.crarj.RemessaPendenteDocument.RemessaPendente) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.com.cra21.crarj.RemessaPendenteDocument newInstance() {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.RemessaPendenteDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.RemessaPendenteDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
