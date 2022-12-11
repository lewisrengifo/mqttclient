package pucp.tesis.msmqttclient.infraestructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pucp.tesis.msmqttclient.infraestructure.document.CO2;

@Repository
public interface CO2Repository extends ReactiveMongoRepository<CO2, String> {
}
