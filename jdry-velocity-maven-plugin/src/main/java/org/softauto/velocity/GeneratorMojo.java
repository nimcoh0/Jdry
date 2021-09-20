package org.softauto.velocity;

import org.apache.avro.Compiler;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.softauto.core.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;


/**
 * Generate Java classes and interfaces from velocity Template (.vm)
 * base on velocity GeneratorMojo
 * @goal generator
 * @phase generate-sources
 * @requiresDependencyResolution runtime
 * @threadSafe
 */
public class GeneratorMojo extends AbstractJdryMojo{




    @Override
    protected void compileFiles(File outDir) throws MojoExecutionException {
           try {
                // Need to register custom logical type factories before schema compilation.
                loadLogicalTypesFactories();
                doCompile(outDir);
            } catch (IOException e) {
                throw new MojoExecutionException("Error compiling template file  to " + outDir, e);
            }
    }

    @Override
    protected void doCompile(File outputDirectory) throws IOException {
        Compiler compiler = new Compiler();
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
        compiler.compileToDestination(outputDirectory, template, outputName,namespace);
    }



}
