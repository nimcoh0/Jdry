package org.softauto.grpc;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.*;
import org.softauto.core.*;
import org.softauto.grpc.schema.MessageHandler;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import javax.lang.model.element.Element;
import java.io.IOException;

/**
 * grpc server impl
 * this class is singleton
 */
public class RpcProviderImpl implements Provider {

    private static RpcProviderImpl rpcProviderImpl = null;
    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(RpcProviderImpl.class);

    /** default listener host **/
    String host = "localhost";

    /** default protocol type **/
    String type = "RPC";

    /** default grpc server port **/
    int port = 8085;

    /** default system server port **/
    int systemPort = 8086;

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


    /*
    @Override
    public Provider initilize() throws IOException {
        try {
            LogManager.setStatus(true);
            logger.info("starting System server ....");
            server = ServerBuilder.forPort(systemPort)
                   //.addService(SystemServer.createServiceDefinition(org.softauto.system.SystemService.class,SystemServiceImpl.getInstance()))
                   .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, new org.softauto.grpc.SerializerServiceImpl()))
                   .build();
            server.start();
            logger.info("System server started on port : "+ port);

        }catch (Exception e){
            logger.fatal("fail to start System server ", e);
            System.exit(1);
        }finally {

        }
        return this;
    }
*/
    /**
     * load the grpc server
     * @return
     * @throws IOException
     */
    /*
    public Provider initilizeSerializer() throws IOException {
        try {
            logger.info("starting Serializer server ....");
            server = ServerBuilder.forPort(port)
                    .addService(SoftautoGrpcServer.createServiceDefinition(Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.STEP_SERVICE,Configuration.get(Context.TEST_MACHINE).asText())))
                    .build();
            server.start();
            logger.info("Serializer server started on port : "+ port);
        }catch (Exception e){
            logger.fatal("fail to start Serializer server ", e);
            System.exit(1);
        }
        return this;
    }

     */
    @Override
    public Provider initilize() throws IOException {
        try {
            org.softauto.serializer.SoftautoGrpcServer.setSerializationEngine(org.softauto.serializer.kryo.KryoSerialization.getInstance());
            server = ServerBuilder.forPort(port)
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, new org.softauto.grpc.SerializerServiceImpl()))
                    .build();
            server.start();

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

    /*
    @Override
    public <RespT> void exec(String methodName, Object[] args, CallFuture<RespT> callback,ManagedChannel channel){
        try {
            logger.debug("exec rpc call "+ methodName);
            MethodDescriptor<Object[], Object> m = ServiceDescriptor.create(iface).getMethod(methodName, MethodDescriptor.MethodType.UNARY);
            if(channel == null) {
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            }
            StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, channel);
            ClientCalls.asyncUnaryCall(channel.newCall(m, CallOptions.DEFAULT), args, observerAdpater);
        }catch (Exception e){
           logger.error("fail exec rpc call "+ methodName, e);
        }
    }

     */

    @Override
    public <RespT> void exec(String methodName,  CallFuture<RespT> callback,ManagedChannel channel,Object...args){
        try {
            logger.debug("exec rpc call "+ methodName);
            Serializer serializer;
            if(channel != null) {
                serializer = new Serializer().setChannel(channel);
            }else {
                serializer = new Serializer().setHost(host).setPort(port).buildChannel();
            }
            Message message = Message.newBuilder().setDescriptor(methodName).setArgs((Object[]) args[0]).setTypes((Class[]) args[1]).build();
            serializer.write(message,callback);
        }catch (Exception e){
            logger.error("fail exec rpc call "+ methodName, e);
        }
    }

}
