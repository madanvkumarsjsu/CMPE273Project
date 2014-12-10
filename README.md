CMPE273Project
==============

Using sample application to test the project execution
========================================================
1. Keep the etcd running in local
2. Download spring-boot-sample-application
3. Ensure there is etcddition.jar and etcdditionlibrary.jar in lib folder and build path
4. Build the application
5. Execute command to run the application from the terminal,
java -jar -Dserver.port=8081 build/libs/spring-boot-sample-simple-0.0.0.jar


key format for etcd 
http://localhost:4001/v2/keys/applications/{application_name}/{service_identifier}/{replica_count}

Value format in etcd
{"Appname": "xxxxxxxxxxxxx", "hostname": "xxxxxxxxxxxx", "port":"xxxxxxxxxxxxxxxx"}

Leaders are stored in 
http://localhost:4001/v2/keys/applications/{application_name}/{service_identifier}/leader

eg. for database service replica1, key is applications/sampleapplication/database/replica1
    for service leader of database, key is applications/sampleapplication/database/leader

To use the project in your application
=======================================

1. Define a xml file similar to below one (mandatory)

Sample xml file for clients registering to the service
-------------------------------------------------------
    <?xml version="1.0" encoding="UTF-8"?>
    <application name="sampleapplication" host="applicationhost" port="applicationport">
    <services>
    <service name="service_name1">
    <replica host="hostname" port="port_num"></replica>
    <replica host="hostname" port="port_num"></replica>
    <replica host="hostname" port="port_num"></replica>
    </service>
    <service name="service_name2">
    <replica host="hostname" port="port_num"></replica>
    <replica host="hostname" port="port_num"></replica>
    <replica host="hostname" port="port_num"></replica>
    </service>
    </services>
    </application>


2. Include etcddition.jar and etcdditionlibrary.jar in you application build path
---------------------------------------------------------------------------
	EtcdInitializer client = new EtcdInitializer("localhost");//specify the etcd host running in a cluster
	ServiceXMLParser sxp = new ServiceXMLParser(client.getClient(),res.getFile());// Create a reference for file and send it to the ServiceXMLParser class
	sxp.parseServiceXML();//Call ParseServiceXML to initiate the service registration and election
---------------------------------------------------------------------------
Use the below method to get the leader of the service among the replicas
---------------------------------------------------------------------------
	client.getService(client.getClient(), sxp.getStrApplicationName(), "database");

