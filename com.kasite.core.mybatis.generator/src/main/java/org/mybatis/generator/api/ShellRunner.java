package org.mybatis.generator.api;

import org.mybatis.generator.internal.util.messages.*;
import org.mybatis.generator.config.xml.*;
import org.mybatis.generator.internal.*;
import java.sql.*;
import java.io.*;
import org.mybatis.generator.exception.*;
import org.mybatis.generator.config.*;
import java.util.*;
import org.mybatis.generator.logging.*;

public class ShellRunner
{
    private static final String CONFIG_FILE = "-configfile";
    private static final String OVERWRITE = "-overwrite";
    private static final String CONTEXT_IDS = "-contextids";
    private static final String TABLES = "-tables";
    private static final String VERBOSE = "-verbose";
    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging";
    private static final String HELP_1 = "-?";
    private static final String HELP_2 = "-h";
    
    public static void main(final String[] args) {
        if (args.length == 0) {
            usage();
            System.exit(0);
            return;
        }
        final Map<String, String> arguments = parseCommandLine(args);
        if (arguments.containsKey("-?")) {
            usage();
            System.exit(0);
            return;
        }
        if (!arguments.containsKey("-configfile")) {
            writeLine(Messages.getString("RuntimeError.0"));
            return;
        }
        final List<String> warnings = new ArrayList<String>();
        final String configfile = arguments.get("-configfile");
        final File configurationFile = new File(configfile);
        if (!configurationFile.exists()) {
            writeLine(Messages.getString("RuntimeError.1", configfile));
            return;
        }
        final Set<String> fullyqualifiedTables = new HashSet<String>();
        if (arguments.containsKey("-tables")) {
            final StringTokenizer st = new StringTokenizer(arguments.get("-tables"), ",");
            while (st.hasMoreTokens()) {
                final String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }
        final Set<String> contexts = new HashSet<String>();
        if (arguments.containsKey("-contextids")) {
            final StringTokenizer st2 = new StringTokenizer(arguments.get("-contextids"), ",");
            while (st2.hasMoreTokens()) {
                final String s2 = st2.nextToken().trim();
                if (s2.length() > 0) {
                    contexts.add(s2);
                }
            }
        }
        try {
            final ConfigurationParser cp = new ConfigurationParser(warnings);
            final Configuration config = cp.parseConfiguration(configurationFile);
            final DefaultShellCallback shellCallback = new DefaultShellCallback(arguments.containsKey("-overwrite"));
            final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            final ProgressCallback progressCallback = arguments.containsKey("-verbose") ? new VerboseProgressCallback() : null;
            myBatisGenerator.generate(progressCallback, contexts, fullyqualifiedTables);
        }
        catch (XMLParserException e) {
            writeLine(Messages.getString("Progress.3"));
            writeLine();
            for (final String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        }
        catch (SQLException e2) {
            e2.printStackTrace();
            return;
        }
        catch (IOException e3) {
            e3.printStackTrace();
            return;
        }
        catch (InvalidConfigurationException e4) {
            writeLine(Messages.getString("Progress.16"));
            for (final String error : e4.getErrors()) {
                writeLine(error);
            }
            return;
        }
        catch (InterruptedException ex) {}
        for (final String warning : warnings) {
            writeLine(warning);
        }
        if (warnings.size() == 0) {
            writeLine(Messages.getString("Progress.4"));
        }
        else {
            writeLine();
            writeLine(Messages.getString("Progress.5"));
        }
    }
    
    private static void usage() {
        final String lines = Messages.getString("Usage.Lines");
        for (int intLines = Integer.parseInt(lines), i = 0; i < intLines; ++i) {
            final String key = "Usage." + i;
            writeLine(Messages.getString(key));
        }
    }
    
    private static void writeLine(final String message) {
        System.out.println(message);
    }
    
    private static void writeLine() {
        System.out.println();
    }
    
    private static Map<String, String> parseCommandLine(final String[] args) {
        final List<String> errors = new ArrayList<String>();
        final Map<String, String> arguments = new HashMap<String, String>();
        for (int i = 0; i < args.length; ++i) {
            if ("-configfile".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) {
                    arguments.put("-configfile", args[i + 1]);
                }
                else {
                    errors.add(Messages.getString("RuntimeError.19", "-configfile"));
                }
                ++i;
            }
            else if ("-overwrite".equalsIgnoreCase(args[i])) {
                arguments.put("-overwrite", "Y");
            }
            else if ("-verbose".equalsIgnoreCase(args[i])) {
                arguments.put("-verbose", "Y");
            }
            else if ("-?".equalsIgnoreCase(args[i])) {
                arguments.put("-?", "Y");
            }
            else if ("-h".equalsIgnoreCase(args[i])) {
                arguments.put("-?", "Y");
            }
            else if ("-forceJavaLogging".equalsIgnoreCase(args[i])) {
                LogFactory.forceJavaLogging();
            }
            else if ("-contextids".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) {
                    arguments.put("-contextids", args[i + 1]);
                }
                else {
                    errors.add(Messages.getString("RuntimeError.19", "-contextids"));
                }
                ++i;
            }
            else if ("-tables".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) {
                    arguments.put("-tables", args[i + 1]);
                }
                else {
                    errors.add(Messages.getString("RuntimeError.19", "-tables"));
                }
                ++i;
            }
            else {
                errors.add(Messages.getString("RuntimeError.20", args[i]));
            }
        }
        if (!errors.isEmpty()) {
            for (final String error : errors) {
                writeLine(error);
            }
            System.exit(-1);
        }
        return arguments;
    }
}
