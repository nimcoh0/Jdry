

 

# Jdry - Java Don't repeat yourself 


Jdry framework is a test automation tool that will save you time writing and Maintenance tests automation code by simplify  the code  and reduce duplication between the test framework and the SUT ,It aim to integrate with existing Test automation framework’s  like TestNg ,Cucumber .. and more . 

depended on your need and  the SUT code it can reduce by significantly the time it take to write tests and  reduce significantly the amount of code you need  to support this tests . It can be use for e2e, functional ,component ,regression... testing .  
 

# Features

 1. Dynamic Api – invoking any method on the SUT 
    
   
 2. Listen for method result – listen to any method in the SUT
   
    
 3. Change parameters value at runtime on any method on the SUT
    
    
 4. Plugin – can support any protocol by plugin mechanism
    
    
 5. Serialize any java type
    
    
 6. Class Injector – can connect to pre existing Objects
    
    
 7. Integrate with existing framework by plugin mechanism
    
    
 8. Very detail trace log
 9. support Async & sync tests

 

# How IT Works
Jdry works as client server architecture . it uses the SUT code impl as the building blocks for the testing . a java agent is loaded in the SUT that get the request and execute it on the SUT and actually publish all the annotated methods as Dynamic Api  .  Methods annotated as Listener  are capture and send to the Listener Server running on the Tester .
to be examine or update and can serve as verify point or data set . method annotated as ExportForTesting are serve as proxy at the Tester so no impl is needed . any java type can be Serialize so the proxy  methods are serve as is without the need of Protobuf like solutions  

# Getting Started
download the project and run "mvn clean install"
Jdry build of two parts  SUT - the developing application &  Tester - the test project

> you also need jdry-serializer , jdry-maven-plugin and
> jdry-velocity-maven-plugin

## SUT

 set the requirement dependency in the SUT pom file and the pom profile - see [SUT setup ](https://github.com/nimcoh0/Jdry/wiki/SUT-pom-setup) 
 annotate any method in the SUT code that you want to invoke as @ExportForTesting
and set the correct protocol you want to use @RPC for grpc ,@JXRS for jax-rs and @SOCKET for socket
 annotate any method in the SUT code that you want to Listen as @ListenerForTesting and @RPC 

> step on the SUT should be done by the developers 

 run mvn clean install -P schema 
> that will create the schema files . TestService.avpr for ExportForTesting & ListenerService.avpr for ListenerForTesting under  target\generated-sources\annotations\tests\infrastructure

 add  the Java agent (loader)  to the SUT project vm options . download from templates or download the project and compile it 
 
    -javaagent:<path>/jdry-agent-beta-1.0.jar



 create Aop.xml file at  src\main\resources\META-INF
 

    <aspectj>  
     <aspects> 
	      <weaver options="-verbose">  
		     <include within="<your domain>..*"/>  
         </weaver>
     </aspects>
    </aspectj>

add 

	<dependency>
            <groupId>org.softauto</groupId>
            <artifactId>jdry-annotations</artifactId>
            <version>beta-1.0</version>
        </dependency>
	
## Tester
the simple why is [quick start ](https://github.com/nimcoh0/Jdry/wiki/Quick-Start)
or for more details read the rest of the file .

the setting depend on the kind of Test framework you are using . in this example we use testNG
for tester setup see [Tester setup](https://github.com/nimcoh0/Jdry/wiki/Tester-pom-setup)
copy the * avpr files to src\test\resources\schema in the Tester project
run mvn clean install
  

> a list of classes are generated at target\generated-sources\tests\infrastructure
> that include the schema interfaces ,the schema impl and the AbstractTesterImpl

set your tests suite class to extend AbstractTesterImpl 

> The current impl testng.vm build the tester to be compitable with TestNg.
    you can replace it with a custom vm that suited your test framework 
    
create configuration file `<tester project root>/Configuration.yaml`
for configuration detail see [configuration](https://github.com/nimcoh0/Jdry/wiki/configuration.yaml)	
 




## command line setup
create a new folder lib at the Tester project \lib
copy the Tester jar , any more protocol jar you need , the test framework jar (for example testng.jar)
and your SUT jar to the lib folder 

    <your tester project path>\target\test-classes> java  -cp "..\..\lib\*;" org.testng.TestNG <path to the testng xml >UserTests.xml  

# Helper classes

## How To Use It

same as in SUT you can annotaed your local method in the helper classes with ExportForTesting . 

> ListenerForTesting is not supported in the local helper classes . (no need)

set the tester pom file according to tester setup and run
mvn clean install -P schema ,
it will produce   TestService.avpr for ExportForTesting . for marge the local tests service with the SUT tests service 
just put the SUT tests service avpr file  in the tests.infrastructure and run the  `mvn clean install -P schema`

for tests examples see [tests](https://github.com/nimcoh0/Jdry/blob/master/tests/appTests/src/test/java/tests/ExampleTests.java) 


for list of current limitations see https://github.com/nimcoh0/Jdry/wiki/limitations

# Contact
[Project Home](https://softauto.org)
