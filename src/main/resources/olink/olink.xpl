<?xml version="1.0"?>

<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" 
    version="1.0"
		xmlns:cx="http://xmlcalabash.com/ns/extensions">

  <p:input port="parameters" kind="parameter" />
  <p:option name="mavenBuildDir"/>
  <p:option name="olinkManifest"/>

  <p:import href="http://xmlcalabash.com/extension/steps/library-1.0.xpl"/>

  <p:load name="olinkxml">
    <p:with-option name="href" select="$olinkManifest"/>
  </p:load>
  
  <p:for-each xmlns:ol="http://docs.rackspace.com/olink">
    <p:iteration-source select="/ol:books/ol:book"/>
    <cx:message>
      <p:with-option name="message" select="concat('Loading: ', resolve-uri(//@path, base-uri(.)))"/>
    </cx:message>
    <p:load>
      <p:with-option name="href" select="resolve-uri(//@path, base-uri(.))"/>
    </p:load>
    <p:xinclude/>
  </p:for-each>

  <p:wrap-sequence wrapper="books" wrapper-namespace="http://docs.rackspace.com/olink" name="wrapit"/>

  <!-- Apply the Transform -->
  <p:xslt>
    <p:input port="stylesheet">
      <p:document href="make-olink-db.xsl" />
    </p:input>
  </p:xslt>

  <cx:message>
    <p:with-option name="message" select="concat('Olink db Created:', $olinkManifest)"/>
  </cx:message>

  <!-- Output to file -->
  <p:store method="xml" ><!-- indent="true -->
    <p:with-option name="href" select="concat($mavenBuildDir,'/olink.db')"/>
  </p:store>

</p:declare-step>
