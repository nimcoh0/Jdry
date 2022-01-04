package org.softauto.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.avro.Compiler;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.softauto.annotations.DefaultValue;
import org.softauto.espl.ExpressionBuilder;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.MessageType;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

    /**
     * get protocol from schema interface
     * @param iface
     * @return
     */
    public static Protocol getProtocol(Class iface) {
        try {
            return (Protocol) (iface.getDeclaredField("PROTOCOL").get(null));
       } catch (Exception e) {
          logger.error("fail get protocol ",e);
        }
        return null;
    }



    private static ClassType getClassType(Protocol.Message msg){
        String initialize = null;
        if(msg.hasProp("class")) {
            initialize = ((HashMap) msg.getObjectProp("class")).get("initialize").toString();
        }else {
            return ClassType.INITIALIZE_NO_PARAM;
        }
        return ClassType.fromString(initialize);
    }




    /**
     * get class by currentThread
     * @param fullClassName
     * @return
     */
    public static Class findClass(String fullClassName){
        try{
          Class c =   Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
          return c;
        }catch (Exception e){
            logger.error("find Class fail " + fullClassName,e);
        }
        return null;
    }

    public static String capitalizeFirstLetter(String str){
        return str.toUpperCase().charAt(0)+str.substring(1,str.length());
    }

    /**
     * get Declared Method
     * @param o
     * @param fullMethodName
     * @param types
     * @return
     * @throws Exception
     */
    public static Method getMethod(Object o, String fullMethodName, Class[] types)throws Exception{
        try {
            Method m = null;
            if(o instanceof Class){
                m = ((Class)o).getDeclaredMethod(getMethodName(fullMethodName), types);
            }else {
                m = o.getClass().getDeclaredMethod(getMethodName(fullMethodName), types);
            }
            m.setAccessible(true);
            return m;
        }catch (Exception e){
            logger.error("fail get method "+ fullMethodName,e);
        }
        return  null;
    }


    /**
     * get method
     * @param o
     * @param fullMethodName
     * @param types
     * @return
     * @throws Exception
     */
    public static Method getMethod2(Object o, String fullMethodName, Class[] types)throws Exception{
        try {
            logger.debug("trying to find method " + fullMethodName +" with types "+ result2String(types)+ " on "+o.getClass().getName());
            Method[] m = o.getClass().getMethods();
            if (o instanceof Class<?>) {
                Class c = (Class) o;
                Method method = c.getMethod(getMethodName(fullMethodName), types);
                logger.debug("found method " + fullMethodName);
                return method;
            }
            Method method = o.getClass().getMethod(fullMethodName, types);
            logger.debug("found method " + fullMethodName);
            return method;
        }catch (Exception e){
            logger.warn("fail get method 2 "+ fullMethodName,e.getMessage());
            return null;

        }

    }

    /**
     * build Method Full Qualified Method Name
     * @param methodName
     * @param clazz
     * @return
     */
    public static String buildMethodFQMN(String methodName,String clazz){
        return clazz.replace(".","_")+"_"+methodName;
    }


    /**
     * get Full Class Name from FQMN
     * @param descriptor
     * @return
     */
    public static String getFullClassName(String descriptor){
        return  descriptor.substring(0,descriptor.lastIndexOf("_")).replace("_",".");
    }

    public static String getFullClassName2(String descriptor){
        return  descriptor.substring(0,descriptor.lastIndexOf("_"));
    }

    public static String getClassName(String descriptor){
       String str = getFullClassName2(descriptor);
       return  str.substring(str.lastIndexOf("_")+1);
    }

    /**
     * get Method Name from FQMN
     * @param descriptor
     * @return
     */
    public static String getMethodName(String descriptor){
        return descriptor.substring(descriptor.lastIndexOf("_")+1,descriptor.length());
    }

    public static String getVariableName(String descriptor){
        return descriptor.substring(descriptor.lastIndexOf("_")+1,descriptor.length());
    }

    /**
     * add URL to URLClassLoader
     * @param u
     * @param sysloader
     */
    public static void addURL(URL u,URLClassLoader sysloader)  {
        Class[] parameters = new Class[]{URL.class};
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL",parameters);
            method.setAccessible(true);
            method.invoke(sysloader,new Object[]{ u });
        } catch (Throwable t) {
            logger.error("add url fail "+ u ,t);
        }
    }

    /**
     * get class local or remote
     * @param path
     * @param clazzName
     * @param testMachine - remote machine ip
     * @return
     */
    public static Class getRemoteOrLocalClass(String path,String clazzName,String testMachine){
       try{
            String sutIp = Utils.getMachineIp();
            if(testMachine.equals("localhost") || testMachine.equals("127.0.0.1") || testMachine.equals(sutIp)){
                return getClazz(path,clazzName);
            }else {
                return getRemoteClazz(path,clazzName,testMachine);
            }
        }catch (Exception e){
            logger.error("fail get class "+ clazzName,e);
        }
        return null;
    }

    /**
     * get local class using URLClassLoader
     * @param path
     * @param clazzName
     * @return
     */
    public static Class getClazz(String path,String clazzName){
        Class c = null;
        try{
            String localPath = path.substring(0,path.lastIndexOf("classes")+8);
            String clazz = path.substring(path.lastIndexOf("classes") + 8, path.length()).replace("/", ".")+"."+clazzName;
            //URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            URL[] urls = new URL[1];
            urls[0] =(new File(localPath.trim()).toURL());
            URLClassLoader urlClassLoader = createClassLoader(urls );
            //addURL(new File(localPath).toURL(),sysloader);
            c = (Class) urlClassLoader.loadClass(clazz );
        }catch (ClassNotFoundException e){
            logger.warn("class not found"+ path+"/"+clazzName);
        }catch (Exception e){
            logger.error("fail get class "+ path+"/"+clazzName,e);
        }
        return c;
    }


    protected static URLClassLoader createClassLoader(URL[] _urls ) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.addAll(Arrays.asList(_urls));
        URLClassLoader uRLClassLoader =  new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(uRLClassLoader);
        return uRLClassLoader;
    }


    /**
     * get remote class using URLClassLoader
     * @param path
     * @param clazzName
     * @param testMachine
     * @return
     */
    public static Class getRemoteClazz(String path,String clazzName,String testMachine){
        Class c = null;
        try{
            String remotePath = path.substring(0,path.lastIndexOf("classes")+8).replace(":","$");
            String clazz = path.substring(path.lastIndexOf("classes") + 8, path.length()).replace("/", ".")+"."+clazzName;
            File f = new File("\\\\" + testMachine + "\\" + remotePath.replace("/", "\\"));
            URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            addURL(f.toURL(),sysloader);
            c = (Class) sysloader.loadClass(clazz );
        }catch (ClassNotFoundException e){
            logger.warn("class not found"+ path+"/"+clazzName);
        }catch (Exception e){
            logger.error("fail get class "+ path+"/"+clazzName,e);
        }
        return c;
    }

    /**
     * get local class using URLClassLoader
     * @param serviceInteface
     * @return
     */
    public static Class getClazz(String serviceInteface){
        Class c = null;
        try{
            String path = serviceInteface.substring(0,serviceInteface.lastIndexOf("classes")+8);
            String clazz = serviceInteface.substring(serviceInteface.lastIndexOf("classes")+8,serviceInteface.length()).replace("/",".");
            URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            addURL(new File(path).toURL(),sysloader);
            c = (Class) sysloader.loadClass(clazz);
        }catch (Exception e){
            logger.error("fail get class "+ serviceInteface,e);
        }
        return c;
    }


    /**
     * save json to file
     * @param json
     * @param path
     * @throws Exception
     */
    public static void save(String json,String path)throws Exception{
        FileWriter file = null;
        File f = new File(path.substring(0,path.lastIndexOf("/")));
        try{
            f.mkdirs();
            file = new FileWriter(path);

            file.write(json);
            logger.debug("save file " + path );
        }catch (Exception e){
            logger.error("fail save file "+  path, e);
        }finally {
            file.close();
        }

    }

    public static void update(String json,String path) throws IOException {
        FileWriter file = null;
        File f = new File(path.substring(0,path.lastIndexOf("/")));
        try{
            f.mkdirs();
            file = new FileWriter(path,true);

            file.write(json);
            logger.debug("save file " + path );
        }catch (Exception e){
            logger.error("fail save file "+  path, e);
        }finally {
            file.close();
        }
    }

    public static boolean isFileExist(String path){
        FileWriter file = null;
        File f = new File(path);
        if(f.exists()){
            return true;
        }
        return false;
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static JsonNode getTextFile(String file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            Path path = Paths.get(file);
            Stream<String> lines = Files.lines(path);
            String data = lines.collect(Collectors.joining("\n"));
            lines.close();
            //String json = objectMapper.writeValueAsString(data);
            node = objectMapper.readTree(data);

            logger.debug("successfully got file "+ file);
            return node;
        }catch(Exception e){
            logger.error("get file fail "+ file , e);
        }
        return node;
    }


    /**
     * get schema/JsonNode from file
     * @param file
     * @return
     * @throws Exception
     */
    public static JsonNode getFile(String file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        InputStream in = null;
        try {
            in = Utils.class.getClassLoader().getResourceAsStream(file);
            Yaml yaml = new Yaml();
            Object obj = yaml.load(in);
            if (obj != null) {
                String json = objectMapper.writeValueAsString(obj);
                node = objectMapper.readTree(json);
            }
            logger.debug("successfully got file "+ file);
            return node;
        }catch(Exception e){
            logger.error("get file fail "+ file , e);
        }finally {
            in.close();

        }
        return node;
    }

    /**
     * create Service Name from service interface protocol
     * @param iface
     * @return
     */
    public static String getServiceName(Class iface) {
        Protocol protocol = getProtocol(iface);
        return protocol.getNamespace() + "." + protocol.getName();
    }

    public static String getServiceName(Protocol protocol) {
       return protocol.getNamespace() + "." + protocol.getName();
    }

    /**
     * get parameter from JsonNode
     * whether it TextNode or String
     * @param params
     * @param param
     * @return
     */
    public static String getParam(JsonNode params, String param){
        if(params != null && params.has(param)){
            if (params.get(param) instanceof TextNode) {
                    return params.get(param).asText();
                } else {
                    return params.get(param).toString();
                }
            }
        return null;
    }


    /**
     * extract Full Method Name
     * @param fullMethodName
     * @return
     */
    public static String extractFullMethodName(String fullMethodName) {
        return  fullMethodName.replace("_",".");
    }


    /**
     * is method generic
     * @param protocol
     * @param key
     * @return
     */
    public  static int isGeneric(Protocol protocol,String key){
        Protocol.Message message = protocol.getMessages().get(key);
        if(message != null) {
            for (int i = 0; i < message.getRequest().getFields().size(); i++)
                if (message.getRequest().getFields().get(i).schema().getType().getName().equals("generic")) {
                    return i;
                }
        }
        return -1;
        }

    /**
     * extract Constructor Default Args Types
     * @param fullClassName
     * @return
     */
    public static Class[] extractConstructorDefaultArgsTypes(String fullClassName){
        try {
            Class c = Class.forName(fullClassName);
            Constructor[] constructors = c.getDeclaredConstructors();
            for(Constructor constructor: constructors){
                if( constructor.getParameters()[0].getAnnotation(DefaultValue.class) != null){
                    return constructor.getParameterTypes();
                }
            }
        }catch (Exception e){
            logger.error("fail extract Args Types for "+fullClassName,e);
        }
        return null;
    }



    /**
     * get Constructor Default Values
     * @return
     */
    public static Object[] getConstructorDefaultValues(String fullClassName){
        List<Object> defaultValues = new ArrayList<>();
        try {
            Class klazz = Class.forName(fullClassName);
            Constructor<?>[] constructors = klazz.getConstructors();
            for (Constructor constructor : constructors) {
                Annotation[][] annotations = constructor.getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    for (int j = 0; j < annotations[i].length; j++) {
                        if (annotations[i][j].annotationType().getName().equals("org.softauto.annotations.DefaultValue")) {
                            String value = ((org.softauto.annotations.DefaultValue) annotations[i][j]).value();
                            String name = constructor.getParameters()[i].getName();
                            if (constructor.getParameters()[i].getAnnotation(DefaultValue.class).type() != null && !constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().isEmpty()) {
                                String type = constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().toString();
                                defaultValues.add(ObjectConverter.convert(value, constructor.getParameters()[i].getType(), type));
                            } else {
                                defaultValues.add(value);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return defaultValues.toArray();
    }


    /**
     * get local Machine Ip
     * @return
     */
    public static String getMachineIp(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get local Machine Name
     * @return
     */
    public static String getMachineName(){
        try {
            return Inet4Address.getLocalHost().getHostName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get the arguments types
     **/
    public static Class[] getTypes(Object obj){
        Class[] types;
        if(obj instanceof Class<?>){
            types = new Class[1];
            types[0] = (Class)obj;
        }else {
            return (Class[])obj;
        }

        return types;
    }

    /**
     * get the arguments values
     **/
    public static Object[] getArgs(Object obj){
        Object[] args;

        if(!(obj instanceof Object[])){
            args = new Object[1];
            args[0] = obj;
        }else {
            return (Object[])obj;
        }

        return args;
    }



    public static String result2String(Object result){
        try{

            if(result != null){
                if(result instanceof List){
                    return ToStringBuilder.reflectionToString(((List)result).toArray(), new MultipleRecursiveToStringStyle());
                }else {
                    return ToStringBuilder.reflectionToString(result, new MultipleRecursiveToStringStyle());
                }
            }
        }catch(Exception e){
            logger.warn("result to String fail on  ",e.getMessage());
        }
        return "";
    }

    public static String toString(Object obj){
        return ToStringBuilder.reflectionToString(obj, new MultipleRecursiveToStringStyle());
    }


    public static String printStackTrace(Object e) {
        if(e instanceof String){
            return e.toString();
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ((Throwable)e).printStackTrace(printWriter);
        printWriter.flush();
        return writer.toString();
    }

    public static boolean isSchemaType(String t){
        Schema.Type[] types = Schema.Type.values();
        String  tt =  t.startsWith("java") ? t.substring(t.lastIndexOf(".") + 1): t;
        for(Schema.Type type : types){
            if(tt.toLowerCase().equals(type.getName().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static String getSchemaType(String t){
        Schema.Type[] types = Schema.Type.values();
        String tt = t.startsWith("java") ? t.substring(t.lastIndexOf(".")+1) : t;
        for(Schema.Type type : types){
            if(tt.toLowerCase().equals(type.getName().toLowerCase())){
                if(Schema.isPrimitive(type.getName())){
                    return type.getName();
                }else {
                    return new Compiler().javaType(type);
                }
            }
        }
        return t;
    }

    public static void addJarToClasspath(String f) {
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            addURL(new File(f).toURL(),classLoader);

        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
    public static boolean isJson(String str){
        try {
            if (str != null ){
                new ObjectMapper().readTree(str.toString());
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }




    public static boolean hasField(Class clazz, String fieldName){
        Field[] fields = clazz.getFields();
        for(Field field : fields){
            if(field.getName().equals(fieldName)){
                return true;
            }
        }
        return false;
    }


    public static String getActualTypeArgumentName(Object obj) throws Exception {
        if(obj instanceof List) {
            Type type = ((List)obj).get(0).getClass().getFields()[0].getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                return pt.getTypeName();
            }
        }
        return obj.toString();
    }


    public static String getTypeNameFromSchema(Schema schema){
        return  schema.getType().getName();
    }


    public static List<String> getTypesNameFromSchema(List<Schema.Field> schema){
        List<String> list = new ArrayList<>();
        for(Schema.Field f : schema){
            list.add(f.schema().getType().getName());
        }
        return  list;
    }

    public static Method getMethodByName(String methodName,Class clazz){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }

    public static String getAttributeFromSchema(Protocol.Message msg,String attribute){
        if(msg.getObjectProp(attribute) != null){
           return msg.getObjectProp(attribute).toString();
        }
        return null;
    }


    public static Method getMethodByNameAndTypeNames(String methodName,Class clazz,Protocol.Message msg){
        Schema request = msg.getRequest();
        List<String> t = getTypesNameFromSchema(request.getFields());
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                Class<?>[]   parameterTypes =   m.getParameterTypes();
                Object[] finalTypes = null;
                if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                        && CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                    finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                }else {
                    finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length );
                }
                if(t.size() == finalTypes.length){
                    for(int i=0;i<t.size();i++){
                        String t1 = parameterTypes[i].getTypeName().toLowerCase();
                        if(!t1.contains(t.get(i).toLowerCase())){
                        return null;
                        }
                     }
                    return m;
                }
            }
        }
        return null;
    }

    public static String getCurrentTimeUTCAsString(){
        return convertToDbTimeStr(getCurrentTimeUTC());
    }

    public static DateTime getCurrentTimeUTC() {
        return new DateTime(DateTimeZone.UTC);
    }

    private static DateTimeFormatter getDateTimeFormatter(String format) {
        return DateTimeFormat.forPattern(format).withZoneUTC();
    }

    public static String convertToDbTimeStr(DateTime dateTime) {
        DateTimeFormatter dtf = getDateTimeFormatter(Context.DEFUALT_DATETIME_FORMAT);
        return dtf.print(dateTime);

    }

    public static Class<?> getSubClass(Class<?>[] classes, String name){
        for(Class<?> c : classes){
            if(c.getName().equals(name)){
                logger.debug("successfully found subclass for "+name);
                return c;
            }
        }
        logger.warn("subclass not found for "+name);
        return null;
    }



    public static Protocol getProtocol(String ctx){
        if(Configuration.getConfiguration().containsKey(ctx)) {
            String json = Configuration.get(ctx);
            return Protocol.parse(json);
        }
        return null;
    }

    public static Protocol.Message getMsg(String methodName,Class[] types){
        Protocol protocol = getProtocol(Context.STEP_SERVICE);
        if(protocol != null){
            boolean find = false;
            for(Map.Entry<String, Protocol.Message> msg : protocol.getMessages().entrySet() ){
                if(msg.getKey().equals(methodName)){
                    List<Schema.Field> fields =  msg.getValue().getRequest().getFields();
                    for(Schema.Field field : fields){
                        String type = field.schema().getNamespace()+"."+field.schema().getName();
                        for(Class t : types){
                            if(t.getTypeName().equals(type)){
                                find = true;
                            }
                        }
                    }
                    if(find){
                        return msg.getValue();
                    }
                }
            }
        }
        return null;
    }


    public static ClassType getClassType(String methodName, Class[] types){
        try{
            Protocol.Message msg = getMsg(methodName,types);
            if(msg != null) {
                String initialize = msg.getJsonProp("class").get("initialize").asText();
                return ClassType.fromString(initialize);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ClassType.NONE;
    }

    public static MessageType getMessageType(String methodName,Class[] types){
        try{
            Protocol.Message msg = getMsg(methodName,types);
            if(msg != null) {
                String type = msg.getProp("type");
                return MessageType.fromString(type);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageType.NONE;
    }

}
