package org.softauto.cucumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cucumber {



    private static Cucumber cucumber = null;
    static List<String> varList = new ArrayList<>();
    static List<String> varNames = new ArrayList<>();
    static HashMap<String,String> objectBuildNames = new HashMap<>();

    public static String getVarName(String name){
        return getVarName(name,true);
    }

    public static String getVarName(String name,boolean shortname){
       String tmpname =  name.replace(".", "_").replace("<", "_").replace(">", "_").replace(",","_");
       if(shortname){
           tmpname =  getShortVarName(tmpname);
       }
       if(varList.contains(tmpname) || isPrimitive(tmpname)){
            for(int i=0;i<1000;i++){
                if(!varList.contains(tmpname+i)){
                    varList.add(tmpname+i);
                    return tmpname+i;
                }
            }
        }
        varList.add(tmpname);
        return tmpname;
    }

   public static String getShortVarName(String name){
        if(name.contains("_")) {
            if (name.endsWith("_result")) {
                String n = name.replace("_result", "");
                return n.substring(n.lastIndexOf("_") + 1, n.length());
            } else {
                if(name.endsWith("_")){
                    name = name.substring(0,name.length()-1);
                    name = name.substring(name.lastIndexOf("_") + 1, name.length());
                    return name+"_";
                }
                return name.substring(name.lastIndexOf("_") + 1, name.length());
            }
        }else {
            return name;
        }
    }

    public static void addObjectName(String type,String name){
        objectBuildNames.put(type,name);
    }

    public static boolean hasObjectName(String type){
        if(objectBuildNames.containsKey(type)){
          return true;
        }
        return false;
    }

    public static String getObjectName(String name){
       return objectBuildNames.get(name);
    }


    public static void addVarName(String name){
        varNames.add(name);
    }

    public static boolean isDescriptionHasArguments(String description){
        if(description == null || description.isEmpty()){
            return true;
        }
        if(description.contains("{") || description.contains("(")){
            return true;
        }
        return false;
    }

    public String buildTableDescription(String description) {
        if((!description.contains("{") || !description.contains("(")) && !description.isEmpty() ){
            if(description.endsWith("$")){
                description = description.substring(0,description.length()-1)+ " # DataTable$";
            }else {
                description = description + " # DataTable$";
            }
        }
        if (description.contains("{") ) {
            String[] varListNames = substringsBetween(description, "{", "}");
            for (String var : varListNames) {
                description = description.replace("{" + var + "}", "");
            }
        }
        if ( description.contains("(")) {
            String[] varListNames = substringsBetween(description, "(", ")");
            for (String var : varListNames) {
                description = description.replace("(" + var + ")", "");
            }
        }
        varNames = new ArrayList<>();
        return description;
    }



    public String fqmnToString(String fqmn){
        return fqmn.replace(".","_").replace("<","_").replace(">","_");
    }

   public String buildDescription(String description) {
       if (description.contains("{")) {
           String[] varListNames = substringsBetween(description, "{", "}");
           for (String var : varListNames) {
               for (String varname : varNames) {
                   if (varname.contains(var)) {
                       description = description.replace("{" + var + "}", "{" + varname + "}");
                   }
               }
           }
       }
       varNames = new ArrayList<>();
       return description;
   }

   public static String getPrimitiveDefaultValue(String name){
        switch (name){
            case "init" : return "0";
            case "Integer" : return "0";
            case "boolean" : return "false" ;
            default : return "null";
        }
   }
    public static boolean isCollection(String name){
        if(name.contains(".")){
            name = name.substring(name.lastIndexOf(".")+1,name.length());
        }
        if(name.contains("[")){
            return true;
        }
        switch (name.toLowerCase()){
            case "list" : return true;
            case "collection" : return true;
            default: return false;
        }
    }

    public static boolean isPrimitive(String name){
        if(name.contains(".")){
            name = name.substring(name.lastIndexOf(".")+1,name.length());
        }
        if(PRIMITIVES.contains(name.toLowerCase())){
            return true;
        }
        return false;
    }

    static final List<String> PRIMITIVES = new ArrayList<>();
    static {
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
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String[] substringsBetween(final String str, final String open, final String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String [list.size()]);
    }
}