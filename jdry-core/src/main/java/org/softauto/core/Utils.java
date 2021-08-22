package org.softauto.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.softauto.annotations.DefaultValue;
import org.softauto.injector.Injector;
import org.softauto.jvm.HeapHelper;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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


    /**
     * get Class Instance - this method will try to load or get exist class instance
     * @param fullClassName
     * @param msg
     * @param request
     * @param method
     * @return
     */
    public static Object getClassInstance(String fullClassName,Protocol.Message msg,Object[] request,Method method){
        List<Object>  objs = null;
        try {

            Class klass = findClass(fullClassName);
            if(!msg.hasProp("type") || !msg.getProp("type").equals("static")) {
                if (klass != null && msg.hasProp("type") && msg.getProp("type").equals("method")) {
                    Object[] objects = HeapHelper.getInstances(klass);
                    if (objects != null)
                        objs = Arrays.asList(objects);
                }
                if (objs != null && objs.size() > 0) {
                    return objs.get(0);
                } else {
                    logger.debug("instance not found for class " + fullClassName + " inject...");
                    //AbstractInjector injector = (AbstractInjector) ServiceLocator.getInstance().getService("INJECTOR");
                    //if (injector == null) {
                     //   logger.error("no injector found");
                     //   new Exception("no injector found ");
                    //}
                    ClassType initialize = getClassType(msg);
                    Object[] result = null;
                    if (msg.hasProp("type")  && msg.getProp("type").equals("constructor")) {
                        result = Injector.inject(fullClassName,initialize,getConstructorDefaultValues(klass),method.getParameterTypes());
                        //injector.UpdateClassDescriptorArgsValues(fullClassName, request);
                    } else {
                        result = Injector.inject(fullClassName,initialize,request,method.getParameterTypes());
                        //injector.UpdateClassDescriptorArgsValues(fullClassName, getConstructorDefaultValues(klass));
                    }


                   // Object[] result = Injector.inject(fullClassName,initialize,request,method.getParameterTypes());
                    return result[0];
                }
            }else {
                return  klass;
            }
        }catch (Exception e){
            logger.error("fail get Class Instances "+ fullClassName,e);
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

    /*
    public static Object getClassInstance(String fullClassName,Protocol.Message msg,Object[] request,Method method){
        List<Object>  objs = null;
        try {

            Class klass = findClass(fullClassName);
            if(!msg.hasProp("type") || !msg.getProp("type").equals("static")) {
                if (klass != null && msg.hasProp("type") && msg.getProp("type").equals("method")) {
                    Object[] objects = HeapHelper.getInstances(klass);
                    if (objects != null)
                        objs = Arrays.asList(objects);
                }
                if (objs != null && objs.size() > 0) {
                    return objs.get(0);
                } else {
                    logger.debug("instance not found for class " + fullClassName + " inject...");
                    AbstractInjector injector = (AbstractInjector) ServiceLocator.getInstance().getService("INJECTOR");
                    if (injector == null) {
                        logger.error("no injector found");
                        new Exception("no injector found ");
                    }
                    if (msg.getProp("type") != null && msg.getProp("type").equals("constructor")) {
                        injector.UpdateClassDescriptorArgsValues(fullClassName, request);
                    } else {
                        injector.UpdateClassDescriptorArgsValues(fullClassName, getConstructorDefaultValues(klass));
                    }

                    Object[] result = injector.inject(fullClassName);
                    return result[0];
                }
            }else {
                return  klass;
            }
        }catch (Exception e){
            logger.error("fail get Class Instances "+ fullClassName,e);
        }
        return null;
    }
    */


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
            Method[] m = o.getClass().getMethods();
            if (o instanceof Class<?>) {
                Class c = (Class) o;
                return c.getMethod(getMethodName(fullMethodName), types);
            }
            return o.getClass().getMethod(fullMethodName, types);
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

    /**
     * get Method Name from FQMN
     * @param descriptor
     * @return
     */
    public static String getMethodName(String descriptor){
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
            URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            addURL(new File(localPath).toURL(),sysloader);
            c = (Class) sysloader.loadClass(clazz );
        }catch (Exception e){
            logger.error("fail get class "+ path+"/"+clazzName,e);
        }
        return c;
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
     * @param klazz
     * @return
     */
    public static Object[] getConstructorDefaultValues(Class klazz){
        List<Object> defaultValues = new ArrayList<>();
        Constructor<?>[]  constructors = klazz.getConstructors();
        for(Constructor constructor : constructors){
            Annotation[][] annotations = constructor.getParameterAnnotations();
            for(int i=0;i<annotations.length;i++){
                for(int j=0;j<annotations[i].length;j++) {
                    if(annotations[i][j].annotationType().getName().equals("org.softauto.annotations.DefaultValue")){
                        String value = ((org.softauto.annotations.DefaultValue)annotations[i][j]).value();
                        String name = constructor.getParameters()[i].getName();
                        if(constructor.getParameters()[i].getAnnotation(DefaultValue.class).type() != null && !constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().isEmpty()) {
                            String type = constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().toString();
                            defaultValues.add (ObjectConverter.convert(value,constructor.getParameters()[i].getType(),type));
                        }else {
                            defaultValues.add(value);
                        }
                     }
                }
            }
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


    /**
     * extract object to String
     * @param result
     * @param logIgnoreList
     * @return
     */
    public static String result2String(Object result,List<String> logIgnoreList){
        try{

            if(result != null){
                if(logIgnoreList.contains(result.getClass().getName())){
                    return "set as log ignore";
                }
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
                return type.getName();
            }
        }
        return t;
    }

    public static void addToClasspath(String f) {
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            String path = f.substring(0,f.lastIndexOf("/"));
            String fileName = f.substring(f.lastIndexOf("/")+1);
            addURL(new File(path).toURL(),classLoader);
            File file = new File(classLoader.getResource(fileName).getFile());
            //URL url = file.toURI().toURL();
            //Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            //method.setAccessible(true);
            //method.invoke(classLoader, url);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

}
