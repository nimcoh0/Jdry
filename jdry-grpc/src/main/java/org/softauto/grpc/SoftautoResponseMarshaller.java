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

package org.softauto.grpc;

import com.google.common.io.ByteStreams;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;
import org.softauto.serialization.KryoSerialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/** Marshaller for  RPC response.
 * base on Avro
 * */
public class SoftautoResponseMarshaller implements MethodDescriptor.Marshaller<Object> {
  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SoftautoResponseMarshaller.class);
  private static final EncoderFactory ENCODER_FACTORY = new EncoderFactory();
  private static final DecoderFactory DECODER_FACTORY = new DecoderFactory();
  private final Protocol.Message message;

  public SoftautoResponseMarshaller(Protocol.Message message) {
    this.message = message;
  }

  @Override
  public InputStream stream(Object value) {
    return new DryResponseInputStream(value, message);
  }

  @Override
  public Object parse(InputStream stream) {
    try {
      if (message.isOneWay())
        return null;
      BinaryDecoder in = DECODER_FACTORY.binaryDecoder(stream, null);
      if (!in.readBoolean()) {
        Object obj = new SpecificDatumReader(Schema.create(Schema.Type.BYTES)).read(null, in);
        Object response = KryoSerialization.getInstance().deserialize(((ByteBuffer) obj).array());
        return response;
      } else {
        Object value = new SpecificDatumReader(message.getErrors()).read(null, in);
        if (value instanceof Exception) {
          return value;
        }
        return new AvroRuntimeException(value.toString());
      }
    } catch (Exception e) {
      throw Status.INTERNAL.withCause(e).withDescription("Error deserializing avro response").asRuntimeException();
    } finally {
      SoftautoGrpcUtils.skipAndCloseQuietly(stream);
    }
  }

  private static class DryResponseInputStream extends SoftautoInputStream {
    private final Protocol.Message message;
    private Object response;

    DryResponseInputStream(Object response, Protocol.Message message) {
      this.response = response;
      this.message = message;
    }

    @Override
    public int drainTo(OutputStream target) throws IOException {
      int written;
      if (getPartial() != null) {
        written = (int) ByteStreams.copy(getPartial(), target);
      } else {
        written = writeResponse(target);
      }
      return written;
    }

    private int writeResponse(OutputStream target) throws IOException {
      int written = -1;
      try {
        if (message.isOneWay()) {
          written = 0;
        } else if (response instanceof Exception) {
          ByteArrayOutputStream bao = new ByteArrayOutputStream();
          BinaryEncoder out = ENCODER_FACTORY.binaryEncoder(bao, null);
          try {
            out.writeBoolean(true);
            new SpecificDatumWriter(message.getErrors()).write(response, out);
          } catch (Exception e) {
            bao = new ByteArrayOutputStream();
            out = ENCODER_FACTORY.binaryEncoder(bao, null);
            out.writeBoolean(true);
            new SpecificDatumWriter(Protocol.SYSTEM_ERRORS).write(new Utf8(e.toString()), out);
          }
          out.flush();
          byte[] serializedError = bao.toByteArray();
          target.write(serializedError);
          written = serializedError.length;
        } else {
          CountingOutputStream outputStream = new CountingOutputStream(target);
          BinaryEncoder out = ENCODER_FACTORY.binaryEncoder(outputStream, null);
          out.writeBoolean(false);
          if (response == null) {
            response = new Byte[1];
          }
          ByteBuffer byteBuffer = ByteBuffer.wrap(KryoSerialization.getInstance().serialize(response));
          new SpecificDatumWriter(Schema.create(Schema.Type.BYTES)).write(byteBuffer, out);
          out.flush();
          written = outputStream.getWrittenCount();
        }
        response = null;
      }catch (Exception e){
        logger.error("fail write Response ",e);
      }
      return written;
    }
  }
}
