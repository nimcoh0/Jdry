package org.softauto.cucumber;

import java.util.ArrayList;
import java.util.List;

public class Cucumber {

    static List<String> varList = new ArrayList<>();

    public static String getVarName(String name){
       if(varList.contains(name)){
            for(int i=0;i<100;i++){
                if(!varList.contains(name+i)){
                    varList.add(name+i);
                    return name+i;
                }
            }
        }
        varList.add(name);
        return name;
    }


    public static boolean isSupportedType(String type){
        if(CucumberTypesParameter.fromString(type) != null){
            return true;
        }
        return false;
    }


}