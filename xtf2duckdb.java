///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavenCentral,ehi=https://jars.interlis.ch/
//DEPS ch.interlis:ili2duckdb:5.2.2,org.duckdb:duckdb_jdbc:1.1.3

import static java.lang.System.*;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.ehi.ili2db.base.Ili2db;
import ch.ehi.ili2db.base.Ili2dbException;
import ch.ehi.ili2db.gui.Config;

public class xtf2duckdb {

    public static void main(String... args) {
        out.println("Hello World");

        List<String> ids = new ArrayList<>();
        try {
            // Load XML from URL
            URL url = new URL("https://data.geo.so.ch/ilidata.xml");
            InputStream inputStream = url.openStream();

            // Parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            // Get all DatasetIdx16.File elements
            NodeList nodeList = doc.getElementsByTagName("id");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    //System.out.println(element.getTextContent().trim());

                    if (element.getTextContent().trim().contains("abbaustelle")) 
                        ids.add(element.getTextContent().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Config settings = createConfig();

        for (var id : ids) {
            settings.setXtffile("ilidata:"+id);

            String dbFileName = "/Users/stefan/tmp/duckdb/" + id + ".duckdb";
            settings.setDbfile(dbFileName);
            settings.setDburl("jdbc:duckdb:" + settings.getDbfile());
            
            try {
                Ili2db.run(settings, null);
            } catch (Ili2dbException e) {
                e.printStackTrace();
            }
        }





    }

    private static Config createConfig() {
        Config settings = new Config();
        new ch.ehi.ili2duckdb.DuckDBMain().initConfig(settings);
        
        settings.setFunction(Config.FC_IMPORT);
        settings.setStrokeArcs(settings.STROKE_ARCS_ENABLE);
        settings.setDoImplicitSchemaImport(true);
        settings.setNameOptimization(Config.NAME_OPTIMIZATION_TOPIC);
        settings.setDefaultSrsCode("2056");
        settings.setValue(Config.CREATE_GEOM_INDEX, Config.TRUE);
        settings.setJsonTrafo(settings.JSON_TRAFO_COALESCE);
        settings.setCreateMetaInfo(true);
        settings.setModeldir("https://geo.so.ch/models;https://data.geo.so.ch");

        
        return settings;
    }    
}


