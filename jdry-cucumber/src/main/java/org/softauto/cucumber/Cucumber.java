package org.softauto.cucumber;

import org.apache.commons.lang3.StringUtils;
import java.util.*;

public class Cucumber {


    private static Cucumber cucumber = null;
    static List<String> varList = new ArrayList<>();
    static List<String> varNames = new ArrayList<>();

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

    public static void addVarName(String name){
        varNames.add(name);
    }

   public String buildDescription(String description) {
       if (description.contains("{")) {
           String[] varListNames = StringUtils.substringsBetween(description, "{", "}");
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

    public static boolean isPrimitive(String name){
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

}
