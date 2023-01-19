/*
 * An XML document type.
 * Localname: ConsultaAutorizacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaAutorizacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj;


/**
 * A document containing one ConsultaAutorizacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public interface ConsultaAutorizacaoDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConsultaAutorizacaoDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("consultaautorizacao6071doctype");
    
    /**
     * Gets the "ConsultaAutorizacao" element
     */
    br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao getConsultaAutorizacao();
    
    /**
     * Sets the "ConsultaAutorizacao" element
     */
    void setConsultaAutorizacao(br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao consultaAutorizacao);
    
    /**
     * Appends and returns a new empty "ConsultaAutorizacao" element
     */
    br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao addNewConsultaAutorizacao();
    
    /**
     * An XML ConsultaAutorizacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public interface ConsultaAutorizacao extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConsultaAutorizacao.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("consultaautorizacaod256elemtype");
        
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
         * Gets the "protocolo" element
         */
        java.lang.String getProtocolo();
        
        /**
         * Gets (as xml) the "protocolo" element
         */
        org.apache.xmlbeans.XmlString xgetProtocolo();
        
        /**
         * Tests for nil "protocolo" element
         */
        boolean isNilProtocolo();
        
        /**
         * Sets the "protocolo" element
         */
        void setProtocolo(java.lang.String protocolo);
        
        /**
         * Sets (as xml) the "protocolo" element
         */
        void xsetProtocolo(org.apache.xmlbeans.XmlString protocolo);
        
        /**
         * Nils the "protocolo" element
         */
        void setNilProtocolo();
        
        /**
         * Gets the "dataProtocolo" element
         */
        java.lang.String getDataProtocolo();
        
        /**
         * Gets (as xml) the "dataProtocolo" element
         */
        org.apache.xmlbeans.XmlString xgetDataProtocolo();
        
        /**
         * Tests for nil "dataProtocolo" element
         */
        boolean isNilDataProtocolo();
        
        /**
         * Sets the "dataProtocolo" element
         */
        void setDataProtocolo(java.lang.String dataProtocolo);
        
        /**
         * Sets (as xml) the "dataProtocolo" element
         */
        void xsetDataProtocolo(org.apache.xmlbeans.XmlString dataProtocolo);
        
        /**
         * Nils the "dataProtocolo" element
         */
        void setNilDataProtocolo();
        
        /**
         * Gets the "marcarBaixado" element
         */
        java.lang.String getMarcarBaixado();
        
        /**
         * Gets (as xml) the "marcarBaixado" element
         */
        org.apache.xmlbeans.XmlString xgetMarcarBaixado();
        
        /**
         * Tests for nil "marcarBaixado" element
         */
        boolean isNilMarcarBaixado();
        
        /**
         * Sets the "marcarBaixado" element
         */
        void setMarcarBaixado(java.lang.String marcarBaixado);
        
        /**
         * Sets (as xml) the "marcarBaixado" element
         */
        void xsetMarcarBaixado(org.apache.xmlbeans.XmlString marcarBaixado);
        
        /**
         * Nils the "marcarBaixado" element
         */
        void setNilMarcarBaixado();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao newInstance() {
              return (br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.com.cra21.crarj.ConsultaAutorizacaoDocument.ConsultaAutorizacao) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument newInstance() {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.ConsultaAutorizacaoDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.ConsultaAutorizacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
