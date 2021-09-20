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
package org.softauto.mojo;


import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.avro.Compiler;
import org.apache.maven.plugin.MojoExecutionException;
import org.softauto.core.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * Generate Java classes and interfaces from Avro protocol files (.avpr)
 * base on Avro ProtocolMojo
 * @goal protocol
 * @phase generate-sources
 * @requiresDependencyResolution runtime
 * @threadSafe
 */
public class ProtocolMojo extends AbstractJdryMojo {

    public ProtocolMojo() {
    }

    /**
     * A set of Ant-like inclusion patterns used to select files from the source
     * directory for processing. By default, the pattern <code>**&#47;*.avpr</code>
     * is used to select grammar files.
     *
     * @parameter
     */
    protected String[] includes = new String[] { "**/*.avpr" };

    /**
     * A set of Ant-like inclusion patterns used to select files from the source
     * directory for processing. By default, the pattern <code>**&#47;*.avpr</code>
     * is used to select grammar files.
     *
     * @parameter
     */
    private String[] testIncludes = new String[] { "**/*.avpr" };

    @Override
    protected void compileFiles(String[] files, File sourceDir, File outDir) throws MojoExecutionException {
        for (String filename : files) {
            try {
                // Need to register custom logical type factories before schema compilation.
                loadLogicalTypesFactories();
                doCompile(filename, sourceDir, outDir);
            } catch (IOException e) {
                throw new MojoExecutionException("Error compiling protocol file " + filename + " to " + outDir, e);
            }
        }
    }

    protected void doCompile(String filename, File sourceDirectory, File outputDirectory) throws IOException {
        File src = new File(sourceDirectory, filename);
        Protocol protocol = Protocol.parse(src);
        Compiler compiler = new Compiler(protocol);
        for(String jar : classpath){
                Utils.addJarToClasspath(jar);
        }

        compiler.setTemplateDir(this.templateDirectory);
        compiler.setStringType(GenericData.StringType.valueOf(this.stringType));
        compiler.setFieldVisibility(this.getFieldVisibility());
        compiler.setCreateOptionalGetters(this.createOptionalGetters);
        compiler.setGettersReturnOptional(this.gettersReturnOptional);
        compiler.setOptionalGettersForNullableFieldsOnly(this.optionalGettersForNullableFieldsOnly);
        compiler.setCreateSetters(this.createSetters);
        compiler.setAdditionalVelocityTools(this.instantiateAdditionalVelocityTools());
        compiler.setEnableDecimalLogicalType(this.enableDecimalLogicalType);
        compiler.setVelocityResources(velocityResources);

        try {
            URLClassLoader classLoader = this.createClassLoader();
            String[] var8 = this.customConversions;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String customConversion = var8[var10];
                compiler.addCustomConversion(classLoader.loadClass(customConversion));
            }
        } catch (ClassNotFoundException | DependencyResolutionRequiredException var12) {
            throw new IOException(var12);
        }
            compiler.setOutputCharacterEncoding(project.getProperties().getProperty("project.build.sourceEncoding"));
            compiler.compileProtocolToDestination(src, outputDirectory, template, outputName == null ? protocol.getName():outputName);
    }

    @Override
    protected String[] getIncludes() {
        return includes;
    }

    @Override
    protected String[] getTestIncludes() {
        return testIncludes;
    }

}
