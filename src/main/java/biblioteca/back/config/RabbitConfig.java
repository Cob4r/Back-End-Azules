package biblioteca.back.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    //️⃣ Nombre de las colas
    public static final String QUEUE_EQUIPOS = "equiposQueue";
    public static final String QUEUE_CONTACTO = "contactoQueue";

    //️⃣ Cola de Equipos (ya la tenías)
    @Bean
    public Queue equiposQueue() {
        return new Queue(QUEUE_EQUIPOS, true);
    }

    //️⃣ Cola de Contacto (👈 la que faltaba)
    @Bean
    public Queue contactoQueue() {
        return new Queue(QUEUE_CONTACTO, true);
    }
}
