package utn.frp.comp.terceros.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import utn.frp.comp.terceros.model.Facultad;
import utn.frp.comp.terceros.services.FacultadService;

@Route(value = "facultad") // URL base: http://localhost:8080/facultad
public class FacultadView extends VerticalLayout implements HasUrlParameter<Long> {

    private final FacultadService facultadService;
    private Facultad facultad;
    private final Binder<Facultad> binder = new BeanValidationBinder<>(Facultad.class);

    // Componentes del Formulario
    private TextField nombre = new TextField("Nombre");
    private TextField direccion = new TextField("Dirección");
    private TextField cuit = new TextField("CUIT");
    private IntegerField sucursal = new IntegerField("Sucursal");
    private TextField telefonos = new TextField("Teléfonos");
    private TextField correo = new TextField("Correo Electrónico");
    private Checkbox defecto = new Checkbox("Por Defecto");

    private Button guardar = new Button("Guardar");
    private Button cancelar = new Button("Cancelar");
    private H2 titulo = new H2("Crear Facultad");
    {
    	Button btnHome = new Button("Volver al Menú", VaadinIcon.HOME.create());
    	btnHome.addClickListener(e -> UI.getCurrent().navigate(""));
    	add(btnHome); // O agregarlo al inicio de la pantalla
    }

    public FacultadView(FacultadService facultadService) {
        this.facultadService = facultadService;
        
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        // Diseño del formulario (organiza en 2 columnas si hay espacio)
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, direccion, cuit, sucursal, telefonos, correo, defecto);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));

        // Botones
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);

        // Vincular campos automáticos por nombre de variable coincidente con el Modelo
        binder.bindInstanceFields(this);

        // Eventos
        guardar.addClickListener(e -> guardarFacultad());
        cancelar.addClickListener(e -> limpiarFormulario());

        // Agregar al contenedor principal
        add(titulo, formLayout, acciones);
        
        // Inicializar con un objeto vacío por defecto (Alta)
        setFacultad(new Facultad());
    }

    // Este método captura el ID de la URL si viene como parámetro (ej: /facultad/3) para actualización
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            facultadService.findById(id).ifPresentOrElse(f -> {
                titulo.setText("Actualizar Facultad #" + f.getId());
                setFacultad(f);
            }, () -> Notification.show("Facultad no encontrada", 3000, Notification.Position.MIDDLE));
        }
    }

    private void setFacultad(Facultad facultad) {
        this.facultad = facultad;
        binder.readBean(facultad); // Vuelca los datos del objeto en los inputs de la pantalla
    }
    

    private void guardarFacultad() {
        try {
            // Escribe y valida los datos de la interfaz hacia el objeto "facultad"
            binder.writeBean(facultad);
            
            // Persistimos mediante el servicio
            facultadService.save(facultad);
            
            Notification notification = Notification.show("¡Datos guardados con éxito!", 3000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            // Volvemos a un estado limpio o podrías redirigir a una grilla
            limpiarFormulario();
            
        } catch (ValidationException e) {
            Notification.show("Por favor, revise los errores en el formulario.", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al guardar: " + e.getMessage(), 4000, Notification.Position.BOTTOM_START);
        }
    }

    private void limpiarFormulario() {
        titulo.setText("Crear Facultad");
        setFacultad(new Facultad());
    }
    
    
}
