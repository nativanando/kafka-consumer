/**
 * Configuration component to set up a kafka client to subscribe topics.
 */
package br.org.pti.kafkaconsumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import br.org.pti.kafkaconsumer.services.SerieService;

/**
 * @author fernando.luiz
 *
 */

@EnableKafka
@Configuration
public class ReceiverConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Autowired
	SerieService serieService;

	/**
	 * This bean is responsible to set up the consumer and to provide some rules to
	 * control the offset and the sink time with new topics
	 * 
	 * @return ConsumerFactory
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, 5000);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return new DefaultKafkaConsumerFactory<>(props);
	}

	/**
	 * @return ConcurrentKafkaListenerContainerFactory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * Assign topics with the pattern sensor_ sufix
	 * 
	 * @param message
	 * @throws ParseException
	 */
	@KafkaListener(topicPattern = "${app.kafka.topic.prefix.expression}", groupId = "receivers")
	public void listen(String message) throws ParseException {
		serieService.save(message);
	}

}
