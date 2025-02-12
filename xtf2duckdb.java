///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavenCentral,ehi=https://jars.interlis.ch/
//DEPS ch.interlis:ili2duckdb:5.2.2-SNAPSHOT

import static java.lang.System.*;

import ch.ehi.ili2db.gui.Config;

public class xtf2duckdb {

    public static void main(String... args) {
        out.println("Hello World");

        Config settings = createConfig();
        settings.setFunction(Config.FC_IMPORT);
    }



    private static Config createConfig() {
        Config settings = new Config();
        new ch.ehi.ili2duckdb.DuckDBMain().initConfig(settings);
        
        settings.setStrokeArcs(settings.STROKE_ARCS_ENABLE);
        settings.setDoImplicitSchemaImport(true);
        settings.setNameOptimization(Config.NAME_OPTIMIZATION_TOPIC);
        settings.setDefaultSrsCode("2056");
        
        return settings;
    }    
}


