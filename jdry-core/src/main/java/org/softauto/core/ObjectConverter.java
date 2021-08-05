package org.softauto.core;

import org.softauto.core.converters.AbstractConverter;
import org.softauto.core.converters.ConverterFactory;

/**
 * convert string to type
 * support primitive types and json
 */
public class ObjectConverter {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ObjectConverter.class);

    public static Object convert(String value,Class clazz,String type){
        try{
             switch (clazz.getName()){
                case "int"     : return Integer.parseInt(value);
                case "boolean" : return Boolean.valueOf(value);
                case "java.lang.String"  : return value;
                case "byte"    : return Byte.valueOf(value);
                case "long"    : return Long.valueOf(value);
                case "float"   : return Float.valueOf(value);
                case "double"  : return Double.valueOf(value);
                default: return extractConstructorDefaultArgsValue( value,clazz,type);
             }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object extractConstructorDefaultArgsValue(String value,Class clazz,String type){
        try {
            if(type != null) {
                AbstractConverter converter = ConverterFactory.getConverter(type);
                return converter.setType(clazz).setValue(value).build();
            }
        }catch (Exception e){
            logger.error("fail extract Args Types for "+type,e);
        }
        return clazz.cast(value);
    }


}