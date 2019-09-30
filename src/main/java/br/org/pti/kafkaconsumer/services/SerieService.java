/**
 * 
 */
package br.org.pti.kafkaconsumer.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.pti.kafkaconsumer.domain.Serie;
import br.org.pti.kafkaconsumer.repositories.SerieRepository;

/**
 * @author fernando.luiz
 *
 */
@Service
public class SerieService {

	@Autowired
	SerieRepository serieRepository;

	/**
	 * @param message
	 * @throws ParseException
	 */
	public void save(String messageReceived) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject message = (JSONObject) parser.parse(messageReceived);

		Long uniqueID = (Long) message.get("device_id");
		String dataName = (String) message.get("data_name");
		Double temperature = (Double) message.get("data_value");
		Timestamp currentTimestamp = new Timestamp((long) message.get("data_release_date"));
		Long year = (Long) message.get("data_release_year");

		Serie serie = new Serie(uniqueID, dataName, year, currentTimestamp, temperature);

		serieRepository.save(serie);
	}

}
