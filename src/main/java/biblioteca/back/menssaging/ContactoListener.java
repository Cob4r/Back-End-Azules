package biblioteca.back.menssaging;

import biblioteca.back.config.RabbitConfig;
import biblioteca.back.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactoListener {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitConfig.QUEUE_CONTACTO)
    public void recibirMensaje(String mensaje) {

        System.out.println("📥 Nuevo mensaje recibido: " + mensaje);

        
        emailService.enviarCorreo(
                "sebi.escob@gmail.com",
                "Nuevo Mensaje desde Contacto",
                mensaje
        );
    }
}
