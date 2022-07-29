package org.softauto.scanner.tools.testc.tree;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.apache.avro.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.*;
import org.softauto.annotations.BeforeTestType;
import org.softauto.scanner.source.tree.*;
import org.softauto.scanner.util.Context;
import org.softauto.scanner.util.MessageBuilder;
import org.softauto.scanner.util.Utils;
import org.softauto.scanner.source.tree.Tree;
import org.softauto.scanner.source.tree.TreeVisitor;


import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.*;


public abstract class TCTree implements Tree {


    ObjectMapper objectMapper;

    public TCTree(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
    }






    public static class TCStep  extends TCTree implements StepTree {

        List<Suite.Step> steps = new ArrayList<>();
        Schema response;
        Schema request;
        Context ctx;
        String template;



        @Override
        public List<Suite.Step> getSteps() {
            return null;
        }

        @Override
        public Kind getKind() {
            return Kind.STEP;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }





        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            for (Map.Entry node : protocol.getMessages().entrySet()) {
                String key = node.getKey().toString();
                Protocol.Message message = (Protocol.Message) node.getValue();
                //if (message.getObjectProp("annotations").contains("ExposedForTesting")) {
                if(Utils.isAnnotationExist("org.softauto.annotations.ExposedForTesting", (HashMap<String, Object>) message.getObjectProp("annotations"))) {
                    //this.message = message;
                    String methodName = message.getProp("method");
                    String namespace = message.getProp("namespace");
                    String name = namespace + "." + methodName;
                    String doc = message.getDoc();
                    Map<String, Object> propMap =  message.getObjectProps();
                    //propMap.put("key",key);
                    //propMap.put("parent",message.getProp("parent"));
                    request = message.getRequest();
                    response = message.getResponse();
                    Suite.Step step = new Suite().createStep(name, doc, propMap, request,response);
                    steps.add(step);
                    //Item parent = ctx.getItemByKey(message.getProp("parent"));
                    //parent.addItem(step);
                    //orderId = Integer.valueOf(message.getObjectProps().get("orderId").toString());
                    //ctx.addItem(orderId,step);
                    //ctx.addKeyToItem(key,step);
                }
            }
            return visitor.visitStep(this, data, (R)this);
        }


        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            map.put("transceiver",props.get("transceiver"));
            map.put("annotations",Utils.getAnnotationOther((ArrayList) props.get("annotations")));
            map.put("description",props.get("description"));
            map.put("orderId",props.get("orderId"));
            map.put("dependencies",Utils.getAnnotationProperty("dependencies",(ArrayList) props.get("annotations")));
            map.put("after",Utils.getAnnotationProperty("after",(ArrayList) props.get("annotations")));
            map.put("before",Utils.getAnnotationProperty("before",(ArrayList) props.get("annotations")));
            return map;
        }







    }






    public static class TCApi  extends TCTree implements ApiTree {


        Suite.Api api;
        int orderId;
        Schema response;
        Schema request;
        ContextTree contextTree;
        Protocol.Message message;

        Context ctx;

        public TCApi(){

        }

        public TCApi(Context ctx){
            this.ctx = ctx;
        }




        @Override
        public Kind getKind() {
            return Kind.API;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            for (Map.Entry node : protocol.getMessages().entrySet()) {
                String key = node.getKey().toString();
                Protocol.Message message = (Protocol.Message) node.getValue();
                //if (message.getObjectProp("annotations").contains("ExposedForTesting")) {
                if(Utils.isAnnotationExist("org.softauto.annotations.ApiForTesting", (HashMap<String, Object>) message.getObjectProp("annotations")) ) {
                    this.message = message;
                    String methodName = message.getProp("method");
                    String namespace = message.getProp("namespace");
                    String name = namespace + "." + methodName;
                    String doc = message.getDoc();
                    Map<String, Object> propMap =  message.getObjectProps();
                    //propMap.put("key",key);
                    request = message.getRequest();
                    response = message.getResponse();
                    //orderId = Integer.valueOf(message.getObjectProps().get("orderId").toString());
                    api = new Suite().createApi(name, doc, propMap, request,response);
                    //ctx.addItem(orderId,api);
                    //ctx.addKeyToItem(key,api);
                }
            }
            return visitor.visitApi(this, data, (R)this);
        }

        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            map.put("transceiver",props.get("transceiver"));
            map.put("annotations",Utils.getAnnotationOther((ArrayList) props.get("annotations")));
            map.put("description",props.get("description"));
            //map.put("orderId",props.get("orderId"));
            map.put("dependencies",Utils.getAnnotationProperty("dependencies",(ArrayList) props.get("annotations"),"ApiForTesting"));
            map.put("after",Utils.getAnnotationProperty("after",(ArrayList) props.get("annotations"),"ApiForTesting"));
            map.put("before",Utils.getAnnotationProperty("before",(ArrayList) props.get("annotations"),"ApiForTesting"));
            //map.put("beforeAssert",Utils.getAnnotationProperty("beforeAssert",(ArrayList) props.get("annotations")));
            return map;
        }



        @Override
        public Suite.Api getApi() {
            return api;
        }




        @Override
        public Protocol.Message getMessage() {
            return message;
        }




    }

    public static class TCVerify  extends TCTree implements VerifyTree {


        Suite.Verify verify;
        Protocol.Message message;
        Schema response;
        Schema request;
        Context ctx;

        public TCVerify(){

        }

        public TCVerify(Context ctx){
            this.ctx = ctx;
        }



        @Override
        public Kind getKind() {
            return Kind.VERIFY;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            try {
                Protocol protocol = (Protocol) data;
                for (Map.Entry node : protocol.getMessages().entrySet()) {
                    String key = node.getKey().toString();
                    Protocol.Message message = (Protocol.Message) node.getValue();
                    //if (message.getProp("annotations").contains("VerifyForTesting")) {
                    if(Utils.isAnnotationExist("org.softauto.annotations.VerifyForTesting", (HashMap<String, Object>) message.getObjectProp("annotations"))) {
                        this.message = message;
                        String methodName = message.getProp("method");
                        String namespace = message.getProp("namespace");
                        String name = namespace + "." + methodName;
                        String doc = message.getDoc();
                        Map<String, Object> propMap =  message.getObjectProps();
                        //propMap.put("key",key);
                        request = message.getRequest();
                        response = message.getResponse();
                        verify = new Suite().createVerify(name, doc, propMap, request,response);
                        //Item parent = ctx.getItemByKey(message.getProp("parent"));
                        //parent.addItem(verify);

                        //orderId = Integer.valueOf(message.getObjectProps().get("orderId").toString());
                        //ctx.addItem(orderId,verify);
                        //ctx.addKeyToItem(key,verify);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return visitor.visitVerify(this,data,(R)this);
        }



        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            map.put("transceiver",props.get("transceiver"));
            map.put("description",props.get("description"));
            //map.put("orderId",props.get("orderId"));
            map.put("annotations",Utils.getAnnotationOther((ArrayList) props.get("annotations")));
            map.put("assertType",Utils.getAnnotationProperty("assertType",(ArrayList) props.get("annotations"),"VerifyForTesting"));
            map.put("verifyType",Utils.getAnnotationProperty("verifyType",(ArrayList) props.get("annotations"),"VerifyForTesting"));
            map.put("after",Utils.getAnnotationProperty("after",(ArrayList) props.get("annotations"),"VerifyForTesting"));
            map.put("before",Utils.getAnnotationProperty("before",(ArrayList) props.get("annotations"),"VerifyForTesting"));
            //map.put("beforeAssert",getAnnotationProperty("beforeAssert",(ArrayList) props.get("annotations")));
            return map;
        }

        @Override
        public Suite.Verify getVerify() {
            return verify;
        }

    }


    public static class TCListener  extends TCTree implements ListenerTree {


        List<Suite.Listener> listeners = new ArrayList<>();
        Schema response;
        Schema request;
        Context ctx;

        public TCListener(){

        }

        public TCListener(Context ctx){
            this.ctx = ctx;
        }



        @Override
        public Kind getKind() {
            return Kind.LISTENER;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            try {
                //String json = new ObjectMapper().writeValueAsString(data);
                //Protocol protocol = Protocol.parse(json);
                Protocol protocol = (Protocol) data;
                for (Map.Entry node : protocol.getMessages().entrySet()) {
                    String key = node.getKey().toString();
                    Protocol.Message message = (Protocol.Message) node.getValue();
                    //if (message.getProp("annotations").contains("ListenerForTesting")) {
                    if(Utils.isAnnotationExist("org.softauto.annotations.ListenerForTesting", (HashMap<String, Object>) message.getObjectProp("annotations"))) {
                        String methodName = message.getProp("method");
                        String namespace = message.getProp("namespace");
                        String name = namespace + "." + methodName;
                        String doc = message.getDoc();
                        Map<String, Object> propMap =  message.getObjectProps();
                        //propMap.put("key",key);
                        //propMap.put("parent",message.getProp("parent"));
                        request = message.getRequest();
                        response = message.getResponse();
                        Suite.Listener listener = new Suite().createListener(name, doc, propMap, request,response);
                        listeners.add(listener);
                        //Item parent = ctx.getItemByKey(message.getProp("parent"));
                        //parent.addItem(listener);
                        //orderId = Integer.valueOf(message.getObjectProps().get("orderId").toString());
                        //ctx.addItem(orderId,listener);
                        //ctx.addKeyToItem(key,listener);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return visitor.visitListener(this,data,(R)this);
        }

        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            map.put("transceiver",props.get("transceiver"));
            map.put("description",props.get("description"));
            //map.put("orderId",props.get("orderId"));
            map.put("annotations",Utils.getAnnotationOther((ArrayList) props.get("annotations")));
            map.put("mode",Utils.getAnnotationProperty("mode",(ArrayList) props.get("annotations"),"ListenerForTesting"));
            map.put("mock_value",Utils.getAnnotationProperty("mock_value",(ArrayList) props.get("annotations"),"ListenerForTesting"));
            map.put("mock_type",Utils.getAnnotationProperty("mock_type",(ArrayList) props.get("annotations"),"ListenerForTesting"));
            map.put("after",Utils.getAnnotationProperty("after",(ArrayList) props.get("annotations"),"ListenerForTesting"));
            map.put("before",Utils.getAnnotationProperty("before",(ArrayList) props.get("annotations"),"ListenerForTesting"));
            return map;
        }






        @Override
        public List<Suite.Listener> getListeners() {
            return listeners;
        }
    }

    public static class TCTest  extends TCTree implements TestTree {
        Protocol protocol;

        Suite.Test test;

        int id ;

        BeforeTestTree beforeTestTree;

        //DependenceTree dependenceTree;

        ProtocolTree protocolTree;

        ArrayNode references;

        public TCTest(Protocol protocol){
            this.protocol = protocol;
        }

        public TCTest(){

        }

        DataTree dataTree;

        @Override
        public Kind getKind() {
            return Kind.TEST;
        }






        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            if(data != null) {
                return visitor.visitTest(this, data, (R)this);
            }
            return visitor.visitTest(this, (D)protocol, (R)this);

        }

        /*
        @Override
        public <R, D> R accept(TestVisitor<R, D> visitor, D data, R result,int id,ArrayNode references) {
            this.id = id;
            this.references = references;
            visitor.visitTest(this,data,result);
            return (R)getTest();
        }


         */


        @Override
        public Suite.Test getTest() {
            try {
                String name = protocolTree.getApi().getName();
                //String id = protocolTree.getApiTree().getId();
                String doc = protocolTree.getApi().getDoc();
                Map<String, Object> propMap =  buildProps(protocolTree.getApi().getObjectProps());
                //Schema request = protocolTree.getApi().getRequest();


                //test = new Suite().createTest(name+"_"+id, doc, (Map<String, ?>) propMap, protocolTree.getSteps(), protocolTree.getVerify(),protocolTree.getListeners(),  protocolTree.getApi());
                test = new Suite().createTest(name+"_"+protocol.getObjectProp("id"), doc, (Map<String, ?>) propMap,   protocolTree.getApi(),protocolTree.getAssert(),dataTree.getData());


            }catch (Exception e){
                e.printStackTrace();
            }
            return test;
        }

        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            //map.put("reference",referenceTree.getReferences());
            map.putAll(props);
            //map.put("dependence",dependenceTree.getDependence());
            map.put("beforeTest",protocolTree.getBeforeTestTree().getBeforeTest());
            map.put("references",protocol.props.get("references"));
            //map.put("templatesList",protocolTree.getContextTree().getItemAndtemplates());
            //map.put("dependencies",getAnnotationProperty("dependencies",(ArrayList) props.get("annotations")));
            return map;
        }

        @Override
        public ProtocolTree getProtocolTree() {
            if(protocolTree == null){
                protocolTree = new TCProtocol();
            }
            return protocolTree;
        }




        @Override
        public DataTree getDataTree() {
            if(dataTree == null){
                dataTree = new TCData(protocolTree);
            }
            return dataTree;
        }



    }

/*
    public static class TCTypes  extends TCTree implements TypesTree {

        Collection<Schema> types;

        @Override
        public Kind getKind() {
            return Kind.TYPES;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            types = protocol.getTypes();
            return visitor.visitTypes(this,data,result);
        }

        @Override
        public Collection<Schema> getTypes() {
            return types;
        }
    }
*/


    public static class TCProtocol  extends TCTree implements ProtocolTree {

        Protocol protocol;
        //Context ctx = new Context();
        VerifyTree verifyTree;
        StepTree stepTree ;
        ApiTree apiTree;
        ListenerTree listenerTree ;
        TypesTree typesTree;
        ContextTree contextTree;
        OtherTree otherTree;
        AssertTree assertTree;
        BeforeTestTree beforeTestTree;



        public Suite.Verify getVerify(){
            return verifyTree.getVerify();
        }

        public List<Suite.Step> getSteps(){
            return stepTree.getSteps();
        }

        public Suite.Api getApi(){
            return apiTree.getApi();
        }

        public List<Suite.Listener> getListeners(){
            return listenerTree.getListeners();
        }



        public Suite.Assert getAssert(){
            return assertTree.getAssert();
        }

        @Override
        public Kind getKind() {
            return Kind.PROTOCOL;
        }



        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            try {
                //String json = objectMapper.writeValueAsString(data);
               // protocol = Protocol.parse(json);
                protocol = (Protocol) data;
            }catch (Exception e){
                e.printStackTrace();
            }
            return visitor.visitProtocol(this,(D)protocol,(R)this);
        }



        @Override
        public Protocol getProtocol() {
            return protocol;
        }

        @Override
        public VerifyTree getVerifyTree() {
            if(verifyTree == null) {
                verifyTree = new TCVerify();
            }
            return verifyTree;
        }



        @Override
        public StepTree getStepTree() {
            if(stepTree == null) {
                return stepTree = new TCStep();
            }
            return stepTree;
        }

        @Override
        public ApiTree getApiTree() {
            if(apiTree == null) {
                return apiTree = new TCApi();
            }
            return apiTree;
        }

        @Override
        public ListenerTree getListenerTree() {
            if(listenerTree == null) {
                listenerTree = new TCListener();
            }
            return listenerTree;
        }

        @Override
        public TypesTree getTypesTree() {
            if(typesTree == null){
                typesTree = new TCTypes();
            }
            return typesTree;
        }



        @Override
        public OtherTree getOtherTree() {
            if(otherTree == null){
                otherTree = new TCOther();
            }
            return otherTree;
        }

        @Override
        public AssertTree getAssertTree() {
            if(assertTree == null){
                assertTree = new TCAssert();
            }
            return assertTree;
        }

        @Override
        public BeforeTestTree getBeforeTestTree() {
            if(beforeTestTree == null){
                beforeTestTree = new TCBeforeTest();
            }
            return beforeTestTree;
        }

        @Override
        public ProtocolTree getProtocolTree() {
            return this;
        }
    }






    public static class TCOther  extends TCTree implements OtherTree {

        Context ctx;




        private boolean isAnnotationExist(String name, ArrayList annotations){
            for(Object node :annotations){
                for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                    if(entry.getKey().equals(name) && entry.getValue() != JsonProperties.NULL_VALUE){
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            for (Map.Entry node : protocol.getMessages().entrySet()) {
                String key = node.getKey().toString();
                Protocol.Message message = (Protocol.Message) node.getValue();
                //if (message.getObjectProp("annotations").contains("ExposedForTesting")) {
                List<HashMap<String,Object>> annotations = (ArrayList) message.getObjectProp("annotations");
                if (!isAnnotationExist("ExposedForTesting", (ArrayList) annotations) &&
                        !isAnnotationExist("ListenerForTesting", (ArrayList) annotations) &&
                        !isAnnotationExist("VerifyForTesting", (ArrayList) annotations) &&
                        !isAnnotationExist("BeforeTest", (ArrayList) annotations) &&
                        !isAnnotationExist("AfterTest", (ArrayList) annotations) &&
                        !isAnnotationExist("ApiForTesting", (ArrayList) annotations)){
                    Item parent = ctx.getItemByKey(message.getProp("parent"));
                    ctx.addKeyToItem(key,parent);
                }
            }
            return visitor.visitOther(this,data,(R)this);
        }
    }

    public static class TCAssert  extends TCTree implements AssertTree {

        Context ctx;
        Suite.Assert _assert;



        public Suite.Assert getAssert(){
            return _assert;
        }


        @Override
        public Kind getKind() {
            return null;
        }



        private Map<String, Object> buildProps(Map<String, Object> props){
            Map<String, Object> map = new HashMap<>();
            map.put("description",props.get("description"));

            //map.put("assertType",getAnnotationProperty("assertType",(ArrayList) props.get("annotations")));
            return map;
        }



        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            for (Map.Entry node : protocol.getMessages().entrySet()) {
                String key = node.getKey().toString();
                Protocol.Message message = (Protocol.Message) node.getValue();
                //if (message.getProp("annotations").contains("VerifyForTesting")) {
                if (Utils.isAnnotationExist("org.softauto.annotations.VerifyForTesting", (HashMap<String, Object>) message.getObjectProp("annotations"))) {
                    String methodName = message.getProp("method");
                    String namespace = message.getProp("namespace");
                    String name = namespace + "." + methodName;
                    String doc = message.getDoc();
                    Map<String, Object> propMap =  buildProps(message.getObjectProps());
                    String assertType = Utils.getAnnotationProperty("assertType",(HashMap<String,Object>) message.getObjectProps().get("annotations"),"org.softauto.annotations.VerifyForTesting").toString();
                    //String beforAssert = getAnnotationProperty("beforeAssert",(ArrayList) message.getObjectProps().get("annotations"),"VerifyForTesting");
                    //String verifyType = getAnnotationProperty("verifyType",(ArrayList) message.getObjectProps().get("annotations"),"VerifyForTesting");
                    propMap.put("assertType",assertType);
                    propMap.put("assertName","future.get()");
                    _assert = new Suite().createAssert(name, doc, propMap);
                }else if (Utils.isAnnotationExist("org.softauto.annotations.ApiForTesting", (HashMap<String, Object>) message.getObjectProp("annotations"))){
                    String methodName = message.getProp("method");
                    String namespace = message.getProp("namespace");
                    String name = namespace + "." + methodName;
                    String doc = message.getDoc();
                    Map<String, Object> propMap =  buildProps(message.getObjectProps());
                    String assertType =Utils. getAnnotationProperty("assertType",(HashMap<String,Object>) message.getObjectProps().get("annotations"),"org.softauto.annotations.ApiForTesting").toString();
                    //String beforAssert = getAnnotationProperty("beforeAssert",(ArrayList) message.getObjectProps().get("annotations"),"Api");
                    //propMap.put("beforAssert",beforAssert);
                    propMap.put("assertType",assertType);
                    propMap.put("assertName","result_"+name.replace(".","_"));
                    _assert = new Suite().createAssert(name, doc, propMap);
                }
            }
            return visitor.visitAssert(this,data,(R)this);
        }
    }

    public static class TCData  extends TCTree implements DataTree {

        ProtocolTree protocolTree;

        Suite.Data _data;

        public Suite.Data getData(){
            return _data;
        }

        public TCData(){

        }

        public TCData(ProtocolTree protocolTree){
            this.protocolTree = protocolTree;
        }

        @Override
        public Kind getKind() {
            return null;
        }




        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Schema response = null;
            Schema request = protocolTree.getApi().getRequest();
            if(protocolTree.getVerify() != null){
                response = protocolTree.getVerify().getResponse();
            }else {
                response = protocolTree.getApi().getResponse();
            }
            _data = new Suite().createData(request,response);


            return visitor.visitData(this,data,(R)this);
        }
    }

    public static class TCBeforeTest  extends TCTree implements BeforeTestTree {

        AuthenticationTree authenticationTree;

        HashMap<String, HashMap<String, Object>> beforeTest =new HashMap<>();

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            return visitor.visitBeforeTest(this,data,(R)this);

        }

        @Override
        public AuthenticationTree getAuthenticationTree() {
            if(authenticationTree == null){
                authenticationTree = new TCAuthentication();
            }
            return authenticationTree;
        }

        @Override
        public HashMap<String, HashMap<String, Object>> getBeforeTest() {
            beforeTest.put("authentication",authenticationTree.getAuthentication());
            return beforeTest;
        }


    }

    public static class TCAfterTest  extends TCTree implements AfterTestTree {

        @Override
        public HashMap<String, Object> getAnnotations() {
            return null;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            return null;
        }
    }

    public static class TCAuthentication  extends TCTree implements AuthenticationTree {

        HashMap<String, Object> authentication = new HashMap<>();

        @Override
        public Kind getKind() {
            return null;
        }

        /*
        public static  Object getAnnotationProperty(String name, ArrayList annotations,String annotation){
            for(Object node :annotations){
                for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                    if(entry.getKey().equals(annotation) && entry.getValue() != JsonProperties.NULL_VALUE){
                        for(Map.Entry e :((HashMap<String,Object>)entry.getValue()).entrySet()){
                            for(Map.Entry a :((HashMap<String,Object>)e.getValue()).entrySet()) {
                                if (a.getKey().equals(name)) {
                                    return a.getValue();
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }

         */

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol = (Protocol) data;
            for (Map.Entry node : protocol.getMessages().entrySet()) {
                Protocol.Message message = (Protocol.Message) node.getValue();
                if (Utils.isAnnotationExist("org.softauto.annotations.BeforeTest", (HashMap<String,Object>) message.getObjectProp("annotations"))) {
                    if (Utils.getAnnotationProperty("type", (HashMap<String,Object>) message.getObjectProp("annotations"), "org.softauto.annotations.BeforeTest").equals(BeforeTestType.Authentication.name())) {
                        Schema request = message.getRequest();
                        for(Schema.Field field : request.getFields()){
                            if(field.schema().getFullName().equals("javax.ws.rs.container.ContainerRequestContext")){
                                JsonNode json = field.currentValue();
                                authentication.put("schema",json.get("Authentication") != null ? json.get("Authentication").asText(): JsonProperties.NULL_VALUE);
                                authentication.put("username",json.get("username") != null ? json.get("username").asText():JsonProperties.NULL_VALUE);
                                authentication.put("password",json.get("password") != null ? json.get("password").asText():JsonProperties.NULL_VALUE);
                                return visitor.visitAuthentication(this,data,(R)this);
                            }
                        }
                    }
                }
            }

            return visitor.visitAuthentication(this,data,(R)this);
        }

        @Override
        public HashMap<String, Object> getAuthentication() {
            return authentication;
        }
    }


    public static class TCVerifyAnnotation extends TCTree implements VerifyAnnotationTree {

        HashMap<String,Object> annotations;



        @Override
        public HashMap<String,Object> getAnnotations(){
            return annotations;
        }


        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class) != null){
                annotations = new HashMap<>();
                annotations.put("description",((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class).description());
                annotations.put("assertType",((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class).assertType().getValue());
                annotations.put("verifyType",((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class).type().getValue());
                annotations.put("before",((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class).before());
                annotations.put("after",((Method) r).getAnnotation(org.softauto.annotations.VerifyForTesting.class).after());
            }
            return visitor.visitVerifyAnnotation(this,data,r);
        }
    }

    public static class TCListenerAnnotation extends TCTree implements ListenerAnnotationTree {

        HashMap<String,Object> annotations;



        @Override
        public HashMap<String,Object> getAnnotations(){
            return annotations;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class) != null){
                annotations = new HashMap<>();
                //annotations.put("value", Utils.result2String(((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).value()));
                annotations.put("mode",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).mode().name());
                annotations.put("before",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).before());
                annotations.put("after",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).after());
                annotations.put("mock_value",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).mock().value());
                annotations.put("mock_type",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).mock().type());
                annotations.put("description",((Method) r).getAnnotation(org.softauto.annotations.ListenerForTesting.class).description());
            }
            return visitor.visitListenerAnnotation(this,data,r);
        }
    }

    public static class TCExposedAnnotation extends TCTree implements ExposedAnnotationTree {



        HashMap<String,Object> annotations;

        @Override
        public HashMap<String,Object> getAnnotations(){
            return annotations;
        }



        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class) != null){
                annotations = new HashMap<>();
                annotations.put("description",((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class).description());
                annotations.put("protocol",((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class).protocol());
                annotations.put("before",((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class).before());
                annotations.put("after",((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class).after());
                annotations.put("dependencies",Utils.result2String(((Method) r).getAnnotation(org.softauto.annotations.ExposedForTesting.class).dependencies()));
            }
            return visitor.visitExposeAnnotation(this,data,r);
        }
    }

    public static class TCApiAnnotation extends TCTree implements ApiAnnotationTree {



        HashMap<String,Object> annotations;

        @Override
        public HashMap<String,Object> getAnnotations(){
            return annotations;
        }



        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class) != null){
                annotations = new HashMap<>();
                annotations.put("description",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).description());
                annotations.put("protocol",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).protocol());
                annotations.put("assertType",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).assertType().getValue());
                annotations.put("before",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).before());
                annotations.put("after",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).after());
                annotations.put("authentication",((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).authentication());
                annotations.put("dependencies",Utils.result2String(((Method) r).getAnnotation(org.softauto.annotations.ApiForTesting.class).dependencies()));
            }
            return visitor.visitApiAnnotation(this,data,r);
        }
    }

    public static class TCBeforeTestAnnotation extends TCTree implements BeforeTestTree {

        HashMap<String,Object> beforeTestAnnotations ;

        @Override
        public Kind getKind() {
            return null;
        }

        public HashMap<String,Object> getAnnotaionValue(Annotation annotation)  {
            HashMap<String,Object> hm = new HashMap<>();
            try {

                Class<? extends Annotation> type = annotation.annotationType();
                //System.out.println("Values of " + type.getName());

                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[]) null);
                    hm.put(method.getName(),value);
                    //System.out.println(" " + method.getName() + ": " + value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return hm;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if (r != null && r instanceof Method) {
                List<Annotation> _annotations = Arrays.asList(((Method) r).getAnnotations());
                for (Annotation annotation : _annotations) {
                    if (annotation.annotationType().getTypeName().equals("org.softauto.annotations.BeforeTest")) {
                        beforeTestAnnotations = new HashMap<>();
                        String type = annotation.annotationType().getTypeName();
                        Method[] f = annotation.annotationType().getMethods();
                        HashMap<String, Object> value = getAnnotaionValue(annotation);
                        beforeTestAnnotations.put(type, value);
                    }
                }
            }
            return visitor.visitBeforeTestAnnotation(this,data,r);
        }

        @Override
        public AuthenticationTree getAuthenticationTree() {
            return null;
        }

        @Override
        public HashMap<String, HashMap<String, Object>> getBeforeTest() {
            return null;
        }

        //@Override
        public HashMap<String, Object> getAnnotations() {
            return beforeTestAnnotations;
        }
    }

    public static class TCAfterTestAnnotation extends TCTree implements AfterTestTree {

        HashMap<String,Object> afterTestAnnotations ;

        @Override
        public Kind getKind() {
            return null;
        }

        public HashMap<String,Object> getAnnotaionValue(Annotation annotation)  {
            HashMap<String,Object> hm = new HashMap<>();
            try {

                Class<? extends Annotation> type = annotation.annotationType();
                //System.out.println("Values of " + type.getName());

                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[]) null);
                    hm.put(method.getName(),value);
                    //System.out.println(" " + method.getName() + ": " + value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return hm;
        }


        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if (r != null && r instanceof Method) {
                List<Annotation> _annotations = Arrays.asList(((Method) r).getAnnotations());
                for (Annotation annotation : _annotations) {
                    if (annotation.annotationType().getTypeName().equals("org.softauto.annotations.AfterTest")) {
                        afterTestAnnotations = new HashMap<>();
                        String type = annotation.annotationType().getTypeName();
                        Method[] f = annotation.annotationType().getMethods();
                        HashMap<String, Object> value = getAnnotaionValue(annotation);
                        afterTestAnnotations.put(type, value);
                    }
                }
            }
            return visitor.visitAfterTestAnnotation(this, data, r);
        }

        @Override
        public HashMap<String,Object> getAnnotations() {
            return  afterTestAnnotations;
        }
    }

    public static class TCAnnotation extends TCTree implements AnnotationTree{


        HashMap<String,Object> annotations = new HashMap<>();



        @Override
        public HashMap<String,Object> getAnnotations(){
            return annotations;
        }

        public HashMap<String,Object> getAnnotaionValue(Annotation annotation)  {
            HashMap<String,Object> hm = new HashMap<>();
            try {

                Class<? extends Annotation> type = annotation.annotationType();
                //System.out.println("Values of " + type.getName());

                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[]) null);
                    hm.put(method.getName(),value);
                    //System.out.println(" " + method.getName() + ": " + value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return hm;
        }

        @Override
        public Kind getKind() {
            return Kind.ANNOTATION;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            List<Annotation> _annotations = null;
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                _annotations = Arrays.asList(sig.getMethod().getAnnotations());

            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                _annotations = Arrays.asList(sig.getConstructor().getAnnotations());
            }
            for(Annotation annotation : _annotations){
                String type = annotation.annotationType().getTypeName();
                Method[] f = annotation.annotationType().getMethods();
                HashMap<String,Object> value = getAnnotaionValue(annotation);
                annotations.put(type,value);
            }


            return visitor.visitAnnotation(this,data,(R)this);
        }
    }



    public static class TCDefaultValue  extends TCTree implements DefaultValueTree {

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public AnnotationTree getAnnotations() {
            return null;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            return null;
        }
    }

    public static class TCTransceiver extends TCTree implements TransceiverTree{

        //AnnotationTree annotationTree;

        String transceiver;

        public String getTransceiver(AnnotationTree annotationTree) {
            try {
                HashMap<String,Object> annotations = annotationTree.getAnnotations();
                for(Map.Entry entry : annotations.entrySet()){
                    HashMap<String,Object> m = null;
                    if(entry.getKey().equals("org.softauto.annotations.ExposedForTesting") ) {
                        m = (HashMap<String, Object>) entry.getValue();
                    }else if(entry.getKey().equals("org.softauto.annotations.ApiForTesting") ) {
                        m = (HashMap<String, Object>) entry.getValue();
                    }
                    if(m != null) {
                        transceiver = m.get("protocol").toString();
                    }else {
                        transceiver = "RPC";
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return transceiver;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D d, R r) {
            return visitor.visitTransceiver(this,d,(R)this);
        }

        @Override
        public Kind getKind() {
            return Kind.TRANSCEIVER;
        }

    }


    public static class TCPackage  extends TCTree implements PackageTree {

        String name;

        public String getName(){
            return name;
        }

        @Override
        public Kind getKind() {
            return Kind.PACKAGE;
        }



        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                name = sig.getMethod().getName();
            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                name = sig.getDeclaringType().getName();
            }

            return visitor.visitPackage(this,data,(R)this);
        }
    }




    public static class TCMessage extends TCTree implements MessageTree {

        Object result = null;
        NamespaceTree namespaceTree;
        TypesTree typesTree;
        MethodTree methodTree;
        ClassTree classTree;
        RequestTree requestTree;
        ResponseTree responseTree;

        AnnotationTree annotationTree;

        int id;
        HashMap<String, Object> message;


        long _timestamp;
       // org.aspectj.lang.ProceedingJoinPoint jp;
        List<HashMap<String,Object>> events;

        public TCMessage(){

        }

        public TCMessage( Object result,int id,List<HashMap<String,Object>> events){

            this.events = events;
            this.id = id;
            this.result = result;
        }


        @Override
        public HashMap<String, Object> getMessage() {
            return message;
        }

        @Override
        public HashMap<String, Object> buildMessage() {
            MessageBuilder.Builder builder = null;
            try {
                builder = MessageBuilder.newBuilder().setMethod(methodTree.getName())
                        //.setDomain(domain)
                        .setNamespace(namespaceTree.getNamespace() != null ? namespaceTree.getNamespace() : null)
                        //.setTransceiver(transceiverTree != null ? transceiverTree.getTransceiver(annotationTree) : null)
                        //.setType(typeTree.getType() != null ? typeTree.getType().toString() : null)
                        //.setArgs(methodTree.args() != null ? (Object[]) methodTree.args() : null)
                        .setClazz(classTree != null ? (HashMap<String, Object>) classTree.getProperties(annotationTree) : null)
                        //.setResult(message.get("result") != null ? message.get("result") : null)
                        //.setId(message.get("id") != null ? Integer.valueOf(message.get("id").toString()) : -1)
                        //.setThreadId(_threadId != -1 ? _threadId : -1)
                        .setId(id)

                        //.set_id(message.get("_id") != null ? Integer.valueOf(message.get("_id").toString()) : -1)
                        .setAnnotations(annotationTree.getAnnotations() != null ? (HashMap<String,Object>) annotationTree.getAnnotations() : null)
                        //.setRec(message.get("rec") != null ? (List<Statement>) message.get("rec") : null)
                        //.setMessageId(_messageId != -1 ? _messageId : -1)
                        //.setKind(kindTree.get_Kind() != null ? kindTree.get_Kind() : null)
                        //.setElement(elementTree.getElement() != null ? elementTree.getElement() : null)
                        //.setRefType(refTree.getRef() != null ? RefType.valueOf(refTree.getRef()) : null)
                        //.setJoinPoint(jp)
                        .setEvents(events)
                        .setTimestamp(_timestamp != -1 ? _timestamp : -1);
                for (HashMap<String, Object> request : ((List<HashMap<String, Object>>) requestTree.getRequest())) {
                    String key = request.get("name").toString();
                    Class type = (Class) request.get("type");
                    Object value = request.get("value");
                    builder.addRequest(key, type, value);
                }
                String response =  responseTree.getResponse();
                //this.result = result;AnnotationTree annotationTree;
                HashMap<String,Object> annotations = annotationTree.getAnnotations();
                if(annotations.containsKey("org.softauto.annotations.ApiForTesting") || annotations.containsKey("org.softauto.annotations.VerifyForTesting")|| annotations.containsKey("org.softauto.annotations.BeforeTest")) {
                    builder.setResponse(response, result);
                }else {
                    builder.setResponse(response, null);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            //protocolTree.addMessage(builder.build());
            return builder.build();
        }


        @Override
        public AnnotationTree getAnnotations() {
            if(annotationTree == null) {
                annotationTree = new TCAnnotation();
            }
            return annotationTree;
        }



        @Override
        public NamespaceTree getNamespace() {
            if(namespaceTree == null) {
                namespaceTree = new TCNamespace();
            }
            return namespaceTree;
        }

        @Override
        public TypesTree getTypesTree() {
            if(typesTree == null) {
                typesTree = new TCTypes();
            }
            return typesTree;
        }

        @Override
        public MethodTree getMethod() {
            if(methodTree == null) {
                methodTree = new TCMethod();
            }
            return methodTree;
        }

        @Override
        public ClassTree getClazz() {
            if(classTree == null) {
                classTree = new TCClass();
            }
            return classTree;
        }

        @Override
        public RequestTree getRequest() {
            if(requestTree == null) {
                requestTree = new TCRequest();
            }
            return requestTree ;
        }

        @Override
        public  ResponseTree getResponse() {
            if(responseTree == null) {
                responseTree = new TCResponse();
            }
            return responseTree;
        }



        @Override
        public Kind getKind() {
            return Kind.MESSAGE;
        }


        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            try {
                //this.result = new ObjectMapper().writeValueAsString(r);
                this.result = r;
            }catch (Exception e){
                e.printStackTrace();
            }
            return visitor.visitMessage(this,data,(R)this);
            //message = buildMessage();
            //return r;
        }

        public <R,D> R accept(TreeVisitor<R,D> visitor, D data, R result, List<HashMap<String,Object>> events,int id){
            this.result = result;
            this.events = events;
            this.id = id;
            //System.out.println(" - " + Thread.currentThread().getId());
            return visitor.visitMessage(this,data,(R)this);
            //message = buildMessage();
            //return result;
        }

    }



    public static class TCRequest extends TCTree implements RequestTree{

        ParametersTree parametersTree;
        Object[] args;
        AnnotationTree annotationTree;

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            args = ((JoinPoint)data).getArgs();

            return visitor.visitRequest(this,data,(R)this);
        }

        @Override
        public Object[] getArgs() {
            return args;
        }

        @Override
        public AnnotationTree getAnnotationTree() {
            if(annotationTree == null){
                annotationTree = new TCAnnotation();
            }
            return annotationTree;
        }

        @Override
        public Kind getKind() {
            return Kind.REQUEST;
        }

        @Override
        public List<HashMap<String, Object>> getRequest() {
            List<HashMap<String,Object>> params = parametersTree.getParameters();
            HashMap<String,Object> annotations = annotationTree.getAnnotations();
            if(args != null && args.length > 0 && args.length == params.size()){
                for(int i=0;i<args.length;i++){
                    if(annotations.containsKey("org.softauto.annotations.ApiForTesting") || annotations.containsKey("org.softauto.annotations.VerifyForTesting") || annotations.containsKey("org.softauto.annotations.BeforeTest")) {
                        params.get(i).put("value", args[i]);
                    }else {
                        params.get(i).put("value", null);
                    }
                }
            }
            if(args.length != params.size()){
                System.out.println("args length different form params size");
            }
            return  params;
        }

        @Override
        public ParametersTree getParameter() {
            return parametersTree = new TCParameter();
        }
    }


    public static class TCResponse extends TCTree implements ResponseTree{

        ReturnTree returnTree;

        String type;

        HashMap<String, Object> response;

        @Override
        public String getResponse() {
            //response = new HashMap<>();
            //response.put(returnTree.getType(),returnTree.getValue());
            return type;
            //return returnTree.getType();
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                type = sig.getMethod().getReturnType().getTypeName();


            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                type = sig.getDeclaringType().getTypeName();

            }

            return visitor.visitResponse(this,data,(R)this);
        }

        @Override
        public ReturnTree getReturn() {
            return returnTree = new TCReturn();
        }

        @Override
        public Kind getKind() {
            return Kind.RESPONSE;
        }
    }





    public static class TCModifiers extends TCTree implements ModifiersTree{

        int modifiers;

        public int getModifiers(){
            return modifiers;
        }

        @Override
        public Kind getKind() {
            return Kind.MODIFIERS;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                modifiers = sig.getMethod().getModifiers();
            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                modifiers = sig.getDeclaringType().getModifiers();
            }
            return visitor.visitModifiers(this,data,(R)this);
        }
    }

    public static class TCReturn extends TCTree implements ReturnTree{

        String type;
        Object value;

        @Override
        public String getType(){
            return type;
        }

        @Override
        public Object getValue(){
            return value;
        }

        @Override
        public Kind getKind() {
            return Kind.RETURN;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                type = sig.getMethod().getReturnType().getTypeName();
                value = null;

            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                type = sig.getDeclaringType().getTypeName();
                value = null;
            }

            return visitor.visitReturn(this,data,(R)this);
        }
    }



    public static class TCVariable extends TCTree implements VariableTree {

        String name ;
        Class type;


        public Class getType() {
            return type;
        }


        @Override
        public Kind getKind() {
            return Kind.VARIABLE;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D d, R r) {
            if(d instanceof Parameter) {
                name = ((Parameter) d).getName();
                type = ((Parameter) d).getType();

                ((Parameter) d).isNamePresent();
                ((Parameter) d).isVarArgs();
            }


            return  visitor.visitVariable(this,d,(R)this);
        }

        @Override
        public String getName() {
            return name;
        }



    }

    public static class TCClass extends TCTree implements ClassTree{

        ModifiersTree modifiersTree;
        String name;
        //AnnotationTree annotationTree;
        HashMap<String,Object> properties = new HashMap<>();

        @Override
        public HashMap<String,Object> getProperties(AnnotationTree annotationTree){
            properties.put("annotations",annotationTree != null ? annotationTree.getAnnotations() : null);
            properties.put("modifiers",modifiersTree != null ? modifiersTree.getModifiers(): null);
            return properties;
        }

        @Override
        public ModifiersTree getModifiers() {
            return modifiersTree = new TCModifiers();
        }


        @Override
        public String getName() {
            return name;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                name = sig.getMethod().getDeclaringClass().getName();


            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                name = sig.getDeclaringType().getTypeName();

            }

            return visitor.visitClass(this,data,(R)this);
        }
    }




    public static class TCNamespace extends TCTree implements NamespaceTree{

        ClassTree classTree;

        @Override
        public Kind getKind() {
            return Kind.NAMESPACE;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            return visitor.visitNamespace(this,data,(R)this);
        }

        @Override
        public String getNamespace() {
            return classTree.getName();
        }

        @Override
        public ClassTree getClazz() {
            return classTree = new TCClass();
        }
    }



    public static class TCMethod extends TCTree implements MethodTree {

        Object[] args;
        String name;
        AnnotationTree annotationTree;
        ModifiersTree modifiersTree;
        ReturnTree returnTree;
        ParametersTree parametersTree;
        ClassTree classTree;

        @Override
        public Object[] args() {
            return args;
        }

        @Override
        public AnnotationTree getAnnotations() {
            return annotationTree = new TCAnnotation();
        }

        @Override
        public ModifiersTree getModifiers() {
            return modifiersTree = new TCModifiers();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ReturnTree getReturn() {
            return returnTree = new TCReturn();
        }

        @Override
        public ParametersTree getParameter() {
            return parametersTree = new TCParameter();
        }

        @Override
        public ClassTree getClazz() {
            return classTree = new TCClass();
        }

        @Override
        public Kind getKind() {
            return Kind.METHOD;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            //MethodSignature sig = null;
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                name = sig.getName();
                args = ((JoinPoint) data).getArgs();
                return visitor.visitMethod(this, data, (R) this);
            }else if(((JoinPoint) data).getSignature() instanceof FieldSignature){
                FieldSignature  sig = (FieldSignature) ((JoinPoint) data).getSignature();
                name = sig.getName();
                args = ((JoinPoint) data).getArgs();
                return visitor.visitMethod(this, data, (R) this);
            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                name = sig.getName();
                args = ((JoinPoint) data).getArgs();
                return visitor.visitMethod(this, data, (R) this);
            }else if(((JoinPoint) data).getSignature() instanceof MemberSignature){
                MemberSignature  sig = (MemberSignature) ((JoinPoint) data).getSignature();
            }else if(((JoinPoint) data).getSignature() instanceof AdviceSignature){
                AdviceSignature  sig = (AdviceSignature) ((JoinPoint) data).getSignature();
            }else if(((JoinPoint) data).getSignature() instanceof InitializerSignature){
                InitializerSignature  sig = (InitializerSignature) ((JoinPoint) data).getSignature();
            }else {
                System.out.println("Signature Type:"+((JoinPoint) data).getSignature().getDeclaringTypeName());
            }


            return visitor.visitMethod(this, data, (R)this);
        }
    }

    public static class TCParameter extends TCTree implements ParametersTree{

        List<HashMap<String, Object>> parameters = new ArrayList<>();
        VariableTree variableTree;

        @Override
        public List<HashMap<String, Object>> getParameters() {
            return parameters;
        }

        @Override
        public Kind getKind() {
            return Kind.PARAMETER;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            Parameter[] parameters = null;
            if(((JoinPoint) data).getSignature() instanceof MethodSignature) {
                MethodSignature sig = (MethodSignature) ((JoinPoint) data).getSignature();
                parameters = sig.getMethod().getParameters();
            } else if(((JoinPoint) data).getSignature() instanceof ConstructorSignature){
                ConstructorSignature  sig = (ConstructorSignature) ((JoinPoint) data).getSignature();
                parameters = sig.getConstructor().getParameters();

            }

            if(parameters != null) {
                for (Parameter p : parameters) {
                    visitor.visitParameters(this, (D) p, (R) this);
                    addParameter(variableTree.getName(), variableTree.getType());
                }
            }
           return visitor.visitParameters(this, data, (R)this);
        }

        @Override
        public VariableTree getVariable(){
            return variableTree = new TCVariable();
        }

        private void addParameter(String name,Class type){
            HashMap<String,Object> req = new HashMap<>();
            req.put("name",name);
            req.put("type",type);

            parameters.add(req);
        }
    }




    public static class TCTypes extends TCTree implements TypesTree{

        ParametersTree parametersTree;
        ReturnTree returnTree;
        List<HashMap<String,Object>> types = new ArrayList<>();

        @Override
        public List<HashMap<String,Object>> getTypes(){
            List<HashMap<String, Object>> parameters = parametersTree.getParameters();
            String returnType = returnTree.getType();
            for(HashMap<String, Object> p : parameters) {
                String t = ((Class)p.get("type")).getName();
                if (!Utils.isSchemaType(t) || t.contains("String"))
                    addType(t, "external");
            }
            if(returnType != null && (!Utils.isSchemaType(returnType)|| returnType.contains("String")))
                addType(returnType,"external");
            return types;
        }

        @Override
        public Kind getKind() {
            return Kind.TYPES;
        }

        @Override
        public ParametersTree getParameter() {
            if(parametersTree == null) {
                parametersTree = new TCParameter();
            }
            return parametersTree;
        }

        @Override
        public ReturnTree getReturn() {
            if(returnTree == null) {
                returnTree = new TCReturn();
            }
            return returnTree;
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R r) {
            return visitor.visitTypes(this,data,(R)this);
        }


        public void addType(String key,Object value){
            HashMap<String,Object> type = new HashMap<>();
            type.put("name",key);
            type.put("type",value);
            types.add(type);
        }
    }

    public static class TCDependence  extends TCTree implements DependenceTree {

        List<String> deps = new ArrayList<>();
        ArrayNode references;
        int id;

        public List<String> getDependence(){
            return deps;
        }


        public TCDependence(){

        }

        public TCDependence(ArrayNode references,int id){
            this.references = references;
            this.id = id;
        }

        @Override
        public Kind getKind() {
            return null;
        }

        private boolean isExist(List<String> nodes,String node){
            for(String n : nodes){
                //for(Map.Entry entry : n.entrySet()){
                //for(Map.Entry e : node.entrySet()) {
                if (node.equals(node)) {
                    return true;
                }
                //}
                // }
            }
            return false;
        }

        private List<String> findNodes(String refname,String process){
            List<String> nodes = new ArrayList<>();
            for(JsonNode node : references){
                //String p = node.get("clazz").asText().replace(".","_") + "_"+node.get("process").asText().replace(".","_")+"_"+node.get("processId").asText();
                if(!node.get("process").asText().equals(process)){
                    if((node.get("reftype").asText().equals("CREATE") || node.get("reftype").asText().equals("UPDATE") ) && node.get("refname").asText().equals(refname)) {
                        //HashMap<String, String> hm = new HashMap<>();
                        //hm.put(node.get("process").asText(),node.get("processId").asText());
                        nodes.add(node.get("process").asText()+"_"+node.get("processId").asText());
                    }
                }
            }
            return nodes;
        }

        private String getMessageId(String messageName){
            return messageName.substring(messageName.lastIndexOf("_")+1);
        }

        @Override
        public <R, D> R accept(TreeVisitor<R, D> visitor, D data, R result) {
            Protocol protocol =    (Protocol)data;

            id = Integer.valueOf(getMessageId(protocol.getMessages().keySet().toArray()[0].toString()));
            for(JsonNode node : protocol.getJsonProp("references")){
                if(node.get("processId").asText().equals(String.valueOf(id))){
                    if(node.get("reftype").asText().equals("READ") || node.get("reftype").asText().equals("UPDATE")) {
                        //String p = node.get("clazz").asText().replace(".","_") + "_"+node.get("process").asText().replace(".","_")+"_"+node.get("processId").asText();
                        List<String> nodes = findNodes(node.get("refname").asText(),node.get("process").asText());
                        for(String n : nodes) {
                            if(!isExist(deps,n)) {
                                deps.add(n);
                                //.addAll(findNodes(node.get("refname").asText(), node.get("process").asText()));
                            }
                        }
                    }
                }
            }
            return visitor.visitDependence(this,data,(R)this);
        }
    }
}
