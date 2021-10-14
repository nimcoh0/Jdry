
## Features
* produce the glue classes from the protocol file
* automatic create the Given, When, Then methods 
* automatic create a relevant ParameterType
* extend Parameter to support Java Expression
* extend Parameter to support Json to pojo 
* extend cucumber to support Async steps 

> for how to create the protocol files see  [Jdry](https://stackedit.io/app#)

> parameter that set as Json will be convert to Pojo according to the original parameter type  

## Java Expression
meaning you can write java expression as parameter . for example
**example 1:**

    When call OverSpeed create event set  userEventValue.checkedValue with location4Value.getSpeed()-1

userEventValue & location4Value are java Objects that you can refer to their fields 

**example 2:**

    Then validate step result of EventStatus.getEventStatus() == location4Value.getSpeed()-1

## Async steps
for every SUT method that is annotated with @ExposedForTesting two methods are created . sync and Async
the Async has #async at the end .it mean all steps until the next sync step will run in async mode 
for example 

    #async test  
    @Over_Speed_New_Event_async  
    Scenario: Over_Speed_New_Event  
      #send new location  
      Given a new Package {"imei":"860906040542521","op":"loc","lat":15.696758,"lng":-88.582168,"altitude":14,"angle":198,"speed":23,"dt_tracker":"2021-05-07 14:45:38","protocol":"ruptela","net_protocol":"tcp","ip":"127.0.0.1","event":"","loc_valid":1,"port":49543,"params":{"gpslev":"14","hdop":"0.6","event":"9","io4":"1","gsmlev":"21","io176":"23","io88":"0","io2":"0","io3":"0","io28":"1","io32":"35","io173":"0","io418":"1","io415":"0","io405":"1","io406":"1","io29":"13112","io30":"4098","io22":"79","io23":"75","io65":"123972496","io150":"70402","io34":"0","driver_id":"1"},"dt_server":"2021-05-11 17:26:29"} #async  
      #load event status  
      When call OverSpeed create event set  userEventValue.checkedValue with location4Value.getSpeed()-1  
      When Event Status result with 38 and "860906040542521"  
      #check the status   
      Then validate step result of EventStatus.getEventStatus() == location4Value.getSpeed()-1

in the example above the "*Given a new Package*" will run in async mode (meaning it will return before execute)  and triggers the *"When call OverSpeed create event"* to wait for *"OverSpeed create event"* method in the SUT to reach to this point in the flow .
then the value of *userEventValue.checkedValue* will be update to *location4Value.getSpeed()-1*
and influence the result of the first step *"Given a new Package"* flow
the step *"When Event Status result"* is  sync call so it will finish the async part and run normally .

## Test project setup pom

add the plugin to your pom

    <dependency>  
     <groupId>org.softauto</groupId>  
     <artifactId>jdry-cucumber-plugin</artifactId>  
     <version>beta-1.0</version>  
    </dependency>

set the jdry-velocity-maven-plugin

    <plugin>  
    	 <groupId>org.softauto</groupId>  
    	 <artifactId>jdry-velocity-maven-plugin</artifactId>  
    	 <version>beta-1.0</version>  
    	 <executions>
    		 <execution>  
    			 <id>cucumber tester</id>  
    			 <phase>generate-sources</phase>  
    			 <goals> 
    				 <goal>generator</goal>  
    			 </goals> 
    			 <configuration> 
    				 <outputDirectory>${basedir}/target/generated-sources</outputDirectory>  
    				 <template>cucumber.vm</template>  
    				 <namespace>tests.infrastructure</namespace>  
    				 <outputName>CucumberAbstractTesterImpl</outputName>  
    				 <classpath>/${user.home}/.m2/repository/org/softauto/jdry-cucumber-plugin/beta-1.0/jdry-cucumber-plugin-beta-1.0.jar</classpath>  
    		 </configuration>
    	 </execution>
     </executions>  
    </plugin>

set the jdry-maven-plugin

        <plugin>  
         <groupId>org.softauto</groupId>  
         <artifactId>jdry-maven-plugin</artifactId>  
         <version>beta-1.0</version>  
         <executions>
    	     <execution>  
    		     <id>cucumber</id>  
    		     <phase>generate-sources</phase>  
    		     <goals> 
    			     <goal>protocol</goal>  
    		     </goals> 
    		     <configuration>  
    			     <sourceDirectory>${project.basedir}/src/test/resources/schema</sourceDirectory>  
    			     <outputDirectory>${basedir}/target/generated-sources</outputDirectory>  
    		         <template>StepsCucumberImpl.vm</template>  
    			     <includes>StepService.avpr</includes>  
    			     <outputName>StepServiceCucumberImpl</outputName>  
    			     <classpath>/${user.home}/.m2/repository/org/softauto/jdry-cucumber-plugin/beta-1.0/jdry-cucumber-plugin-beta-1.0.jar</classpath>  
    			     <velocityToolsClassesNames>org.softauto.cucumber.CucumberUtils</velocityToolsClassesNames>  
                </configuration>  
    	    </execution>  
        <execution>  
    	     <id>local cucumber</id>  
    	     <phase>generate-sources</phase>  
    	     <goals> 
    		     <goal>protocol</goal>  
    	     </goals> 
    		     <configuration>
    			     <sourceDirectory>${project.basedir}/src/test/resources/schema</sourceDirectory>  
    			     <outputDirectory>${basedir}/target/generated-sources</outputDirectory>  
    		         <template>LocalStepsCucumberImpl.vm</template>  
    			     <includes>localStepService.avpr</includes>  
    			     <outputName>LocalStepServiceCucumberImpl</outputName>  
    			     <classpath>/${user.home}/.m2/repository/org/softauto/jdry-cucumber-plugin/beta-1.0/jdry-cucumber-plugin-beta-1.0.jar</classpath>  
    			     <velocityToolsClassesNames>org.softauto.cucumber.CucumberUtils</velocityToolsClassesNames>  
               </configuration>  
    	    </execution>  
    	    <execution>  
    		     <id>cucumber listener</id>  
    		     <phase>generate-sources</phase>  
    		     <goals> 	
    			     <goal>protocol</goal>  
    		     </goals> 
    		     <configuration>
    			     <sourceDirectory>${project.basedir}/src/test/resources/schema</sourceDirectory>  
    			     <outputDirectory>${basedir}/target/generated-sources</outputDirectory>  
    		         <template>ListenerCucumberImpl.vm</template>  
    			     <includes>ListenerService.avpr</includes>  
    			     <outputName>ListenerServiceCucumberImpl</outputName>  
    			     <classpath>/${user.home}/.m2/repository/org/softauto/jdry-cucumber-plugin/beta-1.0/jdry-cucumber-plugin-beta-1.0.jar</classpath>  
    			     <velocityToolsClassesNames>org.softauto.cucumber.CucumberUtils</velocityToolsClassesNames>  
                </configuration>  
    	    </execution>
         </executions>  
    </plugin>


run mvn clean install , it will produce 4 classes 
CucumberAbstractTesterImpl
ListenerServiceCucumberImpl
LocalStepServiceCucumberImpl
StepServiceCucumberImpl

## Then step

the "Then" step is a generic method that except exp , operator , exp 

    @Then("validate step result of {exp} {operator} {expected}")  
    public void validate(String exp ,String operator,Object expected)throws Exception{  
      boolean result = getResult(exp + " "+ operator + " "+ expected);  
      Assert.assertTrue(result);  
    }


> see [examples](https://github.com/nimcoh0/Jdry/tree/master/examples)  for a complete example
