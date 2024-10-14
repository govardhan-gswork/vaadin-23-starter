package org.vaadin.example.bookstore.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.example.bookstore.Customer;
import org.vaadin.example.bookstore.CustomerForm;
import org.vaadin.example.bookstore.CustomerService;
//import org.vaadin.example.bookstore.authentication.AccessControl;
//import org.vaadin.example.bookstore.authentication.AccessControlFactory;
//import org.vaadin.example.bookstore.ui.about.AboutView;
//import org.vaadin.example.bookstore.ui.inventory.InventoryView;

import java.util.List;

/**
 * The main layout. Contains the navigation menu.
 */
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
@Route("/")
public class MainLayout extends AppLayout implements RouterLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid(Customer.class);
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);

    public MainLayout() {

        final VerticalLayout layout = new VerticalLayout();

        filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> {
            grid.setItems(new ListDataProvider<>(service.findAll(e.getValue())));
        });

        Button clearFilterTextBtn = new Button();
        clearFilterTextBtn.setIcon(new Icon(VaadinIcon.CLOSE));
//        clearFilterTextBtn.se("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });

        HorizontalLayout filtering = new HorizontalLayout();
        filtering.add(filterText, clearFilterTextBtn);
//        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
        toolbar.setSpacing(true);

        grid.setColumns("firstName", "lastName", "email");

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSpacing(true);
        main.setSizeFull();
        grid.setWidthFull();
        main.setFlexGrow(1, grid);

        layout.add(toolbar, main);

        updateList();

        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        form.setVisible(false);

        grid.addSelectionListener(event -> {
            if (event.getAllSelectedItems().isEmpty()) {
                form.setVisible(false);
            } else {
                Customer customer = (Customer) event.getAllSelectedItems().iterator().next();
                form.setCustomer(customer);
            }
        });
    }

    public void updateList() {
        // fetch list of Customers from service and assign it to Grid
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setItems(customers);
    }

//    private void logout() {
//        AccessControlFactory.getInstance().createAccessControl().signOut();
//    }

    private RouterLink createMenuLink(Class<? extends Component> viewClass,
            String caption, Icon icon) {
        final RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setClassName("menu-link");
        routerLink.add(icon);
        routerLink.add(new Span(caption));
        icon.setSize("24px");
        return routerLink;
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }

//    private void registerAdminViewIfApplicable(AccessControl accessControl) {
//        // register the admin view dynamically only for any admin user logged in
//        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)
//                && !RouteConfiguration.forSessionScope()
//                        .isRouteRegistered(AdminView.class)) {
//            RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME,
//                    AdminView.class, MainLayout.class);
//            // as logout will purge the session route registry, no need to
//            // unregister the view on logout
//        }
//    }

//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        super.onAttach(attachEvent);
//
//        // User can quickly activate logout with Ctrl+L
//        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L,
//                KeyModifier.CONTROL);
//
//        // add the admin view menu item if user has admin role
//        final AccessControl accessControl = AccessControlFactory.getInstance()
//                .createAccessControl();
//        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
//
//            // Create extra navigation target for admins
//            registerAdminViewIfApplicable(accessControl);
//
//            // The link can only be created now, because the RouterLink checks
//            // that the target is valid.
//            addToDrawer(createMenuLink(AdminView.class, AdminView.VIEW_NAME,
//                    VaadinIcon.DOCTOR.create()));
//        }
//
//        // Finally, add logout button for all users
//        addToDrawer(logoutButton);
//    }

}
