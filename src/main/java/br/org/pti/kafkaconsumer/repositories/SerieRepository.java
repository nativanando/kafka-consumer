/**
 * 
 */
package br.org.pti.kafkaconsumer.repositories;

import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.pti.kafkaconsumer.domain.Serie;

/**
 * @author fernando.luiz
 *
 */
@Repository
public interface SerieRepository extends CassandraRepository<Serie, UUID> {	
}
