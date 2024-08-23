package br.unicamp.cst.data;

import java.util.List;

public class CodeletConfig {
    private String name;
    private String group;
    private List<String> in;
    private List<String> out;
    private List<String> broadcast;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getIn() {
        return in;
    }

    public void setIn(List<String> in) {
        this.in = in;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }

    public List<String> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(List<String> broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public String toString() {
        return "CodeletConfig{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", in=" + in +
                ", out=" + out +
                ", broadcast=" + broadcast +
                '}';
    }
}
