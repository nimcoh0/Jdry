package org.softauto.cucumber;

import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

public class Cucumber {


    static String description;
    static List<String> varList = new ArrayList<>();
    static List<String> varListNames = new ArrayList<>();
    static List<String> varNames = new ArrayList<>();

    public static String getVarName(String name){
       if(varList.contains(name)){
            for(int i=0;i<1000;i++){
                if(!varList.contains(name+i)){
                    varList.add(name+i);
                    return name+i;
                }
            }
        }
        varList.add(name);
        return name;
    }

    public static String getShoreVarName(String name){
        if(name.endsWith("_result")){
            String n = name.replace("_result","");
            return n.substring(n.lastIndexOf("_")+1,n.length());
        }else {
            return name.substring(name.lastIndexOf("_")+1,name.length());
        }
    }

    public static void setDescription(String description){
        Cucumber.description = description;
    }

    public static void addVarName(String name){
        varNames.add(name);
    }


   public String buildDescription() {
       if (description.contains("{")) {
           String[] varListNames = StringUtils.substringsBetween(description, "{", "}");
           for (String var : varListNames) {
               for (String varname : varNames) {
                   if (varname.contains(var)) {
                       Cucumber.description = description.replace("{" + var + "}", "{" + varname + "}");
                   }
               }
           }
       }
       varNames = new ArrayList<>();
       return description;
   }

}
