package utn.frp.comp.terceros.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import utn.frp.comp.terceros.model.Tercero;
import utn.frp.comp.terceros.services.TerceroService;

@Route("tercero") 
public class TerceroView extends VerticalLayout implements HasUrlParameter<Long> {

    private final TerceroService terceroService;
    private Tercero tercero;
    private final Binder<Tercero> binder = new BeanValidationBinder<>(Tercero.class);

    // Componente de Grilla
    private Grid<Tercero> grid = new Grid<>(Tercero.class, false);

    // Componentes del Formulario
    private TextField nombre = new TextField("Nombre / Razón Social");
    private TextField cuit = new TextField("CUIT");
    private TextField sitiva = new TextField("Situación frente al IVA");
    private TextField direccion = new TextField("Dirección");
    private TextField localidad = new TextField("Localidad");
    private TextField provincia = new TextField("Provincia");
    private TextField telefonos = new TextField("Teléfonos");
    private NumberField saldoApertura = new NumberField("Saldo de Apertura");
    private TextField tipoSaldo = new TextField("Tipo de Saldo");

    private Button guardar = new Button("Guardar");
    private Button cancelar = new Button("Cancelar");
    private H2 titulo = new H2("Crear Tercero");
    {
    	Button btnHome = new Button("Volver al Menú", VaadinIcon.HOME.create());
    	btnHome.addClickListener(e -> UI.getCurrent().navigate(""));
    	add(btnHome); // O agregarlo al inicio de la pantalla
    }

    public TerceroView(TerceroService terceroService) {
        this.terceroService = terceroService;
        
        // Configuramos el contenedor principal para que use todo el ancho y alto disponible
        setSizeFull();
        setPadding(true);

        // Configuraciones de inputs
        nombre.setRequiredIndicatorVisible(true);
        cuit.setRequiredIndicatorVisible(true);
        sitiva.setRequiredIndicatorVisible(true);
        direccion.setRequiredIndicatorVisible(true);
        saldoApertura.setPrefixComponent(new HorizontalLayout(new com.vaadin.flow.component.html.Span("$")));

        // --- COLUMNA IZQUIERDA: Formulario de Carga ---
        // Usamos 1 sola columna para que los inputs queden uno abajo del otro y más estéticos
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, cuit, sitiva, direccion, localidad, provincia, telefonos, saldoApertura, tipoSaldo);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1)); 

        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);
        acciones.setWidthFull();

        // Agrupamos el título, inputs y botones en un contenedor vertical para el lado izquierdo
        VerticalLayout contenedorFormulario = new VerticalLayout(titulo, formLayout, acciones);
        contenedorFormulario.setWidth("350px"); // Fijamos un ancho cómodo para los textboxes
        contenedorFormulario.setPadding(false);

        // --- COLUMNA DERECHA: Listado (Grilla) ---
        configurarGrid();
        
        VerticalLayout contenedorGrid = new VerticalLayout(new H2("Listado de Terceros"), grid);
        contenedorGrid.setSizeFull(); // Que ocupe todo el resto del espacio disponible
        contenedorGrid.setPadding(false);

        // --- DISTRIBUCIÓN PRINCIPAL (PANTALLA DIVIDIDA) ---
        // Ponemos el formulario a la izquierda y la grilla a la derecha usando un HorizontalLayout
        HorizontalLayout cuerpoPrincipal = new HorizontalLayout(contenedorFormulario, contenedorGrid);
        cuerpoPrincipal.setSizeFull();
        cuerpoPrincipal.setFlexGrow(1, contenedorGrid); // Le da prioridad de expansión a la grilla

        // Agregamos el diseño distribuído al VerticalLayout principal de la clase
        add(cuerpoPrincipal);

        // Vinculación automática y eventos
        binder.bindInstanceFields(this);
        guardar.addClickListener(e -> guardarTercero());
        cancelar.addClickListener(e -> limpiarFormulario());

        setTercero(new Tercero());
        actualizarLista();
    }

    private void configurarGrid() {
        // Hacemos que la grilla use todo el alto disponible para mostrar muchas filas
        grid.setSizeFull(); 
        
        grid.addColumn(Tercero::getNombre).setHeader("Nombre / Razón Social").setAutoWidth(true).setSortable(true);
        grid.addColumn(Tercero::getCuit).setHeader("CUIT").setAutoWidth(true);
        grid.addColumn(Tercero::getSitiva).setHeader("Sit. IVA").setAutoWidth(true);
        grid.addColumn(Tercero::getLocalidad).setHeader("Localidad").setAutoWidth(true);
        
        grid.addComponentColumn(t -> {
            Button btnEditar = new Button(VaadinIcon.EDIT.create());
            btnEditar.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnEditar.addClickListener(e -> {
                titulo.setText("Actualizar Tercero #" + t.getId());
                setTercero(t);
            });

            Button btnBorrar = new Button(VaadinIcon.TRASH.create());
            btnBorrar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            btnBorrar.addClickListener(e -> eliminarTercero(t));

            return new HorizontalLayout(btnEditar, btnBorrar);
        }).setHeader("Acciones").setAutoWidth(true);
    }

    private void actualizarLista() {
        grid.setItems(terceroService.findAll()); 
    }

    private void eliminarTercero(Tercero t) {
        try {
            terceroService.deleteById(t.getId()); 
            Notification.show("Tercero eliminado correctamente.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            limpiarFormulario();
        } catch (Exception e) {
            Notification.show("Error al eliminar: " + e.getMessage(), 4000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            terceroService.findById(id).ifPresentOrElse(t -> {
                titulo.setText("Actualizar Tercero #" + t.getId());
                setTercero(t);
            }, () -> Notification.show("Tercero no encontrado", 3000, Notification.Position.MIDDLE));
        }
    }

    private void setTercero(Tercero tercero) {
        this.tercero = tercero;
        binder.readBean(tercero); 
    }

    private void guardarTercero() {
        try {
            if (nombre.isEmpty() || cuit.isEmpty() || sitiva.isEmpty() || direccion.isEmpty()) {
                Notification.show("Los campos Nombre, CUIT, Situación IVA y Dirección son obligatorios.", 
                        4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            binder.writeBean(tercero);
            terceroService.save(tercero);
            
            Notification notification = Notification.show("¡Tercero guardado correctamente!", 3000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            limpiarFormulario();
            actualizarLista(); 
        } catch (ValidationException e) {
            Notification.show("Por favor, revise que los formatos ingresados sean correctos.", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al guardar en la base de datos: " + e.getMessage(), 4000, Notification.Position.BOTTOM_START);
        }
    }

    private void limpiarFormulario() {
        titulo.setText("Crear Tercero");
        setTercero(new Tercero());
    }
}