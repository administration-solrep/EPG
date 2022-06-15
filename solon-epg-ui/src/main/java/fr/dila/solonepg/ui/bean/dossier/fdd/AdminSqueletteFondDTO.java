package fr.dila.solonepg.ui.bean.dossier.fdd;

import java.util.LinkedList;

public class AdminSqueletteFondDTO {
    private String name;
    private String id;
    private Boolean editable;
    private LinkedList<AdminSqueletteFondDTO> children;

    public AdminSqueletteFondDTO() {
        this.children = new LinkedList<>();
        this.editable = false;
    }

    public AdminSqueletteFondDTO(String id, String name, Boolean editable) {
        this();
        this.id = id;
        this.name = name;
        this.editable = editable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<AdminSqueletteFondDTO> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<AdminSqueletteFondDTO> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
