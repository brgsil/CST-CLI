package br.unicamp.cst.data;

import java.util.List;

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
        return "";
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
