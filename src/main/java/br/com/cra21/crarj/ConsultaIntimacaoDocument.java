/*
 * An XML document type.
 * Localname: ConsultaIntimacao
 * Namespace: urn:crarj.cra21.com.br
 * Java type: br.com.cra21.crarj.ConsultaIntimacaoDocument
 *
 * Automatically generated - do not modify.
 */
package br.com.cra21.crarj;


/**
 * A document containing one ConsultaIntimacao(@urn:crarj.cra21.com.br) element.
 *
 * This is a complex type.
 */
public interface ConsultaIntimacaoDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConsultaIntimacaoDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("consultaintimacao4852doctype");
    
    /**
     * Gets the "ConsultaIntimacao" element
     */
    br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao getConsultaIntimacao();
    
    /**
     * Sets the "ConsultaIntimacao" element
     */
    void setConsultaIntimacao(br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao consultaIntimacao);
    
    /**
     * Appends and returns a new empty "ConsultaIntimacao" element
     */
    br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao addNewConsultaIntimacao();
    
    /**
     * An XML ConsultaIntimacao(@urn:crarj.cra21.com.br).
     *
     * This is a complex type.
     */
    public interface ConsultaIntimacao extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConsultaIntimacao.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9D9D682E079D2FBB94B35786AFC37305").resolveHandle("consultaintimacao8898elemtype");
        
        /**
         * Gets the "userDados" element
         */
        java.lang.String getUserDados();
        
        /**
         * Gets (as xml) the "userDados" element
         */
        org.apache.xmlbeans.XmlString xgetUserDados();
        
        /**
         * Tests for nil "userDados" element
         */
        boolean isNilUserDados();
        
        /**
         * Sets the "userDados" element
         */
        void setUserDados(java.lang.String userDados);
        
        /**
         * Sets (as xml) the "userDados" element
         */
        void xsetUserDados(org.apache.xmlbeans.XmlString userDados);
        
        /**
         * Nils the "userDados" element
         */
        void setNilUserDados();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao newInstance() {
              return (br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (br.com.cra21.crarj.ConsultaIntimacaoDocument.ConsultaIntimacao) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument newInstance() {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static br.com.cra21.crarj.ConsultaIntimacaoDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (br.com.cra21.crarj.ConsultaIntimacaoDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
