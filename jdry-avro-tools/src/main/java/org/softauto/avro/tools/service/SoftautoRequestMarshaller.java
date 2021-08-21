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

package org.softauto.avro.tools.service;


import com.google.common.io.ByteStreams;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.softauto.core.AbstractSerialization;
import org.softauto.serialization.KryoSerialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;


/** Marshaller for  RPC request.
 * base on Avro
 * */
public class SoftautoRequestMarshaller implements MethodDescriptor.Marshaller<Object[]> {
  private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SoftautoRequestMarshaller.class);
  private static final EncoderFactory ENCODER_FACTORY = new EncoderFactory();
  private static final DecoderFactory DECODER_FACTORY = new DecoderFactory();
  private final Protocol.Message message;
  static AbstractSerialization serializer;

  public SoftautoRequestMarshaller(Protocol.Message message) {
       this.message = message;
       serializer = KryoSerialization.getInstance();
  }

  @Override
  public InputStream stream(Object[] value) {
    try {
      return new DryRequestInputStream(value, message);
    }catch (Exception e){
      logger.error("stream fail ",e);
    }
    return null;
  }

  @Override
  public Object[] parse(InputStream stream) {
    try {
      BinaryDecoder in = DECODER_FACTORY.binaryDecoder(stream, null);
      Schema reqSchema = message.getRequest();
      List<Schema.Field> fields =  reqSchema.getFields();
      for(Schema.Field f : fields){
        f.schema().setType(Schema.Type.BYTES);
      }
      reqSchema.setFields(fields);
      GenericRecord request = (GenericRecord) new SpecificDatumReader<>(reqSchema).read(null, in);
      Object[] args = new Object[reqSchema.getFields().size()];
      int i = 0;
      for (Schema.Field field : reqSchema.getFields()) {
        Object obj = request.get(field.name());
        args[i++] = serializer.deserialize(((ByteBuffer) obj).array());
      }
      return args;
    } catch (Exception e) {
      throw Status.INTERNAL.withCause(e).withDescription("Error deserializing avro request arguments")
          .asRuntimeException();
    } finally {
      AvroGrpcUtils.skipAndCloseQuietly(stream);
    }

  }

  private static class DryRequestInputStream extends SoftautoInputStream {
    private final Protocol.Message message;
    private Object[] args;



    DryRequestInputStream(Object[] args, Protocol.Message message) throws Exception{
      this.args = new Object[args.length];
      for(int i=0;i<args.length;i++){
        if(args[i] != null) {
          this.args[i] = ByteBuffer.wrap(serializer.serialize( args[i]));
        }else {
          this.args[i] = new Byte[1];
        }
      }
      this.message = message;
    }

    @Override
    public int drainTo(OutputStream target) throws IOException {
      int written;
      if (getPartial() != null) {
        written = (int) ByteStreams.copy(getPartial(), target);
      } else {
        Schema reqSchema = message.getRequest();
        CountingOutputStream outputStream = new CountingOutputStream(target);
        BinaryEncoder out = ENCODER_FACTORY.binaryEncoder(outputStream, null);
        int i = 0;
        for (Schema.Field param : reqSchema.getFields()) {
            new SpecificDatumWriter<>(Schema.create(Schema.Type.BYTES)).write(this.args[i++], out);
        }
        out.flush();
        args = null;
        written = outputStream.getWrittenCount();
      }
      return written;
    }
  }
}
