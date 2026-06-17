package utn.frp.comp.terceros.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import utn.frp.comp.terceros.model.Factura;
import utn.frp.comp.terceros.model.Tercero;
import utn.frp.comp.terceros.services.FacturaService;
import utn.frp.comp.terceros.services.TerceroService;

@Route("factura")
public class FacturaView extends VerticalLayout {

    private final FacturaService facturaService;
    private final TerceroService terceroService;
    private Factura factura;
    private final Binder<Factura> binder = new BeanValidationBinder<>(Factura.class);

    private Grid<Factura> grid = new Grid<>(Factura.class, false);

    // Inputs del Formulario
    private TextField nroFactura = new TextField("Nro. Factura");
    private DatePicker fecha = new DatePicker("Fecha");
    private TextField descripcionItem = new TextField("Descripción del Ítem");
    private NumberField precioUnitario = new NumberField("Precio Unitario");
    private IntegerField cantidad = new IntegerField("Cantidad");
    
    // EL COMPONENTE CLAVE: Combo para elegir el Tercero
    private ComboBox<Tercero> tercero = new ComboBox<>("Proveedor / Tercero");

    private Button guardar = new Button("Guardar");
    private Button cancelar = new Button("Cancelar");
    private H2 titulo = new H2("Registrar Factura");

    public FacturaView(FacturaService facturaService, TerceroService terceroService) {
        this.facturaService = facturaService;
        this.terceroService = terceroService;

        setSizeFull();
        setPadding(true);

        Button btnHome = new Button("Volver al Menú", VaadinIcon.HOME.create());
        btnHome.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnHome.addClickListener(e -> UI.getCurrent().navigate(""));

        // Configurar el ComboBox de Terceros
        tercero.setItems(terceroService.findAll()); // Llenamos el combo con los terceros de la BD
        tercero.setItemLabelGenerator(Tercero::getNombre); // Indicamos que muestre el Nombre en el combo
        tercero.setRequired(true);

        FormLayout formLayout = new FormLayout();
        formLayout.add(nroFactura, fecha, tercero, descripcionItem, precioUnitario, cantidad);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);

        VerticalLayout contenedorFormulario = new VerticalLayout(titulo, formLayout, acciones);
        contenedorFormulario.setWidth("350px");
        contenedorFormulario.setPadding(false);

        configurarGrid();

        VerticalLayout contenedorGrid = new VerticalLayout(new H2("Listado de Facturas"), grid);
        contenedorGrid.setSizeFull();
        contenedorGrid.setPadding(false);

        HorizontalLayout cuerpoPrincipal = new HorizontalLayout(contenedorFormulario, contenedorGrid);
        cuerpoPrincipal.setSizeFull();
        cuerpoPrincipal.setFlexGrow(1, contenedorGrid);

        add(btnHome, cuerpoPrincipal);

        binder.bindInstanceFields(this);
        guardar.addClickListener(e -> guardarFactura());
        cancelar.addClickListener(e -> limpiarFormulario());

        setFactura(new Factura());
        actualizarLista();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.addColumn(Factura::getNroFactura).setHeader("Nro. Factura").setAutoWidth(true);
        grid.addColumn(Factura::getFecha).setHeader("Fecha").setAutoWidth(true);
        // Mostramos el nombre del tercero asociado en la tabla
        grid.addColumn(f -> f.getTercero() != null ? f.getTercero().getNombre() : "").setHeader("Proveedor").setAutoWidth(true);
        grid.addColumn(Factura::getDescripcionItem).setHeader("Ítem").setAutoWidth(true);
        grid.addColumn(Factura::getTotal).setHeader("Total ($)").setAutoWidth(true);

        grid.addComponentColumn(f -> {
            Button btnBorrar = new Button(VaadinIcon.TRASH.create());
            btnBorrar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            btnBorrar.addClickListener(e -> eliminarFactura(f));
            return new HorizontalLayout(btnBorrar);
        }).setHeader("Acciones").setAutoWidth(true);
    }

    private void actualizarLista() { grid.setItems(facturaService.findAll()); }

    private void setFactura(Factura factura) {
        this.factura = factura;
        binder.readBean(factura);
    }

    private void guardarFactura() {
        try {
            if (nroFactura.isEmpty() || fecha.getValue() == null || tercero.isEmpty()) {
                Notification.show("Nro. Factura, Fecha y Tercero son obligatorios.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            binder.writeBean(factura);
            facturaService.save(factura);
            
            Notification.show("Factura registrada con éxito.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();
            actualizarLista();
        } catch (Exception e) {
            Notification.show("Error al guardar: " + e.getMessage(), 4000, Notification.Position.BOTTOM_START);
        }
    }

    private void limpiarFormulario() {
        titulo.setText("Registrar Factura");
        setFactura(new Factura());
    }

    private void eliminarFactura(Factura f) {
        try {
            facturaService.deleteById(f.getId());
            Notification.show("Factura eliminada.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
        } catch (Exception e) {
            Notification.show("Error al eliminar: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }
}
