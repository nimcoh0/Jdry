package org.softauto.grpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.*;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.softauto.core.*;
import org.softauto.core.Context;
import org.softauto.grpc.schema.MessageHandler;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageType;
import org.softauto.serializer.service.SerializerService;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * grpc server impl
 * this class is singleton
 */
public class RpcProviderImpl implements Provider {

    private static RpcProviderImpl rpcProviderImpl = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RpcProviderImpl.class);

    /** default listener host **/
    String host = "localhost";

    /** default protocol type **/
    String type = "RPC";

    /** default grpc server port **/
    int port = 8085;



    Server server = null;

    /** the interface for this service **/
    Class iface;




    public static RpcProviderImpl getInstance(){
        if(rpcProviderImpl == null){
            rpcProviderImpl =  new RpcProviderImpl();
        }
        return rpcProviderImpl;
    }



    public RpcProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }




    @Override
    public JsonNode parser(Element element) {
        return  new MessageHandler().parser(element);
    }


    @Override
    public void shutdown() {
    }

    @Override
    public String getType() {
        return type;
    }




    @Override
    public Provider initialize() throws IOException {
        try {
            org.softauto.serializer.SoftautoGrpcServer.setSerializationEngine(org.softauto.serializer.kryo.KryoSerialization.getInstance());
            server = ServerBuilder.forPort(port)
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, new org.softauto.grpc.SerializerServiceImpl()))
                    .build();
            server.start();
            logger.info("Grpc Server load successfully on port "+port);
        }catch (Exception e){
            logger.fatal("fail to start Serializer server ", e);
            System.exit(1);
        }
        return this;
    }



    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }




    @Override
    public <RespT> void exec(String name,  CallFuture<RespT> callback,ManagedChannel channel,Object...args){
        try {
            logger.debug("exec rpc call "+ name);
            Serializer serializer;
            if(channel != null) {
                serializer = new Serializer().setChannel(channel);
            }else {
                serializer = new Serializer().setHost(host).setPort(port).build();
            }
            ClassType classType = Utils.getClassType(name,(Class[]) args[1]);
            MessageType messageType = Utils.getMessageType(name,(Class[]) args[1]);
            if(args.length > 2 && args[2] != null){
                HashMap<String,Object> callOptions = (HashMap<String,Object>)args[2];
                if(callOptions.get("classType")!= null){
                    classType = ClassType.fromString(callOptions.get("classType").toString());
                }
                if(callOptions.get("messageType")!= null){
                    messageType = MessageType.fromString(callOptions.get("messageType").toString());
                }
            }
            Message message = Message.newBuilder().setDescriptor(name).setType(messageType).setArgs((Object[]) args[0]).setTypes((Class[]) args[1]).addData("classType",classType.name()).build();
            serializer.write(message,callback);
            logger.debug("callback value "+callback.getResult()+" get error "+callback.getError());
        }catch (Exception e){
            logger.error("fail exec rpc call "+ name, e);
        }
    }



}
