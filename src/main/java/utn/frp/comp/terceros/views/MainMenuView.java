package utn.frp.comp.terceros.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import utn.frp.comp.terceros.model.Usuario;
import utn.frp.comp.terceros.services.UsuarioService;
import utn.frp.comp.terceros.services.PagosService;

@Route("") 
public class MainMenuView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private final PagosService pagosService;
    
    private VerticalLayout layoutLogin = new VerticalLayout();
    private VerticalLayout layoutMenu = new VerticalLayout();

    public MainMenuView(UsuarioService usuarioService, PagosService pagosService) {
        this.usuarioService = usuarioService;
        this.pagosService = pagosService;
        
        setSizeFull();
        setPadding(true);

        armarSeccionLogin();
        armarSeccionMenu();

        Usuario usuarioLogueado = (Usuario) VaadinSession.getCurrent().getAttribute("usuario");
        if (usuarioLogueado != null) {
            add(layoutMenu);
        } else {
            add(layoutLogin);
        }
    }

    private void armarSeccionLogin() {
        layoutLogin.setSizeFull();
        layoutLogin.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutLogin.setAlignItems(Alignment.CENTER);

        H1 tituloLogin = new H1("Ingreso al Sistema");
        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.addLoginListener(e -> {
            usuarioService.login(e.getUsername(), e.getPassword()).ifPresentOrElse(usuario -> {
                VaadinSession.getCurrent().setAttribute("usuario", usuario);
                remove(layoutLogin);
                add(layoutMenu);
                Notification.show("¡Bienvenido, " + usuario.getNombreCompleto() + "!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }, () -> {
                loginForm.setError(true);
            });
        });

        Button btnRegistrarse = new Button("Crear una cuenta nueva");
        btnRegistrarse.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnRegistrarse.addClickListener(e -> abrirVentanaRegistro());

        layoutLogin.add(tituloLogin, loginForm, btnRegistrarse);
    }

    private void abrirVentanaRegistro() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Registro de Nuevo Usuario");

        TextField txtNombre = new TextField("Nombre Completo");
        TextField txtEmail = new TextField("Correo Electrónico");
        TextField txtUser = new TextField("Nombre de Usuario");
        PasswordField txtPass = new PasswordField("Contraseña");

        VerticalLayout formRegistro = new VerticalLayout(txtNombre, txtEmail, txtUser, txtPass);
        formRegistro.setPadding(false);
        dialog.add(formRegistro);

        Button btnGuardar = new Button("Registrarme", e -> {
            if (txtNombre.isEmpty() || txtEmail.isEmpty() || txtUser.isEmpty() || txtPass.isEmpty()) {
                Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                Usuario nuevo = new Usuario();
                nuevo.setNombreCompleto(txtNombre.getValue());
                nuevo.setEmail(txtEmail.getValue());
                nuevo.setUsername(txtUser.getValue());
                nuevo.setPassword(txtPass.getValue());

                usuarioService.save(nuevo);
                Notification.show("Usuario registrado. Ya podés loguearte.", 4000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Error: El usuario o email ya existen.", 4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button btnCancelar = new Button("Cancelar", e -> dialog.close());

        dialog.getFooter().add(btnCancelar, btnGuardar);
        dialog.open();
    }

    private void armarSeccionMenu() {
        layoutMenu.setSizeFull();
        layoutMenu.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutMenu.setAlignItems(Alignment.CENTER);
        layoutMenu.setSpacing(true);

        Button btnLogout = new Button("Cerrar Sesión", VaadinIcon.SIGN_OUT.create());
        btnLogout.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        btnLogout.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("usuario", null);
            remove(layoutMenu);
            add(layoutLogin);
            UI.getCurrent().getPage().reload(); 
        });

        H1 titulo = new H1("Sistema de Gestión de Terceros");
        Paragraph subtitulo = new Paragraph("Seleccione el módulo al que desea ingresar para comenzar a trabajar:");
        subtitulo.getElement().getStyle().set("color", "var(--lumo-secondary-text-color)");

        VerticalLayout cardFacultad = crearTarjeta("Gestión de Facultades", VaadinIcon.INSTITUTION.create(), "#1677ff", "facultad");
        VerticalLayout cardTercero = crearTarjeta("Gestión de Terceros", VaadinIcon.USERS.create(), "#52c41a", "tercero");
        VerticalLayout cardFactura = crearTarjeta("Gestión de Facturas", VaadinIcon.FILE_TEXT.create(), "#722ed1", "factura");
        
        // CORREGIDO DEFINITIVAMENTE: Apunta a "pagos" en plural para coincidir con tu ruta disponible
        VerticalLayout cardPago = crearTarjeta("Gestión de Pagos", VaadinIcon.MONEY.create(), "#fa8c16", "pagos");

        HorizontalLayout contenedorOpciones = new HorizontalLayout(cardFacultad, cardTercero, cardFactura, cardPago);
        contenedorOpciones.setSpacing(true);
        contenedorOpciones.setPadding(true);

        layoutMenu.add(btnLogout, titulo, subtitulo, contenedorOpciones);
    }

    private VerticalLayout crearTarjeta(String texto, com.vaadin.flow.component.icon.Icon icono, String colorHex, String ruta) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("280px");
        card.setHeight("220px");
        card.setAlignItems(Alignment.CENTER);
        card.setJustifyContentMode(JustifyContentMode.CENTER);
        card.getElement().getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-size-m)")
                .set("background-color", "var(--lumo-base-color)");

        H1 iconoH1 = new H1(icono);
        iconoH1.getElement().getStyle().set("color", colorHex);
        
        Button btn = new Button(texto);
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btn.getElement().getStyle().set("background-color", colorHex);
        btn.addClickListener(e -> UI.getCurrent().navigate(ruta));

        card.add(iconoH1, btn);
        return card;
    }
}