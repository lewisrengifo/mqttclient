package pucp.tesis.msmqttclient.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import pucp.tesis.msmqttclient.infraestructure.document.CO2;
import pucp.tesis.msmqttclient.infraestructure.document.Humidity;
import pucp.tesis.msmqttclient.infraestructure.document.Temperature;
import pucp.tesis.msmqttclient.infraestructure.repository.CO2Repository;
import pucp.tesis.msmqttclient.infraestructure.repository.HumidityRepository;
import pucp.tesis.msmqttclient.infraestructure.repository.TemperatureRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class MqttBeans {

  @Autowired TemperatureRepository temperatureRepository;

  @Autowired HumidityRepository humidityRepository;

  @Autowired CO2Repository co2Repository;

  public MqttPahoClientFactory mqttPahoClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[] {"tcp://107.22.128.97:1883"});
    options.setCleanSession(false);
    factory.setConnectionOptions(options);
    return factory;
  }

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageProducer inbound() {
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(
            "Spring Boot",
            mqttPahoClientFactory(),
            "sensor/temperatura",
            "sensor/humedad",
            "sensor/co2");
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(2);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public MessageHandler handler() {
    return new MessageHandler() {
      @Override
      public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
        System.out.println("topico leido: " + topic);
        Temperature temperature = new Temperature();
        Humidity humidity = new Humidity();
        CO2 co2 = new CO2();
        Calendar timestampDate = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT-5");
        timestampDate.setTimeZone(timeZone);

        String hora = String.valueOf(timestampDate.get(Calendar.HOUR));

        String minutos = String.valueOf(timestampDate.get(Calendar.MINUTE));

        String seconds = String.valueOf(timestampDate.get(Calendar.SECOND));

        if (topic.equals("sensor/temperatura")) {
          System.out.println("Tópico Elegido: " + topic);
          System.out.println(message.getPayload());

          System.out.println("************ 1");
          String data = String.valueOf(message.getPayload());
          try {

            System.out.println("timestamp: ");
            temperature.setHour(hora);
            temperature.setMinute(minutos);
            temperature.setSecond(seconds);
            temperature.setTemperatureSensed(data);
            temperatureRepository.save(temperature).subscribe();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else if (topic.equals("sensor/humedad")) {
          System.out.println("Tópico Elegido: " + topic);
          System.out.println(message.getPayload());

          System.out.println("************ 2");
          String data = String.valueOf(message.getPayload());
          humidity.setHour(hora);
          humidity.setMinute(minutos);
          humidity.setSecond(seconds);
          humidity.setHumiditySensed(data);
          humidityRepository.save(humidity).subscribe();
        } else if (topic.equals("sensor/co2")) {
          System.out.println("Tópico Elegido: " + topic);
          System.out.println(message.getPayload());

          System.out.println("************ 3");
          String data = String.valueOf(message.getPayload());
          co2.setHour(hora);
          co2.setMinute(minutos);
          co2.setSecond(seconds);
          co2.setCoSensed(data);
          co2Repository.save(co2).subscribe();
        }
      }
    };
  }

  @Bean
  public MessageChannel mqttOutBoundChannel() {
    return new DirectChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel")
  public MessageHandler mqttOutbound() {
    MqttPahoMessageHandler messageHandler =
        new MqttPahoMessageHandler("serverOut", mqttPahoClientFactory());
    messageHandler.setAsync(true);
    // messageHandler.setDefaultTopic("sensor/temperatura");
    return messageHandler;
  }
}
