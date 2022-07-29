package org.softauto.serializer;

//import org.softauto.core.jackson.serializer.WsRsResponsSerializer;


public class javax_ws_rs_core_response implements IObject{

    Object obj;



    public javax_ws_rs_core_response(){

    }

    @Override
    public String getTypeAsString() {
        return ((javax.ws.rs.core.Response)obj).getEntity().getClass().getTypeName();

    }

    @Override
    public boolean isApply(Object obj) {
        if(obj instanceof javax.ws.rs.core.Response){
            this.obj = obj;
            return true;
        }
        return false;
    }


}
