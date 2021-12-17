package tests;

import com.baeldung.jersey.server.model.Fruit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Assert;
import org.softauto.core.CallFuture;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.infrastructure.AbstractTesterImpl;
import tests.infrastructure.Listener;
import tests.infrastructure.Step;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;


@Listeners(org.softauto.testng.JdryTestListener.class)
public class WebServiceExampleTests extends AbstractTesterImpl {

    private static final String USERNAME = "baeldung";
    private static final String PASSWORD = "super-secret";


    @Test
    public void call_a_rest_service2()throws Exception{
        String result =  new Step.com_baeldung_jersey_server_Greetings_getHelloGreeting().setProduce(MediaType.APPLICATION_JSON_TYPE)
                                                                                         .execute()
                                                                                         .get_Result();
        Assert.assertTrue(result.equals("hello"));
    }

    @Test
    public void readPathParam() throws Exception{
        String result = new Step.com_baeldung_jersey_server_Items_readPathParam().setProduce(MediaType.APPLICATION_JSON_TYPE)
                                                                 .execute("blabla")
                                                                 .get_Result();
        Assert.assertTrue(result.equals("Path parameter value is [blabla]"));
    }

    @Test
    public void call_a_rest_service1() throws Exception{
        Response res = new Step.com_baeldung_jersey_server_EchoHeaders_getHeadersBackFromDigestAuthentication().setProduce(MediaType.APPLICATION_JSON_TYPE)
                                                                                                .execute()
                                                                                                .get_Result();
        Assert.assertTrue(Response.Status.UNAUTHORIZED.getStatusCode()==res.getStatus());
    }


    @Test
    public void call_a_rest_service()throws Exception{
        com.baeldung.jersey.server.model.Fruit fruit   = new ObjectMapper().readValue("{\"name\":\"strawberry\",\"weight\":20}",com.baeldung.jersey.server.model.Fruit.class);
        Response response =  new Step.com_baeldung_jersey_server_rest_FruitResource_createNewFruit().execute(fruit).get_Result();
        assertEquals("Http Response should be 201 ", Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void listen_for_method_after()throws Exception {
        CallFuture<Fruit> future = new CallFuture<>();
        Fruit fruit = new Fruit();
        fruit.setName("strawberry");
        fruit.setWeight(20);
        new Step.com_baeldung_jersey_server_rest_FruitResource_createFruit().execute(fruit).get_Result();
        new Step.com_baeldung_jersey_server_rest_FruitResource_findFruitByName().execute("strawberry").then(
                Listener.com_baeldung_jersey_service_SimpleStorageService_findByName.waitToResult(future));
        Assert.assertTrue(future.getResult().getName().equals("strawberry"));

    }



    @Test
    public void basic_Authentication()throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(USERNAME, PASSWORD);
        javax.ws.rs.core.Response  response = new Step.com_baeldung_jersey_server_EchoHeaders_getHeadersBack()
                .authentication(feature)
                .execute()
                .get_Result();
        Assert.assertTrue(response.getStatus() == 204);
    }

    @Test
    public void basic_Authentication_Listen_Before()throws Exception{
        CallFuture<Object[]> future = new CallFuture<>();
        com.baeldung.jersey.server.model.Fruit fruit   = new ObjectMapper().readValue("{\"name\":\"strawberry\",\"weight\":20}",com.baeldung.jersey.server.model.Fruit.class);
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(USERNAME, PASSWORD);
        Response  response = new Step.com_baeldung_jersey_server_rest_FruitResource_createFruit()
                .authentication(feature)
                .execute(fruit)
                .then(Listener.com_baeldung_jersey_service_SimpleStorageService_storeFruit.waitTo(future))
                .get_Result();
        Assert.assertTrue(response.getStatus() == 204);
        Assert.assertTrue(((Fruit)future.get()[0]).getName().equals("strawberry"));
    }



}
