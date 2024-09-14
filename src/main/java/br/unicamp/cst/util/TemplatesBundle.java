package br.unicamp.cst.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TemplatesBundle {

    private static TemplatesBundle singleton = new TemplatesBundle();

    private HashMap<String, String> templateMap = new HashMap();

    private TemplatesBundle(){
        InputStream templates = this.getClass().getClassLoader().getResourceAsStream("templates");
        BufferedReader reader = new BufferedReader(new InputStreamReader(templates));
        String resource;
        try {
            while ((resource = reader.readLine()) != null) {
                templateMap.put(resource, loadTemplate(resource));
            }
        } catch (IOException ex){
            Logger.getLogger(TemplatesBundle.class.getName()).log(Level.SEVERE, "Resource bundle for code templates was not found!");
        }
    }

    public static TemplatesBundle getInstance(){
        return singleton;
    }

    private String loadTemplate(String file){
        InputStream templateStream = this.getClass().getClassLoader().getResourceAsStream("templates/" + file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(templateStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ex){
            Logger.getLogger(TemplatesBundle.class.getName()).log(Level.SEVERE, "Resource " + file + " not found!");
        }
        return builder.toString();
    }

    public String getTemplate(String templateName){
        return new String(templateMap.get(templateName));
    }
}
