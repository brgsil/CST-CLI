package br.unicamp.cst.cli.data;

import java.io.File;

public class ConfigParser {

    public static AgentConfig parseProjectToConfig(){
        // Get root dir. Traverse up if necessary, until find folder src/
        File currDir = new File(System.getProperty("user.dir"));

        File srcFolder = new File(currDir.getAbsolutePath() + "/src");
        while (!srcFolder.exists() && currDir.getParentFile().exists()){
            currDir = currDir.getParentFile();
            srcFolder = new File(currDir.getAbsolutePath() + "/src");
        }

        if (!currDir.exists() ) return new AgentConfig();

        //Traverse folders and get packageName
        //Find AgentMind file
        srcFolder = new File(srcFolder, "main");
        if (!srcFolder.exists())
            return new AgentConfig();

        File agentMindFile = new File(srcFolder, "AgentMind.java");
        while (!agentMindFile.exists() && srcFolder.listFiles().length > 0){
            srcFolder = srcFolder.listFiles()[0];
            agentMindFile = new File(srcFolder, "AgentMind.java");
        }

        if (!agentMindFile.exists())
            return new AgentConfig();

        System.out.println(agentMindFile.getAbsolutePath());
        //Parse file to AgentConfig
            //Search all memories declarations


        //For each codelet configuration
            // Create a CodeletConfig
                

        return new AgentConfig();
    }
}
