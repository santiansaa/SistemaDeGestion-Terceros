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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import utn.frp.comp.terceros.model.Pagos;
import utn.frp.comp.terceros.model.Tercero;
import utn.frp.comp.terceros.services.PagosService;
import utn.frp.comp.terceros.services.TerceroService;

@Route("pagos") // Mapea con la ruta exacta en plural que te pide Vaadin
public class PagosView extends VerticalLayout {

    private final PagosService pagosService;
    private final TerceroService terceroService;
    private Pagos pago;
    private final Binder<Pagos> binder = new BeanValidationBinder<>(Pagos.class);

    private Grid<Pagos> grid = new Grid<>(Pagos.class, false);

    // --- INPUTS REALES DE TU BD (Volamos el nroRecibo) ---
    private DatePicker fecha = new DatePicker("Fecha de Pago");
    private NumberField importe = new NumberField("Importe a Pagar");
    private com.vaadin.flow.component.select.Select<String> formaPago = new com.vaadin.flow.component.select.Select<>();
    private ComboBox<Tercero> tercero = new ComboBox<>("Proveedor / Tercero");

    private Button guardar = new Button("Guardar Pago");
    private Button cancelar = new Button("Cancelar");
    private H2 titulo = new H2("Registrar Pago");

    public PagosView(PagosService pagosService, TerceroService terceroService) {
    	
    	formaPago.setLabel("Forma de Pago");
    	formaPago.setItems("CHEQUE", "EFECTIVO", "TARJETA", "TRANSFERENCIA", "MERCADO PAGO");
    	formaPago.setRequiredIndicatorVisible(true); 
        this.pagosService = pagosService;
        this.terceroService = terceroService;

        setSizeFull();
        setPadding(true);

        Button btnHome = new Button("Volver al Menú", VaadinIcon.HOME.create());
        btnHome.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnHome.addClickListener(e -> UI.getCurrent().navigate(""));

        fecha.setRequiredIndicatorVisible(true);
        importe.setRequiredIndicatorVisible(true);
        importe.setPrefixComponent(new HorizontalLayout(new com.vaadin.flow.component.html.Span("$")));

        tercero.setItems(terceroService.findAll());
        tercero.setItemLabelGenerator(Tercero::getNombre);
        tercero.setRequired(true);

        // Formulario ordenado sin el campo fantasma
        FormLayout formLayout = new FormLayout();
        formLayout.add(fecha, tercero, importe, formaPago);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);
        acciones.setWidthFull();

        VerticalLayout contenedorFormulario = new VerticalLayout(titulo, formLayout, acciones);
        contenedorFormulario.setWidth("350px");
        contenedorFormulario.setPadding(false);

        configurarGrid();

        VerticalLayout contenedorGrid = new VerticalLayout(new H2("Listado de Pagos Realizados"), grid);
        contenedorGrid.setSizeFull();
        contenedorGrid.setPadding(false);

        HorizontalLayout cuerpoPrincipal = new HorizontalLayout(contenedorFormulario, contenedorGrid);
        cuerpoPrincipal.setSizeFull();
        cuerpoPrincipal.setFlexGrow(1, contenedorGrid);

        add(btnHome, cuerpoPrincipal);

        binder.bindInstanceFields(this);
        guardar.addClickListener(e -> guardarPago());
        cancelar.addClickListener(e -> limpiarFormulario());

        setPago(new Pagos());
        actualizarLista();
    }

    private void configurarGrid() {
        grid.setSizeFull();
        // Cambiamos la primera columna para que muestre tu "id_pagos" real de la BD
        grid.addColumn(Pagos::getId).setHeader("ID Pago").setAutoWidth(true);
        grid.addColumn(Pagos::getFecha).setHeader("Fecha").setAutoWidth(true);
        grid.addColumn(p -> p.getTercero() != null ? p.getTercero().getNombre() : "").setHeader("Proveedor").setAutoWidth(true);
        grid.addColumn(Pagos::getFormaPago).setHeader("Forma de Pago").setAutoWidth(true);
        grid.addColumn(Pagos::getImporte).setHeader("Importe ($)").setAutoWidth(true);

        grid.addComponentColumn(p -> {
            Button btnBorrar = new Button(VaadinIcon.TRASH.create());
            btnBorrar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            btnBorrar.addClickListener(e -> eliminarPago(p));
            return new HorizontalLayout(btnBorrar);
        }).setHeader("Acciones").setAutoWidth(true);
    }

    private void actualizarLista() {
        grid.setItems(pagosService.findAll());
    }

    private void setPago(Pagos pago) {
        this.pago = pago;
        binder.readBean(pago);
    }

    private void guardarPago() {
        try {
            if (fecha.getValue() == null || importe.isEmpty() || tercero.isEmpty()) {
                Notification.show("Los campos Fecha, Importe y Tercero son obligatorios.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            binder.writeBean(pago);
            pagosService.save(pago);

            Notification.show("Pago registrado con éxito.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            limpiarFormulario();
            actualizarLista();
        } catch (Exception e) {
            Notification.show("Error al registrar: " + e.getMessage(), 4000, Notification.Position.BOTTOM_START);
        }
    }

    private void limpiarFormulario() {
        titulo.setText("Registrar Pago");
        setPago(new Pagos());
    }

    private void eliminarPago(Pagos p) {
        try {
            pagosService.deleteById(p.getId());
            Notification.show("Pago eliminado correctamente.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
        } catch (Exception e) {
            Notification.show("Error al eliminar: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }
}
