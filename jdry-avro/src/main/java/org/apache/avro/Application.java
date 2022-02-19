package org.apache.avro;

import java.util.Map;
import java.util.Set;

public class Application extends JsonProperties{

    private String name;
    private String namespace;
    private String doc;

    Application(Set<String> reserved) {
        super(reserved);
    }

    Application(Set<String> reserved, Map<String, ?> propMap) {
        super(reserved, propMap);
    }

    public Process createProcess(String name, String doc, String namespace){
        this.name = name;
        this.doc = doc;
        this.namespace = namespace;
        return new Process(name,doc,namespace);
    }

    public class Process extends Protocol{
        private String name;



        public Process(Protocol p) {
            super(p);
        }

        public Process(String name, String doc, String namespace) {
            super(name, doc, namespace);
        }

        public Process(String name, String namespace) {
            super(name, namespace);
        }
    }
}
