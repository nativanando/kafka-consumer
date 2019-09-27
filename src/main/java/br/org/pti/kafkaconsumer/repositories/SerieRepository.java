/**
 * 
 */
package br.org.pti.kafkaconsumer.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import br.org.pti.kafkaconsumer.domain.Serie;

/**
 * @author fernando.luiz
 *
 */
@Repository
public interface SerieRepository extends CassandraRepository<Serie, UUID> {	
	
}
