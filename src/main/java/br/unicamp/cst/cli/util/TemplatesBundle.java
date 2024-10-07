package br.unicamp.cst.cli.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TemplatesBundle {

    private static final TemplatesBundle singleton = new TemplatesBundle();

    private final HashMap<String, String> templateMap = new HashMap<>();

    private TemplatesBundle(){
        // getResourceAsStream needs to receive a constant so GraalVM can process it
        loadTemplate("CodeletTemplate", TemplatesBundle.class.getResourceAsStream("/templates/CodeletTemplate"));
        loadTemplate("AgentMindTemplate", TemplatesBundle.class.getResourceAsStream("/templates/AgentMindTemplate"));
        loadTemplate("settings", TemplatesBundle.class.getResourceAsStream("/templates/settings.gradle"));
        loadTemplate("build", TemplatesBundle.class.getResourceAsStream("/templates/build.gradle"));
        loadTemplate("MainTemplate", TemplatesBundle.class.getResourceAsStream("/templates/MainTemplate"));
    }

    public static TemplatesBundle getInstance(){
        return singleton;
    }

    private void loadTemplate(String file, InputStream templateStream){
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
        templateMap.put(file, builder.toString());
    }

    public String getTemplate(String templateName){
        return new String(templateMap.get(templateName));
    }
}
