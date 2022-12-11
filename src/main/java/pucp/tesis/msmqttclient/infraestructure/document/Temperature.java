package pucp.tesis.msmqttclient.infraestructure.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("tempSensed")
public class Temperature {
  private String temperatureSensed;
  private String hour;
  private String minute;
  private String second;
}
