package org.softauto.serializer;

import java.util.ArrayList;
import java.util.List;

public class TypeMapper {

    private List<IObject> oList = new ArrayList<>();


    public void register(IObject type){
        oList.add(type);
    }

    public String getTypeAsString(Object obj){
       // if(obj != null) {
            for (IObject type : oList) {
                if (type.isApply(obj)) {
                    return type.getTypeAsString();
                }
            }
            return obj.getClass().getTypeName();
        //}
        //return null;
    }


}
