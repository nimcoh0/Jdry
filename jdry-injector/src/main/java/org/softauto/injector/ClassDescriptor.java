package org.softauto.injector;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apache.avro.Protocol;
import org.softauto.core.ClassType;

import javax.annotation.CheckReturnValue;

/**
 * hold the class details . this "class" is the class that contain the require method to be invoke
 */
public class ClassDescriptor {
    private ClassType type;
    private String fullClassName;
    private Class[] types;
    private Object[] args;
    static ObjectMapper mapper = new ObjectMapper();


    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ClassDescriptor.class);

    private ClassDescriptor(ClassType classType, String fullClassName ) {
        type = (ClassType) Preconditions.checkNotNull(classType, "type");
        this.fullClassName = (String)Preconditions.checkNotNull(fullClassName, "fullClassName");
        logger.debug("create class descriptor for "+fullClassName);
    }



    private ClassDescriptor(ClassType type, String fullClassName, Class[] types , Object[] args) {
        this.type = (ClassType) Preconditions.checkNotNull(type, "type");
        this.fullClassName = (String)Preconditions.checkNotNull(fullClassName, "fullClassName");
        this.types = types;
        this.args = args;

        logger.debug("create class descriptor for "+fullClassName);
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


    public ClassType getType() {
        return this.type;
    }

    public Class[] getTypes() {
        return this.types;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public String getFullClassName() {
        return this.fullClassName;
    }




    public static  Builder newBuilder() {
        return new Builder();
    }



    public String toString() {
        return MoreObjects.toStringHelper(this).add("fullClassName", this.fullClassName).add("type", this.type).toString();
    }



    public static final class Builder {
        private ClassType type;
        private String fullClassName;
        private Class[] types;
        private Object[] args;
        private Protocol.Message msg;

        private Builder() {
        }


        public Builder setMsg(Protocol.Message msg) {
            this.msg = msg;
            return this;
        }

        public Builder setType(ClassType type) {
            this.type = type;
            return this;
        }

        public Builder setTypes(Class[] types) {
            this.types = types;
            return this;
        }

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder setFullClassName(String fullClassName) {
            this.fullClassName = fullClassName;
            return this;
        }

        @CheckReturnValue
        public ClassDescriptor build() {
           return new ClassDescriptor(this.type, this.fullClassName);
        }

        @CheckReturnValue
        public ClassDescriptor build0() {
            return new ClassDescriptor(this.type, this.fullClassName,this.types,this.args);
        }


    }


}
