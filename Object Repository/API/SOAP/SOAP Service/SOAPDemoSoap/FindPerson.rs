<?xml version="1.0" encoding="UTF-8"?>
<WebServiceRequestEntity>
   <description></description>
   <name>FindPerson</name>
   <tag></tag>
   <elementGuidId>624a48fe-ad98-43a9-a818-da94055d6db9</elementGuidId>
   <selectorMethod>BASIC</selectorMethod>
   <useRalativeImagePath>false</useRalativeImagePath>
   <autoUpdateContent>true</autoUpdateContent>
   <connectionTimeout>-1</connectionTimeout>
   <followRedirects>false</followRedirects>
   <httpBody></httpBody>
   <httpBodyContent></httpBodyContent>
   <httpBodyType></httpBodyType>
   <httpHeaderProperties>
      <isSelected>false</isSelected>
      <matchCondition>equals</matchCondition>
      <name>SOAPAction</name>
      <type>Main</type>
      <value>http://tempuri.org/SOAP.Demo.FindPerson</value>
      <webElementGuid>fc99e3fa-88fe-41dc-a06f-2ff37f93d043</webElementGuid>
   </httpHeaderProperties>
   <httpHeaderProperties>
      <isSelected>false</isSelected>
      <matchCondition>equals</matchCondition>
      <name>Content-Type</name>
      <type>Main</type>
      <value>text/xml; charset=utf-8</value>
      <webElementGuid>e51a9d96-8117-4ebc-b476-3b2fac27b72b</webElementGuid>
   </httpHeaderProperties>
   <katalonVersion>9.2.0</katalonVersion>
   <maxResponseSize>-1</maxResponseSize>
   <restRequestMethod></restRequestMethod>
   <restUrl></restUrl>
   <serviceType>SOAP</serviceType>
   <soapBody>&lt;soapenv:Envelope xmlns:soapenv=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot; xmlns:tem=&quot;http://tempuri.org&quot;>&#xd;
   &lt;soapenv:Header/>&#xd;
   &lt;soapenv:Body>&#xd;
      &lt;tem:FindPerson>&#xd;
         &lt;tem:id>gero et&lt;/tem:id>&#xd;
      &lt;/tem:FindPerson>&#xd;
   &lt;/soapenv:Body>&#xd;
&lt;/soapenv:Envelope></soapBody>
   <soapHeader></soapHeader>
   <soapRequestMethod>SOAP</soapRequestMethod>
   <soapServiceEndpoint>https://www.crcind.com:443/csp/samples/SOAP.Demo.cls</soapServiceEndpoint>
   <soapServiceFunction>FindPerson</soapServiceFunction>
   <socketTimeout>-1</socketTimeout>
   <useServiceInfoFromWsdl>false</useServiceInfoFromWsdl>
   <wsdlAddress>https://www.crcind.com/csp/samples/SOAP.Demo.CLS?WSDL=1</wsdlAddress>
</WebServiceRequestEntity>