package com.sjsu.etcd;

import static org.boon.Boon.puts;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServiceXMLParser {


	File serviceConfFile;
	Etcd client;
	String strApplicationName = null;
	public String getStrApplicationName() {
		return strApplicationName;
	}

	public void setStrApplicationName(String strApplicationName) {
		this.strApplicationName = strApplicationName;
	}

	String strEtcdAppPath = null;
	ArrayList<String> alServiceList = null;
	static HashMap<String, ServicesGroup> hmService = new HashMap<String, ServicesGroup>();

	public ServiceXMLParser(Etcd client, File fServiceConfig) {
		alServiceList = new ArrayList<String>();
		this.client = client;
		this.serviceConfFile = fServiceConfig;
		this.client.createDir("applications");
	}

	public ServiceXMLParser() {
	}

	public boolean parseServiceXML(){
		boolean bParseSuccess = true;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(serviceConfFile);
			doc.normalize();//getDocumentElement().normalize();

			String strAppName 	= doc.getDocumentElement().getAttribute("name");
			String strHostName 	= doc.getDocumentElement().getAttribute("host");
			String strPort 		= doc.getDocumentElement().getAttribute("port");
			strApplicationName = strAppName;
			strEtcdAppPath = "applications/"+strApplicationName;
			client.createDir(strEtcdAppPath);

			addChildServices(doc.getDocumentElement().getChildNodes());

		} catch (ParserConfigurationException e) {
			bParseSuccess = false;
			e.printStackTrace();
		} catch (SAXException e) {
			bParseSuccess = false;
			e.printStackTrace();
		} catch (IOException e) {
			bParseSuccess = false;
			e.printStackTrace();
		}
		return bParseSuccess;
	}

	private void addChildServices(NodeList nlChildren ){
		try{
		int iReplicaCount = 0;
		String strPreviousService = "";
		for (int i = 0; i < nlChildren.getLength(); i++){
			Node node = nlChildren.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				iReplicaCount++;
				if("service".equalsIgnoreCase(node.getNodeName())){
					client.createDir("applications/"+ strApplicationName +"/"+ node.getAttributes().getNamedItem("name").getNodeValue());
					strPreviousService = "applications/"+ strApplicationName +"/"+ node.getAttributes().getNamedItem("name").getNodeValue();
				}
				if("replica".equalsIgnoreCase(node.getNodeName())){
					if(node.getAttributes().getNamedItem("host") != null && node.getAttributes().getNamedItem("port") != null){
						if(node.getParentNode() != null && node.getParentNode().getAttributes().getNamedItem("name") != null){
							String strServiceName = node.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
							HeartBeat serviceHB = new HeartBeat(client, strApplicationName+"/"+strServiceName+"/replica"+iReplicaCount, node.getAttributes().getNamedItem("host").getNodeValue(), node.getAttributes().getNamedItem("port").getNodeValue());
							alServiceList.add("replica"+iReplicaCount);
							serviceHB.start();
						}
					}
				}
				if(node.getChildNodes() != null){
					addChildServices(node.getChildNodes());
					if(alServiceList != null && alServiceList.size() > 0 && "service".equalsIgnoreCase(node.getNodeName())){
						ServicesGroup sg = new ServicesGroup(client, strPreviousService, (ArrayList<String>)alServiceList.clone());
						sg.electLeader();
						hmService.put(strPreviousService, sg);
						alServiceList.clear();
					}
				}
			}
		}
		}
		catch(Exception ex){
			System.out.println("Exception in addChildServices::::::"+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		EtcdInitializer client = new EtcdInitializer("localhost");
//		ServiceXMLParser sxp = new ServiceXMLParser(client.getClient(),null);// for testing
//		sxp.serviceConfFile = new File("src/com/sjsu/etcd/appservices.xml");// for testing
//		sxp.parseServiceXML();// for testing
	}

}
