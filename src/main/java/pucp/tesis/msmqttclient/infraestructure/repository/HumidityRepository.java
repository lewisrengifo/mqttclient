package pucp.tesis.msmqttclient.infraestructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pucp.tesis.msmqttclient.infraestructure.document.Humidity;

@Repository
public interface HumidityRepository extends ReactiveMongoRepository<Humidity, String> {
}
