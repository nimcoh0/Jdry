
 

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

Jdry build of two parts  SUT - the developing application &  Tester - the test project

## SUT

 set the requirement dependency in the SUT pom file and the pom profile - see [SUT setup ](https://github.com/nimcoh0/Jdry/wiki/SUT-setup) 
 annotate any method in the SUT code that you want to invoke as @ExportForTesting
and set the correct protocol you want to use @RPC for grpc ,@JXRS for jax-rs and @SOCKET for socket
 annotate any method in the SUT code that you want to Listen as @ListenerForTesting and @RPC 

> step 2 & 3 should be done by the developers 

 run mvn clean install -P schema 
> that will create the schema files . TestService.avpr for ExportForTesting & ListenerService.avpr for ListenerForTesting under  target\generated-sources\annotations\tests\infrastructure

 add  the Java agent (loader)  & the waver to the SUT project vm options 

    -javaagent:<path>/aspectjweaver-1.9.6.jar
    -javaagent:<path>/jdry-loader-1.0.0-jar.jar



 create Aop.xml file at  src\main\resources\META-INF
 

    <aspectj>  
     <aspects> 
	     <aspect name="org.softauto.grpc.listener.Client"/>  
		 <weaver options="-verbose">  
		     <include within="<your domain>..*"/>  
         </weaver>
     </aspects>
    </aspectj>

## Tester
for tester setup see [Tester setup](https://github.com/nimcoh0/Jdry/wiki/Tester-setup)
copy the * avpr files to src\test\resources\schema in the Tester project
run mvn clean install
  

> a list of classes are generated at target\generated-sources\tests\infrastructure
> that include the schema interfaces ,the schema impl and the AbstractTester

set your tests suite class to extend AbstractTester 

> The current impl testng.vm build the tester to be compitable with TestNg.
    you can replace it with a custom vm that suited your test framework 
    
create configuration file `<tester project root>/Configuration.yaml`
for configuration detail see [configuration](https://github.com/nimcoh0/Jdry/wiki/configuration.yaml)	
 

## command line setup
create a new folder lib at the Tester project \lib
copy the Tester jar , any more protocol jar you need , the test framework jar (for example testng.jar)
and your SUT jar to the lib folder 

    <your tester project path>\target\test-classes> java  -cp "..\..\lib\*;" org.testng.TestNG <path to the testng xml >UserTests.xml  


# How To Use It

Example test :
	this test uses Async call ,Sync call and listener for change the method arg values at runtime .
	no other classes needed .all the real impl is at the SUT code 
	
    public class packageProcessorEventTests extends AbstractTester {
    
    @Test  
    public void Over_Speed_New_Event(){  
        try {  
            logger.info("start test Over_Speed_New_Event");  
            CountDownLatch lock = new CountDownLatch(1);  //we need this lock for the Async steps
		    final CallFuture<String> future = new CallFuture<>();  //calback for the Async call
		    AtomicReference<Object[]> ref = new AtomicReference();  
	        asyncTests.helper_Sock_send(data,1,future);  //call Async method 
             //set new listener
		     new ListenerServiceImpl(){  
                @Override  
			    public java.lang.Object[] org_pack_processor_process_events_EventHeadler_HandlerOverSpeed(org.pack.processor.objects.UserEvent userEvent, org.pack.processor.objects.UserInfo userInfo, org.pack.processor.objects.ObjectInfo objectInfo, org.pack.processor.objects.DeviceWebLocationData location){  
                    userEvent.setCheckedValue(String.valueOf(location.getSpeed()-1));  //change arg value 
			        ref.set(new Object[]{userEvent.getEventId(),location.getImei()});  
			        lock.countDown();  //remove the lockdown
				    return new Object[]{userEvent,userInfo,objectInfo,location}; //return the update arg to the real method  
		      }  
            };  
		      lock.await(10, TimeUnit.MINUTES);  
    	      future.get();  //execute the Async 
    	      //execute Sync method for verify 
		      EventStatus eventStatus = tests.org_pack_processor_db_DBService_loadEventStatus(Integer.valueOf(ref.get()[0].toString()),String.valueOf(ref.get()[1]));  
		      Assert.assertTrue(eventStatus.getEventStatus() > 0);  
		      logger.info("test Over_Speed_New_Event finish successfully");  
      
	      }catch (Exception e){  
            logger.error("test Over_Speed_New_Event fail ",e);  
          }  
	    }
	   }
 

in the above test we use asyncTests proxy to invoke helper_Sock_send method (when package = helper , class = Sock and method = send ) in async way . because the method send flow will run the org_pack_processor_process_events_EventHeadler_HandlerOverSpeed method befor he finish , we need to run it Async so the listener can be register before the send execute . at this example we wait to code to rich the HandlerOverSpeed method and then replace the value of userEvent CheckedValue filed . then return the args to the SUT to perform the real call with the update args . after the Async finsih we call a sync method using the test proxy
to call org_pack_processor_db_DBService_loadEventStatus method for verify the result .
