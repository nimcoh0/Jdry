package org.softauto.grpc;

import java.util.HashMap;

public class CallOptions implements org.softauto.core.CallOptions {

    HashMap<String, Object> options = null;

    public CallOptions(){
        options =  new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getOptions() {
        return null;
    }

    public void addOption(String key,String value){
        options.put(key,value);
    }
}
