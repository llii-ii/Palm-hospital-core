package org.mybatis.generator.ant;

import org.apache.tools.ant.types.*;
import org.mybatis.generator.internal.util.*;
import org.mybatis.generator.internal.util.messages.*;
import org.apache.tools.ant.*;
import org.mybatis.generator.config.xml.*;
import org.mybatis.generator.internal.*;
import org.mybatis.generator.api.*;
import java.sql.*;
import java.io.*;
import org.mybatis.generator.exception.*;
import org.mybatis.generator.config.*;
import java.util.*;

public class GeneratorAntTask extends Task
{
    private String configfile;
    private boolean overwrite;
    private PropertySet propertyset;
    private boolean verbose;
    private String contextIds;
    private String fullyQualifiedTableNames;
    
    public void execute() throws BuildException {
        if (!StringUtility.stringHasValue(this.configfile)) {
            throw new BuildException(Messages.getString("RuntimeError.0"));
        }
        final List<String> warnings = new ArrayList<String>();
        final File configurationFile = new File(this.configfile);
        if (!configurationFile.exists()) {
            throw new BuildException(Messages.getString("RuntimeError.1", this.configfile));
        }
        final Set<String> fullyqualifiedTables = new HashSet<String>();
        if (StringUtility.stringHasValue(this.fullyQualifiedTableNames)) {
            final StringTokenizer st = new StringTokenizer(this.fullyQualifiedTableNames, ",");
            while (st.hasMoreTokens()) {
                final String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }
        final Set<String> contexts = new HashSet<String>();
        if (StringUtility.stringHasValue(this.contextIds)) {
            final StringTokenizer st2 = new StringTokenizer(this.contextIds, ",");
            while (st2.hasMoreTokens()) {
                final String s2 = st2.nextToken().trim();
                if (s2.length() > 0) {
                    contexts.add(s2);
                }
            }
        }
        try {
            final Properties p = (this.propertyset == null) ? null : this.propertyset.getProperties();
            final ConfigurationParser cp = new ConfigurationParser(p, warnings);
            final Configuration config = cp.parseConfiguration(configurationFile);
            final DefaultShellCallback callback = new DefaultShellCallback(this.overwrite);
            final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(new AntProgressCallback(this, this.verbose), contexts, fullyqualifiedTables);
        }
        catch (XMLParserException e) {
            for (final String error : e.getErrors()) {
                this.log(error, 0);
            }
            throw new BuildException(e.getMessage());
        }
        catch (SQLException e2) {
            throw new BuildException(e2.getMessage());
        }
        catch (IOException e3) {
            throw new BuildException(e3.getMessage());
        }
        catch (InvalidConfigurationException e4) {
            for (final String error : e4.getErrors()) {
                this.log(error, 0);
            }
            throw new BuildException(e4.getMessage());
        }
        catch (InterruptedException ex) {}
        catch (Exception e5) {
            this.log((Throwable)e5, 0);
            throw new BuildException(e5.getMessage());
        }
        for (final String error2 : warnings) {
            this.log(error2, 1);
        }
    }
    
    public String getConfigfile() {
        return this.configfile;
    }
    
    public void setConfigfile(final String configfile) {
        this.configfile = configfile;
    }
    
    public boolean isOverwrite() {
        return this.overwrite;
    }
    
    public void setOverwrite(final boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    public PropertySet createPropertyset() {
        if (this.propertyset == null) {
            this.propertyset = new PropertySet();
        }
        return this.propertyset;
    }
    
    public boolean isVerbose() {
        return this.verbose;
    }
    
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }
    
    public String getContextIds() {
        return this.contextIds;
    }
    
    public void setContextIds(final String contextIds) {
        this.contextIds = contextIds;
    }
    
    public String getFullyQualifiedTableNames() {
        return this.fullyQualifiedTableNames;
    }
    
    public void setFullyQualifiedTableNames(final String fullyQualifiedTableNames) {
        this.fullyQualifiedTableNames = fullyQualifiedTableNames;
    }
}
