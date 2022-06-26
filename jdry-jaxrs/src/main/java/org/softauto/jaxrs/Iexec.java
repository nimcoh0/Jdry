package org.softauto.jaxrs;

import io.grpc.ManagedChannel;

public interface Iexec {

    <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel, Object...args);
}
