package org.softauto.serializer;

public class java_lang_null implements IObject{
    @Override
    public String getTypeAsString() {
        return "null";
    }

    @Override
    public boolean isApply(Object obj) {
        if(obj == null){
            return true;
        }
        return false;
    }


}
