package org.softauto.tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiceHelper {

    static List<String> guiceBindList = new ArrayList<>();
    static List<String> varList = new ArrayList<>();

   public static boolean isBind(String clazz){
       if(guiceBindList.contains(clazz)){
           return true;
       }
       return false;
   }

   public static void addBind(String clazz){
       guiceBindList.add(clazz);
   }

   public static boolean isVarExist(String var){
       if(varList.contains(var)){
           return true;
       }
       return false;
   }

   public static String getVarName(String var){
       for(int i=0;i<100;i++){
           if(varList.contains(var)){
               var = var+i;
           }else {
               varList.add(var);
               return var;
           }
       }
       return var;
   }

}
