/**
 * 
 */
package br.org.pti.kafkaconsumer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author fernando.luiz
 *
 */
@Table("data")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class Serie implements Serializable {

	private static final long serialVersionUID = 1913186375912584021L;
	  
	@PrimaryKeyColumn(name = "device_id", type = PrimaryKeyType.PARTITIONED)
	private UUID id;
	
	@PrimaryKeyColumn(name = "data_name", type = PrimaryKeyType.PARTITIONED)
	private String dataName;
	
	@PrimaryKeyColumn(name = "data_release_date", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	@CassandraType(type = Name.TIMESTAMP)
	Date dataReleaseDate;
	
	@Column("data_release_year")
	private int dataReleaseYear;
	
	@Column("data_value")
	private Double dataValue;
	
}
