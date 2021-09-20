/*
        Licensed to the Apache Software Foundation (ASF) under one or more
        contributor license agreements.  See the NOTICE file distributed with
        this work for additional information regarding copyright ownership.
        The ASF licenses this file to You under the Apache License, Version 2.0
        (the "License"); you may not use this file except in compliance with
        the License.  You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
/*
        this class base on avro plugin ,and wa modify by jdry project
*/

package org.softauto.velocity;


import org.apache.avro.LogicalTypes;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.apache.avro.Compiler;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.lang.model.element.Modifier.STATIC;


/**
 * jdry Compiler Mojos.
 * base on AbstractAvroMojo .
 * this mojo extend avro mojo by adding three more options
 * template : specify velocity vm file
 * schema : the schema/protocol file to be use
 * outputName : specify out put class name
 */
public abstract class AbstractJdryMojo extends AbstractMojo {


    /**
     * @parameter property="outputDirectory"
     *            default-value="${project.build.directory}/generated-sources/avro"
     */
    private File outputDirectory;


    /**
     * @parameter property="template"
     *            default-value=""
     */
    public String template;


    /**
     * @parameter property="outputName"
     *            default-value=""
     */
    public String outputName;



    /**
     * The field visibility indicator for the fields of the generated class, as
     * string values of SpecificCompiler.FieldVisibility. The text is case
     * insensitive.
     *
     * @parameter default-value="PRIVATE"
     */
    private String fieldVisibility;


    /**
     * A set of Ant-like exclusion patterns used to prevent certain files from being
     * processed. By default, this set is empty such that no files are excluded.
     *
     * @parameter
     */
    protected String[] excludes = new String[0];


    /**
     * The Java type to use for Avro strings. May be one of CharSequence, String or
     * Utf8. CharSequence by default.
     *
     * @parameter property="stringType"
     */
    protected String stringType = "CharSequence";

    /**
     * The directory (within the java classpath) that contains the velocity
     * templates to use for code generation. The default value points to the
     * templates included with the avro-maven-plugin.
     *
     * @parameter property="templateDirectory"
     */
    protected String templateDirectory = "";

    /**
     * The qualified names of classes which the plugin will look up, instantiate
     * (through an empty constructor that must exist) and set up to be injected into
     * Velocity templates by Avro compiler.
     *
     * @parameter property="velocityToolsClassesNames"
     */
    protected String[] velocityToolsClassesNames = new String[0];

    /**
     * list of velocity resources . can be jar , file
     * @parameter property="velocityResources"
     */
    protected String velocityResources = "";

    /**
     * The createOptionalGetters parameter enables generating the getOptional...
     * methods that return an Optional of the requested type. This works ONLY on
     * Java 8+
     *
     * @parameter property="createOptionalGetters"
     */
    protected boolean createOptionalGetters = false;

    /**
     * The gettersReturnOptional parameter enables generating get... methods that
     * return an Optional of the requested type. This will replace the This works
     * ONLY on Java 8+
     *
     * @parameter property="gettersReturnOptional"
     */
    protected boolean gettersReturnOptional = false;

    /**
     * The optionalGettersForNullableFieldsOnly parameter works in conjunction with
     * gettersReturnOptional option. If it is set, Optional getters will be
     * generated only for fields that are nullable. If the field is mandatory,
     * regular getter will be generated. This works ONLY on Java 8+.
     *
     * @parameter property="optionalGettersForNullableFieldsOnly"
     */
    protected boolean optionalGettersForNullableFieldsOnly = false;

    /**
     * Determines whether or not to create setters for the fields of the record. The
     * default is to create setters.
     *
     * @parameter default-value="true"
     */
    protected boolean createSetters;

    /**
     * A set of fully qualified class names of custom
     * {@link org.apache.avro.Conversion} implementations to add to the compiler.
     * The classes must be on the classpath at compile time and whenever the Java
     * objects are serialized.
     *
     * @parameter property="customConversions"
     */
    protected String[] customConversions = new String[0];

    /**
     * A set of fully qualified class names of custom
     * {@link LogicalTypes.LogicalTypeFactory} implementations to
     * add to the compiler. The classes must be on the classpath at compile time and
     * whenever the Java objects are serialized.
     *
     * @parameter property="customLogicalTypeFactories"
     */
    protected String[] customLogicalTypeFactories = new String[0];

    /**
     * Determines whether or not to use Java classes for decimal types
     *
     * @parameter default-value="false"
     */
    protected boolean enableDecimalLogicalType;

    /**
     * A set of fully qualified Jar names
     *
     * @parameter property="classpath"
     */
    protected String[] classpath = new String[0];

    /**
     * A set of namespace
     *
     * @parameter property="namespace"
     */
    protected String namespace = "";

    /**
     * The current Maven project.
     *
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;






    @Override
    public void execute() throws MojoExecutionException {
        compileFiles(outputDirectory);
    }



    protected abstract void compileFiles( File outDir) throws MojoExecutionException ;

    protected void loadLogicalTypesFactories() throws IOException, MojoExecutionException {
        try (URLClassLoader classLoader = createClassLoader()) {
            for (String factory : customLogicalTypeFactories) {
                Class<LogicalTypes.LogicalTypeFactory> logicalTypeFactoryClass = (Class<LogicalTypes.LogicalTypeFactory>) classLoader
                        .loadClass(factory);
                LogicalTypes.LogicalTypeFactory factoryInstance = logicalTypeFactoryClass.getDeclaredConstructor()
                        .newInstance();
                LogicalTypes.register(factoryInstance.getTypeName(), factoryInstance);
            }
        } catch (DependencyResolutionRequiredException | ClassNotFoundException e) {
            throw new IOException(e);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new MojoExecutionException("Failed to instantiate logical type factory class", e);
        }
    }

    protected Compiler.FieldVisibility getFieldVisibility() {
        try {
            String upper = String.valueOf(this.fieldVisibility).trim().toUpperCase();
            return Compiler.FieldVisibility.valueOf(upper);
        } catch (IllegalArgumentException e) {
            return Compiler.FieldVisibility.PRIVATE;
        }
    }

    protected List<Object> instantiateAdditionalVelocityTools() {
        final List<Object> velocityTools = new ArrayList<>(velocityToolsClassesNames.length);
        for (String velocityToolClassName : velocityToolsClassesNames) {
            try {
                Class klass = Class.forName(velocityToolClassName);
                Integer modifier = klass.getModifiers();
                if(modifier.equals(STATIC)){
                    velocityTools.add(klass);
                }else{
                    velocityTools.add(klass.newInstance());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return velocityTools;
    }

    protected abstract void doCompile(File outputDirectory) throws IOException;

    protected URLClassLoader createClassLoader() throws DependencyResolutionRequiredException, MalformedURLException {
        final List<URL> urls = appendElements(project.getRuntimeClasspathElements());
        urls.addAll(appendElements(project.getTestClasspathElements()));
        return new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
    }

    private List<URL> appendElements(List runtimeClasspathElements) throws MalformedURLException {
        if (runtimeClasspathElements == null) {
            return new ArrayList<>();
        }
        List<URL> runtimeUrls = new ArrayList<>(runtimeClasspathElements.size());
        for (Object runtimeClasspathElement : runtimeClasspathElements) {
            String element = (String) runtimeClasspathElement;
            runtimeUrls.add(new File(element).toURI().toURL());
        }
        return runtimeUrls;
    }

}
