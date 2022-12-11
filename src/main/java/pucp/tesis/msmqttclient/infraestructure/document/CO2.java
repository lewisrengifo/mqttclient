package pucp.tesis.msmqttclient.infraestructure.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("co2")
public class CO2 {
  private String coSensed;
  private String timestamp;
}
