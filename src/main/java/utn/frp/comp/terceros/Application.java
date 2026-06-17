package utn.frp.comp.terceros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.spring.annotation.EnableVaadin; // Asegúrate de que se importe

@SpringBootApplication(scanBasePackages = "utn.frp.comp.terceros")
@EnableVaadin("utn.frp.comp.terceros") // <--- Esto le dice a Spring: "Che, escaneá las rutas de Vaadin acá"
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
