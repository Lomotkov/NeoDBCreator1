//package ru.lom.d;
//
//import com.vaadin.data.Binder;
//import com.vaadin.event.ShortcutAction;
//import com.vaadin.icons.VaadinIcons;
//import com.vaadin.ui.*;
//import com.vaadin.ui.themes.ValoTheme;
//
//public class CustomerForm extends FormLayout {
//    private TextField nameField = new TextField("name");
//    private TextField secondNameField = new TextField("Second name");
//    private TextField email = new TextField("E-mail");
//    private NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
//    private DateField birthDate = new DateField("birth");
//    private Button save = new Button("Save");
//    private Button delete = new Button("Delete");
//    private Binder<Customer> binder = new Binder<>(Customer.class);
//
//    private CustomerService service = CustomerService.getInstance();
//    private Customer customer;
//    private MyUI myUI;
//
//    public CustomerForm(MyUI myUI) {
//        this.myUI = myUI;
//        setSizeUndefined();
//        HorizontalLayout buttons = new HorizontalLayout(save, delete);
//        addComponents(nameField, secondNameField, email, status, birthDate, buttons);
//
//        status.setItems(CustomerStatus.values());
//        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//
//        binder.bindInstanceFields(this);
//
//        save.addClickListener(e -> save());
//        delete.addClickListener(e -> delete());
//
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//        binder.setBean(customer);
//
//        delete.setVisible(customer.isPersisted());
//        setVisible(true);
//        nameField.selectAll();
//    }
//
//    public void delete() {
//        service.delete(customer);
//        myUI.updateList();
//        setVisible(false);
//    }
//
//    public void save() {
//        service.save(customer);
//        myUI.updateList();
//        setVisible(false);
//    }


//}
