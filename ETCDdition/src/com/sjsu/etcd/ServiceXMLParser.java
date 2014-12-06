package com.sjsu.etcd;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.boon.etcd.Etcd;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServiceXMLParser {


	File serviceConfFile;
	Etcd client;
	String strApplicationName = null;

	public ServiceXMLParser(Etcd client, File fServiceConfig) {
		this.client = client;
		this.serviceConfFile = fServiceConfig;
		this.client.createDir("applications");
	}

	public ServiceXMLParser() {
	}

	public boolean parseServiceXML(){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(serviceConfFile);
			doc.normalize();//getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			String strAppName 	= doc.getDocumentElement().getAttribute("name");
			String strHostName 	= doc.getDocumentElement().getAttribute("host");
			String strPort 		= doc.getDocumentElement().getAttribute("port");
			System.out.println(strAppName+" "+strHostName+" "+strPort);
			strApplicationName = strAppName;
			HeartBeat appHB = new HeartBeat(client, strAppName, strHostName, strPort);
			appHB.start();

			addChildServices(doc.getDocumentElement().getChildNodes());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return false;
	}

	private void addChildServices(NodeList nlChildren ){
		int iReplicaCount = 0;
		for (int i = 0; i < nlChildren.getLength(); i++){
			Node node = nlChildren.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				iReplicaCount++;
				if("replica".equalsIgnoreCase(node.getNodeName())){
					if(node.getAttributes().getNamedItem("host") != null && node.getAttributes().getNamedItem("port") != null){
						if(node.getParentNode() != null && node.getParentNode().getAttributes().getNamedItem("name") != null){
							String strServiceName = node.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
							HeartBeat serviceHB = new HeartBeat(client, strApplicationName+"/"+strServiceName+"/replica"+iReplicaCount, node.getAttributes().getNamedItem("host").getNodeValue(), node.getAttributes().getNamedItem("port").getNodeValue());
							serviceHB.start();
						}
					}
				}

				if(node.getChildNodes() != null){
					addChildServices(node.getChildNodes());
				}
			}
		}
	}

	public static void main(String[] args) {
		EtcdInitializer client = new EtcdInitializer("localhost");
		ServiceXMLParser sxp = new ServiceXMLParser(client.getClient(),null);// for testing
		sxp.serviceConfFile = new File("src/com/sjsu/etcd/appservices.xml");// for testing
		sxp.parseServiceXML();// for testing
	}

}
