package org.vaadin.example.bookstore;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.example.bookstore.ui.MainLayout;

public class CustomerForm extends VerticalLayout {

    private Binder<Customer> binder = new Binder<>(Customer.class);
    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MainLayout myUI;
    private CustomerFormDesign cfd = new CustomerFormDesign();

    public CustomerForm(MainLayout myUI) {
        this.myUI = myUI;
        cfd.status.setItems(CustomerStatus.values());
        cfd.save.addClickShortcut(Key.ENTER);
        cfd.save.addClickListener(e -> this.save());
        cfd.delete.addClickListener(e -> this.delete());
        add(cfd);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.setBean(customer);
        binder.bindInstanceFields(cfd);

        // Show delete button for only customers already in the database
        cfd.delete.setVisible(customer.isPersisted());
        setVisible(true);
//        firstName();
    }

    private void delete() {
        service.delete(customer);
        myUI.updateList();
        setVisible(false);
    }

    private void save() {
        service.save(customer);
        myUI.updateList();
        setVisible(false);
    }
}