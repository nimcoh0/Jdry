package org.softauto.serializer;

public class org_glassfish_jersey_server_containerResponse implements IObject{

    Object obj;


    @Override
    public String getTypeAsString() {
        return "java.lang.String";

    }

    @Override
    public boolean isApply(Object obj) {
        if(obj instanceof org.glassfish.jersey.server.ContainerResponse){
            this.obj = obj;
            return true;
        }
        return false;
    }


}
