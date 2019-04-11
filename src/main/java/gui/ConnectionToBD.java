package gui;

import back.BDWorker;
import back.Item;
import back.Utils;
import com.vaadin.data.TreeData;
import com.vaadin.ui.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

public class ConnectionToBD extends FormLayout {
    private static BDWorker worker;
    private MyUI myUI;
    private Tree<Item> tree;
    VerticalLayout v1 = new VerticalLayout();
    HorizontalLayout h1 = new HorizontalLayout();
    private NativeSelect<NetworksProtocols> networksProtocols = new NativeSelect<>("Сетевой протокол");
    private TextField urlTextField = new TextField("Url");
    private TextField portTextField = new TextField("Порт");
    private TextField loginTextField = new TextField("Логин");
    private Label labelConnection = new Label();
    private PasswordField passwordField = new PasswordField("Пароль");

    public ConnectionToBD(MyUI myUI, Tree<Item> tree) {
        this.myUI = myUI;
        this.tree = tree;
        h1.addComponents(networksProtocols, new Label("://"), urlTextField, new Label(":"), portTextField);
        v1.addComponents(h1, loginTextField, passwordField, labelConnection);
        networksProtocols.setItems(NetworksProtocols.values());
        labelConnection.setVisible(false);
        urlTextField.setValue("localhost");
        portTextField.setValue("7687");
        loginTextField.setValue("neo4j");
        portTextField.setWidth("80");
        addComponent(v1);
    }

    public static BDWorker getWorker() {
        return worker;
    }

    public void connect() {
        try {
            if (worker == null) {
                worker = new BDWorker(networksProtocols.getValue().name()
                        + "://"
                        + loginTextField.getValue()
                        + ":"
                        + passwordField.getValue()
                        + "@"
                        + urlTextField.getValue()
                        + ":"
                        + portTextField.getValue());
                labelConnection.setVisible(true);
                labelConnection.setValue("Подключение успешно прошло");

                System.out.println("2");
            } else {
                labelConnection.setVisible(true);
                labelConnection.setValue("Подключение уже было установлено");
            }
        } catch (IllegalArgumentException e) {
            labelConnection.setValue("Что-то пошло не так: " + e.getMessage());
        }
    }



    public Tree<Item> getTree() {
        return tree;
    }
}
