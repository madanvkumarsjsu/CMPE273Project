CMPE273Project
==============

Project 273

1. Keep the etcd running
2. Specify the port number in Dserver.port on which the application should run. Command to run the application from the terminal,
java -jar -Dserver.port=8081 build/libs/spring-boot-sample-simple-0.0.0.jar


key format for services - applications/{application_name}/{service_identifier}/{service_name}
eg. for service replica1, key is applications/sampleapplication/database/replica1
    for service leader of database, key is applications/sampleapplication/database/leader



Sample xml file for clients registering to the service

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
