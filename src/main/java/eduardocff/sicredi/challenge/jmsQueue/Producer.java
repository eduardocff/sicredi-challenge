package eduardocff.sicredi.challenge.jmsQueue;

import eduardocff.sicredi.challenge.jmsQueue.config.ActiveMQServerConfiguration.JmsConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    private final JmsTemplate jmsTemplate;

    public void send(String message) {
        jmsTemplate.convertAndSend(JmsConfiguration.QUEUE, message);
    }
}
