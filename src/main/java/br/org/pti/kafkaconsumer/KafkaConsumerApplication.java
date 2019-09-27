/**
 * Kafka consumer service. The goal is to build a kafka client to subscribe topics and to send messages for an external database 
 */
package br.org.pti.kafkaconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fernando.luiz
 *
 */
@SpringBootApplication
public class KafkaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerApplication.class, args);
	}

}
