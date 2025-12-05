package biblioteca.back.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_EQUIPOS = "equipos_queue";

    @Bean
    public Queue equiposQueue() {
        return new Queue(QUEUE_EQUIPOS, true);
    }
}
