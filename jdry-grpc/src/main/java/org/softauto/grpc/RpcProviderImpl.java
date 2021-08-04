package org.softauto.grpc;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.*;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.softauto.core.*;
import org.softauto.core.Context;
import org.softauto.grpc.schema.MessageHandler;
import org.softauto.grpc.system.SystemServiceImpl;
import org.softauto.logger.LogManager;
import org.softauto.plugin.api.Provider;

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


    /**
     * load the system server
     * @return
     * @throws IOException
     */
    @Override
    public Provider initilize() throws IOException {
        try {
            LogManager.setStatus(true);
            logger.info("starting System server ....");
            server = ServerBuilder.forPort(systemPort)
                   .addService(SystemServer.createServiceDefinition(org.softauto.grpc.system.SystemService.class,SystemServiceImpl.getInstance()))
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

    /**
     * load the grpc server
     * @return
     * @throws IOException
     */
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

    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }

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



}
