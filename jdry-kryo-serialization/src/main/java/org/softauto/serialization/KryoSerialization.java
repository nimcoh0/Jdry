package org.softauto.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import de.javakaffee.kryoserializers.*;
import de.javakaffee.kryoserializers.guava.*;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalTimeSerializer;
import org.joda.time.DateTime;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.softauto.core.AbstractSerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;


/**
 * wrapper to Kryo Serialization
 */
public class KryoSerialization extends AbstractSerialization {

    public static Kryo kryo = null;

    private static KryoSerialization kryoSerialization;

    public static KryoSerialization getInstance(){
        if(kryoSerialization == null){
            kryoSerialization = new KryoSerialization();
        }
        return kryoSerialization;
    }

    private KryoSerialization(){
        kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setReferences(false);
        //CompatibleFieldSerializer.CompatibleFieldSerializerConfig config = new CompatibleFieldSerializer.CompatibleFieldSerializerConfig();
        //config.setChunkedEncoding(true);
        //config.setReadUnknownFieldData(true);
        //kryo.setDefaultSerializer(new SerializerFactory.CompatibleFieldSerializerFactory(config));
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        addSerializers();
    }



    @Override
    public synchronized byte[] serialize(Object obj) throws Exception{
        Output output = null;
        byte[] data = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
            //output = new Output(new ObjectOutputStream(outputStream));
            output = new Output(outputStream);
            serialize(obj, output);
            data = output.toBytes();
            //byte[] data =  outputStream.toByteArray();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(output != null){
                output.flush();
                output.close();
            }
        }
        return data;
    }


    protected synchronized void serialize(Object obj, Output outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        } else {
            kryo.writeClassAndObject(outputStream, obj);

        }
    }

    @Override
    public synchronized <T> T deserialize(final byte[] objectData) throws Exception{
        Input input = null;
        Object object = null;
        try {
            if (objectData == null) {
                throw new IllegalArgumentException("The byte[] must not be null");
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectData);
            input = new Input(byteArrayInputStream, (int) objectData.length);
            object = deserialize(input);

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return (T) object;
    }

    @Override
    public synchronized <T> T deserialize(byte[] objectData, Class<T> type) throws Exception {
        Input input = null;
        try {
            if (objectData == null) {
                throw new IllegalArgumentException("The byte[] must not be null");
            }
            input = new Input(objectData);
            Object object = deserialize(input,type);
            return (T) object;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    protected synchronized <T> T deserialize(Input  inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        try{
            return (T)kryo.readClassAndObject(inputStream);

        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
        }

    protected synchronized <T> T deserialize(Input  inputStream,Class<T> type) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        try{
            return (T)kryo.readObject(inputStream,type);

        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    private void addSerializers(){
        kryo.register( Arrays.asList( "" ).getClass(), new ArraysAsListSerializer() );
        kryo.register( Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer() );
        kryo.register( Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer() );
        kryo.register( Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer() );
        kryo.register( Collections.singletonList( "" ).getClass(), new CollectionsSingletonListSerializer() );
        kryo.register( Collections.singleton( "" ).getClass(), new CollectionsSingletonSetSerializer() );
        kryo.register( Collections.singletonMap( "", "" ).getClass(), new CollectionsSingletonMapSerializer() );
        kryo.register( GregorianCalendar.class, new GregorianCalendarSerializer() );
        kryo.register( InvocationHandler.class, new JdkProxySerializer() );
        UnmodifiableCollectionsSerializer.registerSerializers( kryo );
        SynchronizedCollectionsSerializer.registerSerializers( kryo );

// custom serializers for non-jdk libs

// register CGLibProxySerializer, works in combination with the appropriate action in handleUnregisteredClass (see below)
        //kryo.register( CGLibProxySerializer.CGLibProxyMarker.class, new CGLibProxySerializer(  ) );
// dexx
        //ListSerializer.registerSerializers( kryo );
        //MapSerializer.registerSerializers( kryo );
        //SetSerializer.registerSerializers( kryo );
// joda DateTime, LocalDate, LocalDateTime and LocalTime
        kryo.register( DateTime.class, new JodaDateTimeSerializer() );
        kryo.register( LocalDate.class, new JodaLocalDateSerializer() );
        kryo.register( LocalDateTime.class, new JodaLocalDateTimeSerializer() );
        kryo.register( LocalDateTime.class, new JodaLocalTimeSerializer() );
// protobuf
        //kryo.register( SampleProtoA.class, new ProtobufSerializer() ); // or override Kryo.getDefaultSerializer as shown below
// wicket
        //kryo.register( MiniMap.class, new MiniMapSerializer() );
// guava ImmutableList, ImmutableSet, ImmutableMap, ImmutableMultimap, ImmutableTable, ReverseList, UnmodifiableNavigableSet
        //ImmutableListSerializer.registerSerializers( kryo );
        //ImmutableSetSerializer.registerSerializers( kryo );
        //ImmutableMapSerializer.registerSerializers( kryo );
        //ImmutableMultimapSerializer.registerSerializers( kryo );
        //ImmutableTableSerializer.registerSerializers( kryo );
        //ReverseListSerializer.registerSerializers( kryo );
        //UnmodifiableNavigableSetSerializer.registerSerializers( kryo );
// guava ArrayListMultimap, HashMultimap, LinkedHashMultimap, LinkedListMultimap, TreeMultimap, ArrayTable, HashBasedTable, TreeBasedTable
        ArrayListMultimapSerializer.registerSerializers( kryo );
        HashMultimapSerializer.registerSerializers( kryo );
        LinkedHashMultimapSerializer.registerSerializers( kryo );
        LinkedListMultimapSerializer.registerSerializers( kryo );
        TreeMultimapSerializer.registerSerializers( kryo );
        ArrayTableSerializer.registerSerializers( kryo );
        HashBasedTableSerializer.registerSerializers( kryo );
        TreeBasedTableSerializer.registerSerializers( kryo );

    }

}
