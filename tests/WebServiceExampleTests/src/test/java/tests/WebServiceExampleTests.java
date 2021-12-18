package tests;

import com.baeldung.jersey.server.model.Fruit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Assert;
//import org.softauto.serializer.CallFuture;
import org.softauto.core.CallFuture;
import org.softauto.jaxrs.CallOptions;
import org.softauto.tester.AbstractTesterImpl;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.infrastructure.Listener;
import tests.infrastructure.Step;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;


@Listeners(org.softauto.testng.JdryTestListener.class)
public class WebServiceExampleTests extends AbstractTesterImpl {

    private static final String USERNAME = "baeldung";
    private static final String PASSWORD = "super-secret";


    @Test
    public void call_a_rest_service2()throws Exception{
        CallOptions callOptions = CallOptions.newBuilder().setProduce(MediaType.APPLICATION_JSON_TYPE).build();
        String result =  new Step.com_baeldung_jersey_server_Greetings_getHelloGreeting(callOptions).get_Result();

        Assert.assertTrue(result.equals("hello"));
    }

    @Test
    public void readPathParam() throws Exception{
        CallOptions callOptions = CallOptions.newBuilder().setProduce(MediaType.APPLICATION_JSON_TYPE).build();
        String result = new Step.com_baeldung_jersey_server_Items_readPathParam("blabla",callOptions).get_Result();

        Assert.assertTrue(result.equals("Path parameter value is [blabla]"));
    }

    @Test
    public void call_a_rest_service1() throws Exception{
        CallOptions callOptions = CallOptions.newBuilder().setProduce(MediaType.APPLICATION_JSON_TYPE).build();
        Response res = new Step.com_baeldung_jersey_server_EchoHeaders_getHeadersBackFromDigestAuthentication(callOptions).get_Result();
        Assert.assertTrue(Response.Status.UNAUTHORIZED.getStatusCode()==res.getStatus());
    }


    @Test
    public void call_a_rest_service()throws Exception{
        CallOptions callOptions = CallOptions.newBuilder().build();
        com.baeldung.jersey.server.model.Fruit fruit   = new ObjectMapper().readValue("{\"name\":\"strawberry\",\"weight\":20}",com.baeldung.jersey.server.model.Fruit.class);
        Response response =  new Step.com_baeldung_jersey_server_rest_FruitResource_createNewFruit(fruit,callOptions).get_Result();
        assertEquals("Http Response should be 201 ", Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void listen_for_method_after()throws Exception {
        CallFuture<Fruit> future = new CallFuture<>();
        Fruit fruit = new Fruit();
        fruit.setName("strawberry");
        fruit.setWeight(20);
        CallOptions callOptions = CallOptions.newBuilder().build();
        new Step.com_baeldung_jersey_server_rest_FruitResource_createNewFruit(fruit,callOptions).get_Result();
        new Step.com_baeldung_jersey_server_rest_FruitResource_findFruitByName("strawberry",callOptions).then(
                Listener.com_baeldung_jersey_service_SimpleStorageService_findByName.waitToResult(future));
        Assert.assertTrue(future.getResult().getName().equals("strawberry"));

    }



    @Test
    public void basic_Authentication()throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(USERNAME, PASSWORD);
        CallOptions callOptions = CallOptions.newBuilder().authentication(feature).build();
        javax.ws.rs.core.Response  response = new Step.com_baeldung_jersey_server_EchoHeaders_getHeadersBack(callOptions).get_Result();

        Assert.assertTrue(response.getStatus() == 204);
    }

    @Test
    public void basic_Authentication_Listen_Before()throws Exception{
        CallFuture<Object[]> future = new CallFuture<>();
        com.baeldung.jersey.server.model.Fruit fruit   = new ObjectMapper().readValue("{\"name\":\"strawberry\",\"weight\":20}",com.baeldung.jersey.server.model.Fruit.class);
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(USERNAME, PASSWORD);
        CallOptions callOptions = CallOptions.newBuilder().authentication(feature).build();
        Response  response = new Step.com_baeldung_jersey_server_rest_FruitResource_createFruit(fruit,callOptions)
                .then(Listener.com_baeldung_jersey_service_SimpleStorageService_storeFruit.waitTo(future))
                .get_Result();
        Assert.assertTrue(response.getStatus() == 204);
        Assert.assertTrue(((Fruit)future.get()[0]).getName().equals("strawberry"));
    }

    @Test
    public void new_listener_for_method_after()throws Exception {
        CallFuture<Fruit> future = new CallFuture<>();
        Fruit fruit = new Fruit();
        fruit.setName("strawberry");
        fruit.setWeight(20);
        CallOptions callOptions = CallOptions.newBuilder().build();
        Listener listener =  Listener.addListener("com_baeldung_jersey_service_SimpleStorageService_findByName",new Class[]{String.class});
        new Step.com_baeldung_jersey_server_rest_FruitResource_createNewFruit(fruit,callOptions).get_Result();
        new Step.com_baeldung_jersey_server_rest_FruitResource_findFruitByName("strawberry",callOptions).then(
                listener.waitToResult("com_baeldung_jersey_service_SimpleStorageService_findByName", future));
        Assert.assertTrue(future.getResult().getName().equals("strawberry"));

    }

    public Function ff()throws Exception{
        return new Function() {
            @Override
            public Object apply(Object o) {
                return o;
            }
        };
    }

    @Test
    public void new_listener_for_method_after_with_function()throws Exception {
        CallFuture<Fruit> future = new CallFuture<>();
        Fruit fruit = new Fruit();
        fruit.setName("strawberry");
        fruit.setWeight(20);
        CallOptions callOptions = CallOptions.newBuilder().build();
        Listener listener =  Listener.addListener("com_baeldung_jersey_service_SimpleStorageService_findByName",new Class[]{String.class});
        new Step.com_baeldung_jersey_server_rest_FruitResource_createNewFruit(fruit,callOptions).get_Result();
        new Step.com_baeldung_jersey_server_rest_FruitResource_findFruitByName("strawberry",callOptions).then(
                listener.waitToResult("com_baeldung_jersey_service_SimpleStorageService_findByName", ff(),future));
        Assert.assertTrue(future.getResult().getName().equals("strawberry"));

    }

    @Test
    public void call_a_rest_service_with_new_step()throws Exception{
        CallOptions callOptions = CallOptions.newBuilder().setProduce(MediaType.APPLICATION_JSON_TYPE).build();
        String result =   new org.softauto.tester.Step("com_baeldung_jersey_server_Greetings_getHelloGreeting",new Object[]{},new Class[]{},"JAXRS",callOptions).get_Result();

        Assert.assertTrue(result.equals("hello"));
    }



}
