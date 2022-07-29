package org.softauto.scanner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.avro.JsonProperties;
import org.apache.avro.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public  static <T> T loadSchema(String path, String name,Class clazz) throws URISyntaxException, IOException {
        return (T) new ObjectMapper().readValue(Paths.get(path +"/"+name).toFile(),  clazz);
    }

    public static void toJson(String filename, String path,Object obj)throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        save(schema, path + filename+".json");
    }

    public static void save(String json,String path)throws Exception{
        FileWriter file = null;
        File f = new File(path.substring(0,path.lastIndexOf("/")));
        try{
            f.mkdirs();
            file = new FileWriter(path);

            file.write(json);
            //logger.debug("save file " + path );
        }catch (Exception e){
            //logger.error("fail save file "+  path, e);
        }finally {
            file.close();
        }

    }

    public static boolean isAnnotationExist(String name, HashMap<String,Object> annotations){
        for(Map.Entry entry : annotations.entrySet()){
                if(entry.getKey().equals(name) && entry.getValue() != JsonProperties.NULL_VALUE){
                    return true;
                }
            }
        return false;
    }


    public static boolean isAnnotationExist(String name, ArrayList annotations){
        for(Object node :annotations){
            for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                if(entry.getKey().equals(name) && entry.getValue() != JsonProperties.NULL_VALUE){
                    return true;
                }
            }
        }
        return false;
    }



    public static HashMap<String ,Object> getAnnotationOther(ArrayList annotations){
        HashMap<String ,Object> hm = new HashMap<>();
        for(Object node :annotations){
            for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                if(entry.getKey().equals("Other")  && entry.getValue() != JsonProperties.NULL_VALUE){
                    for(Map.Entry e :((HashMap<String,Object>)entry.getValue()).entrySet()){
                        hm.put(e.getKey().toString(),e.getValue());
                    }

                }
            }
        }
        return hm;
    }

    public static String getAnnotationProperty(String name, ArrayList annotations){
        for(Object node :annotations){
            for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                if(entry.getKey().equals("ExposedForTesting")  && entry.getValue() != JsonProperties.NULL_VALUE){
                    for(Map.Entry e :((HashMap<String,Object>)entry.getValue()).entrySet()){
                        if(e.getKey().equals(name)){
                            return e.getValue().toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static  Object getAnnotationProperty(String name, ArrayList annotations,String annotation){
        for(Object node :annotations){
            for(Map.Entry entry : ((HashMap<String,Object>)node).entrySet()){
                if(entry.getKey().equals(annotation) && entry.getValue() != JsonProperties.NULL_VALUE){
                    for(Map.Entry e :((HashMap<String,Object>)entry.getValue()).entrySet()){
                        if(e.getKey().equals(name)){
                            return e.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }


    public static  Object getAnnotationProperty(String name, HashMap<String,Object> annotations,String annotation){
       // for(Object node :annotations){
            for(Map.Entry entry : annotations.entrySet()){
                if(entry.getKey().equals(annotation) && entry.getValue() != JsonProperties.NULL_VALUE){
                    for(Map.Entry e :((HashMap<String,Object>)entry.getValue()).entrySet()){
                        if(e.getKey().equals(name)){
                            return e.getValue();
                        }
                    }
                }
            }
        //}
        return null;
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
            //logger.warn("result to String fail on  ",e.getMessage());
        }
        return "";
    }

    public static boolean isPrimitive(String type){
        List<String> PRIMITIVES = new ArrayList<>();
        PRIMITIVES.add("string");
        PRIMITIVES.add("bytes");
        PRIMITIVES.add("int");
        PRIMITIVES.add("long");
        PRIMITIVES.add("float");
        PRIMITIVES.add("double");
        PRIMITIVES.add("boolean");
        PRIMITIVES.add("integer");
        PRIMITIVES.add("null");
        PRIMITIVES.add("void");

        if(type.contains(".")){
            type = type.substring(type.lastIndexOf(".")+1,type.length());
        }
        if(PRIMITIVES.contains(type.toLowerCase())){
            return true;
        }
        return false;
    }

    public static boolean isSchemaType(String t){
        if(t != null) {
            Schema.Type[] types = Schema.Type.values();
            String tt = t.startsWith("java") ? t.substring(t.lastIndexOf(".") + 1) : t;
            for (Schema.Type type : types) {
                if (tt.toLowerCase().equals(type.getName().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }


}
