package eduardocff.sicredi.challenge.rabbitMQ;

import eduardocff.sicredi.challenge.rabbitMQ.config.ActiveMQServerConfiguration.JmsConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    @JmsListener(destination = JmsConfiguration.QUEUE)
    public void listen(@Payload String resultMessage,
                       @Headers MessageHeaders headers, Message message) {

        log.info(String.format("Voting result received %s", resultMessage));
    }
}
