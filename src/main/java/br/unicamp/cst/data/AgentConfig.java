package br.unicamp.cst.data;

import br.unicamp.cst.commands.CSTInit;
import br.unicamp.cst.util.TemplatesBundle;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static br.unicamp.cst.commands.CSTInit.TAB;

public class AgentConfig {

    private String projectName;

    private List<CodeletConfig> codelets;
    private List<MemoryConfig> memories;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<CodeletConfig> getCodelets() {
        return codelets;
    }

    public void setCodelets(List<CodeletConfig> codelets) {
        this.codelets = codelets;
    }

    public List<MemoryConfig> getMemories() {
        return memories;
    }

    public void setMemories(List<MemoryConfig> memories) {
        this.memories = memories;
    }

    public String generateCode() {
        String templateInstance = TemplatesBundle.getInstance().getTemplate("AgentMindTemplate");

        Iterator<String> uniqueCodeletsGroups = getCodelets().stream()
                .map(CodeletConfig::getGroup).distinct().iterator();
        StringBuilder codeletGroups = new StringBuilder();
        while (uniqueCodeletsGroups.hasNext()){
            codeletGroups.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("createCodeletGroup(\"")
                    .append(uniqueCodeletsGroups.next())
                    .append("\");");
        }

        Iterator<String> uniqueMemoryGroups = getMemories().stream()
                .map(MemoryConfig::getGroup).distinct().iterator();
        StringBuilder memoryGroups = new StringBuilder();
        while (uniqueMemoryGroups .hasNext()){
            memoryGroups .append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("createMemoryGroup(\"")
                    .append(uniqueMemoryGroups .next())
                    .append("\");");
        }

        StringBuilder memoryDeclarations = new StringBuilder();
        StringBuilder memoryInit = new StringBuilder();
        for (MemoryConfig memory : this.getMemories()){
            // Declare memory variable
            memoryDeclarations.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("public Memory ")
                    .append(memory.getName())
                    .append(";");
            // Initialize memory object
            memoryInit.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append(memory.getName());

        }


        templateInstance = templateInstance.replace("{{codeletGroups}}", codeletGroups.toString());
        templateInstance = templateInstance.replace("{{memoryGroups}}", memoryGroups.toString());
        templateInstance = templateInstance.replace("{{memoryObjects}}", memoryDeclarations.toString());
        return templateInstance;
    }

    @Override
    public String toString() {
        return "AgentConfig{" +
                "projectName='" + projectName + '\'' +
                ", codelets=" + codelets +
                ", memories=" + memories +
                '}';
    }
}
