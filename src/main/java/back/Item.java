package back;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Item {
    private Item parent;
    private Set<Item> children;
    private int id;
    private String name;
    private HashMap<String,String> param;




    public Item(String name, HashMap<String,String> param, int id, Item parent, Set<Item> children) {
        this.name = name;
        this.param = param;
        this.id = id;
        this.parent = parent;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(HashMap<String,String> param) {
        this.param = param;
    }

    public HashMap<String,String> getParam() {
        return param;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public Item getParent() {
        return parent;
    }

    public void setChildren(Set<Item> children) {
        this.children = children;
    }

    public Set<Item> getChildren() {
        return children;
    }
}
