/**
 * 
 */
package br.org.pti.kafkaconsumer.services;

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
	
	public void save(Serie serie) {
		serieRepository.save(serie);
	}

}
