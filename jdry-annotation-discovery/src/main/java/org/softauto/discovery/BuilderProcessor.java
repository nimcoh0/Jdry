package org.softauto.discovery;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;
import org.softauto.core.Utils;
import org.softauto.discovery.schema.SchemaHeaderHandler;
import org.softauto.discovery.schema.SchemaMessageHandler;
import org.softauto.discovery.schema.SchemaTypesHandler;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * annotation processing for schema building
 */
@SupportedAnnotationTypes({"org.softauto.annotations.ExposedForTesting","org.softauto.annotations.ListenerForTesting"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(BuilderProcessor.class);

    private static String SCHEMA_VERSION = "1.0";
    private static String SCHEMA_NAMESPACE = "tests.infrastructure";
    static private String STEP_PROTOCOL_NAME = "StepService";
    static private String LISTENER_PROTOCOL_NAME = "ListenerService";


    List<Provider> providers = null;
    ObjectMapper objectMapper = new ObjectMapper();
    List<AbstractSchema> schemaBuilders;
    JsonNode nodes;


    public BuilderProcessor(){
        schemaBuilders = new ArrayList<>();
        schemaBuilders.add(new SchemaHeaderHandler());
        schemaBuilders.add(new SchemaTypesHandler());
        schemaBuilders.add(new SchemaMessageHandler());
     }

     private void setOptions(){
        if(processingEnv.getOptions().get("stepProtocolName") != null){
            STEP_PROTOCOL_NAME = processingEnv.getOptions().get("stepProtocolName");
        }
        if(processingEnv.getOptions().get("listenerProtocolName") != null){
             LISTENER_PROTOCOL_NAME = processingEnv.getOptions().get("listenerProtocolName");
        }
        if(processingEnv.getOptions().get("schemaVersion") != null){
            SCHEMA_VERSION = processingEnv.getOptions().get("schemaVersion");
        }

     }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            setOptions();
            for (TypeElement annotation : annotations) {
                logger.debug("found annotation "+ annotation);
                Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
                Map<Boolean, List<Element>> annotatedMethods = annotatedElements.stream().collect(Collectors.partitioningBy(element -> (true)));
                providers = ProviderManager.getProviders();
                annotatedMethods.entrySet().forEach(k-> {
                    nodes = objectMapper.createObjectNode();
                    for (Element element : k.getValue()) {
                         for (Provider provider : providers) {
                             JsonNode node = provider.parser(element);
                             if(node != null) {
                                 List<Provider> extProviders = ProviderManager.getExtendedProviders();
                                 for (Provider p : extProviders) {
                                     ((ObjectNode) node).setAll((ObjectNode) p.parser(element));
                                 }
                             }
                             Visitor v = new SchemaVisitor(getProtocolName(annotation),node,nodes);
                             this.accept(v);
                             logger.debug("new element for provider "+ provider.getClass().getName());
                        }
                     }
                });
                String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes);
                FileObject builderFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, SCHEMA_NAMESPACE, getProtocolName(annotation) + ".avpr", null);
                String filePath = builderFile.toUri().getPath();
                Utils.save(schema, filePath);
                logger.debug("new schema  created at "+ filePath);
            }


        }catch (Exception e){
            logger.warn("annotation process fail ",e.getMessage());
        }
        return false;
    }

    public void accept(Visitor v) {
        try {
            for(AbstractSchema builder : schemaBuilders){
                builder.accept(v);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }





    private String getProtocolName(TypeElement annotation){
        if(annotation.getSimpleName().toString().equals("ListenerForTesting")){
            return LISTENER_PROTOCOL_NAME;
        }
        if(annotation.getSimpleName().toString().equals("ExposedForTesting")){
            return STEP_PROTOCOL_NAME;
        }
        return null;
    }
}
