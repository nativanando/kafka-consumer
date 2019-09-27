/**
 * Configuration component to set up a kafka client to subscribe topics.
 */
package br.org.pti.kafkaconsumer.config;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.Session;

import br.org.pti.kafkaconsumer.domain.Serie;
import br.org.pti.kafkaconsumer.services.SerieService;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		System.out.println("Received Messasge in group foo: " + message);
		JSONParser parser = new JSONParser();
		JSONObject messagereceived = (JSONObject) parser.parse(message);
		sendMessageCassandra(messagereceived);
	}
	
	public void sendMessageCassandra(JSONObject messageReceived) {
		System.out.println(messageReceived.get("device_id"));
		
		UUID uniqueID = UUID.fromString((String) messageReceived.get("device_id"));
		String dataName = (String) messageReceived.get("data_name");
		Double temperature = (Double) messageReceived.get("data_value");
		Date currentTimestamp = new Date();
		currentTimestamp = new Timestamp(currentTimestamp.getTime());
		JSONObject message = new JSONObject();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		Serie serie = new Serie(uniqueID, dataName, currentTimestamp, year, temperature);
		System.out.println(serie.toString());
		serieService.save(serie);
	}

}
