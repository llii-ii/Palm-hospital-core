package org.mybatis.generator.api;

import org.mybatis.generator.internal.util.messages.*;
import java.sql.*;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.internal.util.*;
import java.util.*;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.*;
import org.mybatis.generator.internal.*;
import java.io.*;

public class MyBatisGenerator
{
    private Configuration configuration;
    private ShellCallback shellCallback;
    private List<GeneratedJavaFile> generatedJavaFiles;
    private List<GeneratedXmlFile> generatedXmlFiles;
    private List<String> warnings;
    private Set<String> projects;
    
    public MyBatisGenerator(final Configuration configuration, final ShellCallback shellCallback, final List<String> warnings) throws InvalidConfigurationException {
        if (configuration == null) {
            throw new IllegalArgumentException(Messages.getString("RuntimeError.2"));
        }
        this.configuration = configuration;
        if (shellCallback == null) {
            this.shellCallback = new DefaultShellCallback(false);
        }
        else {
            this.shellCallback = shellCallback;
        }
        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        }
        else {
            this.warnings = warnings;
        }
        this.generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
        this.generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
        this.projects = new HashSet<String>();
        this.configuration.validate();
    }
    
    public void generate(final ProgressCallback callback) throws SQLException, IOException, InterruptedException {
        this.generate(callback, null, null, true);
    }
    
    public void generate(final ProgressCallback callback, final Set<String> contextIds) throws SQLException, IOException, InterruptedException {
        this.generate(callback, contextIds, null, true);
    }
    
    public void generate(final ProgressCallback callback, final Set<String> contextIds, final Set<String> fullyQualifiedTableNames) throws SQLException, IOException, InterruptedException {
        this.generate(callback, contextIds, fullyQualifiedTableNames, true);
    }
    
    public void generate(ProgressCallback callback, final Set<String> contextIds, final Set<String> fullyQualifiedTableNames, final boolean writeFiles) throws SQLException, IOException, InterruptedException {
        if (callback == null) {
            callback = new NullProgressCallback();
        }
        this.generatedJavaFiles.clear();
        this.generatedXmlFiles.clear();
        ObjectFactory.reset();
        RootClassInfo.reset();
        List<Context> contextsToRun;
        if (contextIds == null || contextIds.size() == 0) {
            contextsToRun = this.configuration.getContexts();
        }
        else {
            contextsToRun = new ArrayList<Context>();
            for (final Context context : this.configuration.getContexts()) {
                if (contextIds.contains(context.getId())) {
                    contextsToRun.add(context);
                }
            }
        }
        if (this.configuration.getClassPathEntries().size() > 0) {
            final ClassLoader classLoader = ClassloaderUtility.getCustomClassloader(this.configuration.getClassPathEntries());
            ObjectFactory.addExternalClassLoader(classLoader);
        }
        int totalSteps = 0;
        for (final Context context2 : contextsToRun) {
            totalSteps += context2.getIntrospectionSteps();
        }
        callback.introspectionStarted(totalSteps);
        for (final Context context2 : contextsToRun) {
            context2.introspectTables(callback, this.warnings, fullyQualifiedTableNames);
        }
        totalSteps = 0;
        for (final Context context2 : contextsToRun) {
            totalSteps += context2.getGenerationSteps();
        }
        callback.generationStarted(totalSteps);
        for (final Context context2 : contextsToRun) {
            context2.generateFiles(callback, this.generatedJavaFiles, this.generatedXmlFiles, this.warnings);
        }
        if (writeFiles) {
            callback.saveStarted(this.generatedXmlFiles.size() + this.generatedJavaFiles.size());
            for (final GeneratedXmlFile gxf : this.generatedXmlFiles) {
                this.projects.add(gxf.getTargetProject());
                this.writeGeneratedXmlFile(gxf, callback);
            }
            for (final GeneratedJavaFile gjf : this.generatedJavaFiles) {
                this.projects.add(gjf.getTargetProject());
                this.writeGeneratedJavaFile(gjf, callback);
            }
            for (final String project : this.projects) {
                this.shellCallback.refreshProject(project);
            }
        }
        callback.done();
    }
    
    private void writeGeneratedJavaFile(final GeneratedJavaFile gjf, final ProgressCallback callback) throws InterruptedException, IOException {
        try {
            final File directory = this.shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
            File targetFile = new File(directory, gjf.getFileName());
            String source;
            if (targetFile.exists()) {
                if (this.shellCallback.isMergeSupported()) {
                    source = this.shellCallback.mergeJavaFile(gjf.getFormattedContent(), targetFile, MergeConstants.OLD_ELEMENT_TAGS, gjf.getFileEncoding());
                }
                else if (this.shellCallback.isOverwriteEnabled()) {
                    source = gjf.getFormattedContent();
                    this.warnings.add(Messages.getString("Warning.11", targetFile.getAbsolutePath()));
                }
                else {
                    source = gjf.getFormattedContent();
                    targetFile = this.getUniqueFileName(directory, gjf.getFileName());
                    this.warnings.add(Messages.getString("Warning.2", targetFile.getAbsolutePath()));
                }
            }
            else {
                source = gjf.getFormattedContent();
            }
            callback.checkCancel();
            callback.startTask(Messages.getString("Progress.15", targetFile.getName()));
            this.writeFile(targetFile, source, gjf.getFileEncoding());
        }
        catch (ShellException e) {
            this.warnings.add(e.getMessage());
        }
    }
    
    private void writeGeneratedXmlFile(final GeneratedXmlFile gxf, final ProgressCallback callback) throws InterruptedException, IOException {
        try {
            final File directory = this.shellCallback.getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
            File targetFile = new File(directory, gxf.getFileName());
            String source;
            if (targetFile.exists()) {
                if (gxf.isMergeable()) {
                    source = XmlFileMergerJaxp.getMergedSource(gxf, targetFile);
                }
                else if (this.shellCallback.isOverwriteEnabled()) {
                    source = gxf.getFormattedContent();
                    this.warnings.add(Messages.getString("Warning.11", targetFile.getAbsolutePath()));
                }
                else {
                    source = gxf.getFormattedContent();
                    targetFile = this.getUniqueFileName(directory, gxf.getFileName());
                    this.warnings.add(Messages.getString("Warning.2", targetFile.getAbsolutePath()));
                }
            }
            else {
                source = gxf.getFormattedContent();
            }
            callback.checkCancel();
            callback.startTask(Messages.getString("Progress.15", targetFile.getName()));
            this.writeFile(targetFile, source, "UTF-8");
        }
        catch (ShellException e) {
            this.warnings.add(e.getMessage());
        }
    }
    
    private void writeFile(final File file, final String content, final String fileEncoding) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw;
        if (fileEncoding == null) {
            osw = new OutputStreamWriter(fos);
        }
        else {
            osw = new OutputStreamWriter(fos, fileEncoding);
        }
        final BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }
    
    private File getUniqueFileName(final File directory, final String fileName) {
        File answer = null;
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; ++i) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);
            final File testFile = new File(directory, sb.toString());
            if (!testFile.exists()) {
                answer = testFile;
                break;
            }
        }
        if (answer == null) {
            throw new RuntimeException(Messages.getString("RuntimeError.3", directory.getAbsolutePath()));
        }
        return answer;
    }
    
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return this.generatedJavaFiles;
    }
    
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return this.generatedXmlFiles;
    }
}
