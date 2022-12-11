package pucp.tesis.msmqttclient.infraestructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pucp.tesis.msmqttclient.infraestructure.document.Temperature;

@Repository
public interface TemperatureRepository extends ReactiveMongoRepository<Temperature, String> {}
