/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.softauto.core;

//import org.apache.avro.ipc.Callback;

//import java.util.concurrent.Future;
import java.util.concurrent.*;

/**
 * A Future implementation for RPCs base on Avro.
 */
public class CallFuture<T> implements Future<T> {
  private final CountDownLatch latch = new CountDownLatch(1);
  private final Callback<T> chainedCallback;
  private T result = null;
  private Throwable error = null;
  private Object[] arguments = null;
  private Class[] argumentsType = null;
  private boolean succeeded= false;
  private boolean failed = false;
  private Throwable cause;
  //private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(CallFuture.class);

  /**
   * Creates a CallFuture.
   */
  public CallFuture() {
    this(null);
  }

  /**
   * Creates a CallFuture with a chained Callback which will be invoked when this
   * CallFuture's Callback methods are invoked.
   * 
   * @param chainedCallback the chained Callback to set.
   */
  public CallFuture(Callback<T> chainedCallback) {
    this.chainedCallback = chainedCallback;
  }


  public CallFuture handleArguments(Object[] arguments){
    this.arguments = arguments;
    return this;
  }

  public CallFuture handleArgumentsType(Class[] argumentsType){
    this.argumentsType = argumentsType;
    return this;
  }

  /**
   * Sets the RPC response, and unblocks all threads waiting on {@link #get()} or
   * {@link #get(long, TimeUnit)}.
   * 
   * @param result the RPC result to set.
   */

  public CallFuture handleResult(T result) {
    this.result = result;
    latch.countDown();
    if (chainedCallback != null) {
      chainedCallback.handleResult(result);
    }
    succeeded = true;
    return this;
  }

  /**
   * Sets an error thrown during RPC execution, and unblocks all threads waiting
   * on {@link #get()} or {@link #get(long, TimeUnit)}.
   * 
   * @param error the RPC error to set.
   */

  public CallFuture handleError(Throwable error) {
    this.error = error;
    latch.countDown();
    if (chainedCallback != null) {
      chainedCallback.handleError(error);
    }
    failed = true;
    cause = error;
    return this;
  }

  public Object[] getArguments() {
    return arguments;
  }

  public Class[] getArgumentsType() {
    return argumentsType;
  }

  /**
   * Gets the value of the RPC result without blocking. Using {@link #get()} or
   * {@link #get(long, TimeUnit)} is usually preferred because these methods block
   * until the result is available or an error occurs.
   * 
   * @return the value of the response, or null if no result was returned or the
   *         RPC has not yet completed.
   */
  public T getResult() {
    return result;
  }

  /**
   * Gets the error that was thrown during RPC execution. Does not block. Either
   * {@link #get()} or {@link #get(long, TimeUnit)} should be called first because
   * these methods block until the RPC has completed.
   * 
   * @return the RPC error that was thrown, or null if no error has occurred or if
   *         the RPC has not yet completed.
   */
  public Throwable getError() {
    return error;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    latch.await();
    if (error != null) {
      throw new ExecutionException(error);
    }
    return result;
  }

  @Override
  public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    if (latch.await(timeout, unit)) {
      if (error != null) {
        throw new ExecutionException(error);
      }
      return result;
    } else {
      throw new TimeoutException();
    }
  }

  /**
   * Waits for the CallFuture to complete without returning the result.
   * 
   * @throws InterruptedException if interrupted.
   */
  public void await() throws InterruptedException {
    latch.await();
  }

  /**
   * Waits for the CallFuture to complete without returning the result.
   * 
   * @param timeout the maximum time to wait.
   * @param unit    the time unit of the timeout argument.
   * @throws InterruptedException if interrupted.
   * @throws TimeoutException     if the wait timed out.
   */
  public void await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
    if (!latch.await(timeout, unit)) {
      throw new TimeoutException();
    }
  }

  @Override
  public boolean isDone() {
    return latch.getCount() <= 0;
  }

  @Override
  public T result() {
    return result;
  }

  @Override
  public Object[] arguments() {
    return new Object[0];
  }

  @Override
  public Class[] argumentsType() {
    return new Class[0];
  }

  @Override
  public Throwable cause() {
    return cause;
  }

  @Override
  public boolean succeeded() {
    return succeeded;
  }

  @Override
  public boolean failed() {
    return failed;
  }
}
