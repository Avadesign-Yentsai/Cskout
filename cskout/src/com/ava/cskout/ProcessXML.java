package com.ava.cskout;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

public class ProcessXML 
{
	public static String ReadXML(InputStream XMLinputStream,String TagName) 
    {
    	  String re = "nodata";
    	  DocumentBuilderFactory docBuilderFactory = null;
    	  DocumentBuilder docBuilder = null;
    	  Document doc = null;
    	  Context context;
    	 
    	  try 
    	  {
    		  docBuilderFactory = DocumentBuilderFactory.newInstance();
	    	  docBuilder = docBuilderFactory.newDocumentBuilder();
	    	
	    	  doc = docBuilder.parse(XMLinputStream);
	    	   
	    	  org.w3c.dom.Element root = doc.getDocumentElement();

	    	  /*
	    	  //<message data="2009-09-23"> //re=2009-09-23
	    	  NodeList nodeList = root.getElementsByTagName("message");
	    	  Node nd = nodeList.item(0);
	    	  re = nd.getAttributes().getNamedItem("date").getNodeValue();
	    	  */
	    	  
	    	  //<title>Android XML</title> //re=Android XML
	    	  NodeList nodeList = root.getElementsByTagName(TagName);
	    	  Node nd = nodeList.item(0);
	    	  re = nd.getTextContent();
	    	  
	    	  
    	  } catch (ParserConfigurationException e) 
    	  {
    		  e.printStackTrace();
    	  } catch (SAXException e) 
    	  {
    		  e.printStackTrace();
    	  } catch (IOException e)
    	  {
    		  e.printStackTrace();
    	  } finally 
    	  {
    		  doc = null;
    		  docBuilder = null;
    		  docBuilderFactory = null;
    	  }
    	  return re;
    }
}
