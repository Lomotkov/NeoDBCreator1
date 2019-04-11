package gui;

import javax.servlet.annotation.WebServlet;

import back.BDWorker;
import back.Item;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.TreeData;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.lom.d.CustomerService;


import java.util.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Title("NeoDBCreator")
@Theme("mytheme")
public class MyUI extends UI {
    private CustomerService service = CustomerService.getInstance();
    //    private Grid<Customer> infGrid = new Grid<>(Customer.class);
//    private TextField filterText = new TextField();
    private Button clearFilterButton = new Button(VaadinIcons.BAN);
    private Button addCustomerBtn = new Button("Add new custmomer");
    //    private CustomerForm form = new CustomerForm(this);
    private Tree<Item> tree = new Tree<>();
    private ConnectionToBD connectionForm = new ConnectionToBD(this, tree);
    //    private TextField outputText = new TextField("Selected item");
    private Button connectionParametres = new Button(VaadinIcons.DATABASE);
    Button generate = new Button("Сгенерировать");
    TextField textField = new TextField("add new");
    VerticalLayout ver1 = new VerticalLayout();
    VerticalLayout ver2 = new VerticalLayout();
    VerticalLayout ver3 = new VerticalLayout();
    Label parametrLabel = new Label("Параметры");
    HorizontalLayout head = new HorizontalLayout();
    Label labelNameOfProgram = new Label("Система генерации экземпляров");
    private Button connectButton = new Button("Подключить");
    BDWorker worker;
    ArrayList<Item> parents = new ArrayList<>();


//    public TreeData<Item> createTreeObjects() {
//        TreeData<Item> tree = new TreeData<>();
//        Item object = new Item("Person");
//        tree.addItem(null,object);
//        tree.addItem(object,new Item("123"));
//        return tree;
//    }
//
//    public TreeData setDataTest() {
//        TreeData<Item> treeData = new TreeData();
//        Item objects = new Item("Объекты", null, 0, null, null);
//        Item agents = new Item("Агенты", null, 1, null, null);
//        Item station = new Item("Состояния", null, 2, null, null);
//        treeData.addItem(null, objects);
//        treeData.addItem(null, agents);
//        treeData.addItem(null, station);
//        treeData.addItem(objects, new Item("Пища", new ArrayList<>(Arrays.asList("Количество", "Сытность")), 4, null, null));
//        treeData.addItem(objects, new Item("Дерево", new ArrayList<>(Arrays.asList("Высота", "Возраст")), 5, null, null));
//        treeData.addItem(agents, new Item("Человек", null, 6, null, null));
//        treeData.addItem(agents, new Item("Корова", null, 7, null, null));
//        return treeData;
//    }


    public void select(SelectionEvent<Item> event) {
        Optional<Item> optionalItems = event.getFirstSelectedItem();
        if (optionalItems.get() != null) {
            if (optionalItems.get().getParam() == null) {
                ver2.removeAllComponents();
                ver2.addComponent(parametrLabel);
                return;
            }
            if (optionalItems.isPresent()) {
//                ver2.forEach(comp -> {
//                    if(comp instanceof TextField) {
//                        TextField text = (TextField) comp;
//                        optionalItems.get().getParam().put(text.getCaption(), text.getValue());
//                    }
//                });
                ver2.removeAllComponents();
                ver2.addComponent(parametrLabel);
                HashMap<String, String> params = optionalItems.get().getParam();
                params.keySet().forEach(key -> {
                    TextField text = new TextField(key);
                    ver2.addComponent(text);
                    text.setValue(params.get(key));
                    text.addValueChangeListener(e -> {
                        params.put(text.getCaption(), e.getValue());
                    });
                });
            }
        }
    }


    private void ChangeVisible(Component component) {
        if (component.isVisible()) {
            component.setVisible(false);
        } else {
            component.setVisible(true);
        }
    }

    private void save() {
        parents.forEach(item -> worker.createObjects(new ArrayList<>(getChildData(item))));
    }

    private List<Item> getChildData(Item parentItem){
        return  tree.getTreeData().getChildren(parentItem);
    }

    private TreeData setDataToTree() {
        connectionForm.connect();
        worker = ConnectionToBD.getWorker();
        TreeData<Item> treeData = new TreeData();
        Item itemLabel;
        ArrayList<String> names;
        ArrayList<String> labels = worker.getLabels();
        for (String label : labels) {
            label = label.substring(label.indexOf('[') + 1, label.indexOf(']'));
            if (label == null || label.equals("")) {
                continue;
            }
            names = worker.getNodesByLabel(label);
            itemLabel = new Item(label, null, 0, null, null);
            if (label.equals("Агент") || label.equals("Объект")) {
                treeData.addItem(null, itemLabel);
                parents.add(itemLabel);
                for (String name : names) {
                    ArrayList<String> parametres = worker.getParametres(label, name);
                    HashMap<String, String> parametr = new HashMap<>();
                    parametres.forEach(param -> parametr.put(param, "0-0"));
                    treeData.addItem(itemLabel, new Item(name, parametr, 0, itemLabel, null));
                }
            }
        }
        return treeData;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //        filterText.setPlaceholder("Filter by name....");
//        filterText.addValueChangeListener(e -> updateList());
//        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        //        infGrid.setColumns("firstName", "lastName", "email");
//        addCustomerBtn.addClickListener(e -> {
//            infGrid.asSingleSelect().clear();
//            form.setCustomer(new Customer());
//        });
        //        clearFilterButton.addClickListener(e -> filterText.clear());
//        infGrid.setSizeFull();
//        form.setVisible(false);
//        infGrid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() == null) {
//                form.setVisible(false);
//            } else {
//                form.setCustomer(event.getValue());
//            }
//        });
        // main.setExpandRatio(infGrid, 1);
        //        updateList();
        //        clearFilterButton.setDescription("Clear  filter");
        connectButton.addClickListener(e -> {
            tree.setTreeData(setDataToTree());
        });
        labelNameOfProgram.setStyleName("title");
        final VerticalLayout layout = new VerticalLayout();
        head.addComponents(labelNameOfProgram, connectionParametres);
        head.setWidth("100%");
        head.addStyleName("darkBackLay");
        head.setHeight("100");
        addStyleName("greyBackLay");
        tree.setItemCaptionGenerator(data -> data.getName());
        generate.addClickListener(e-> save());
        tree.setSizeFull();
        connectionParametres.addClickListener(e -> ChangeVisible(ver3));
        tree.addSelectionListener(e -> select(e));
        FormLayout main = new FormLayout();
        ver1.addComponents(tree, generate);
        ver2.addComponents(parametrLabel);
        HorizontalLayout toolbar = new HorizontalLayout(main);
//        BDWorker movie = new BDWorker("bolt://neo4j:123@localhost:7687");
//        movie.search("p");
        CssLayout filtering = new CssLayout();
        filtering.addComponents(toolbar, clearFilterButton);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        ver3.addComponents(connectionForm, connectButton);
        ver3.setVisible(false);
        layout.addComponents(head, new HorizontalLayout(ver1, ver2, ver3));
        setContent(layout);

    }

//
//    public void updateList() {
//        List<Customer> allCustomers = service.findAll(filterText.getValue());
//        infGrid.setItems(allCustomers);
//    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
