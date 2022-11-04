package ua.lviv.dataart;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.lviv.dataart.dto.AirSensorDTO;
import ua.lviv.dataart.dto.GeneralSensorDTO;
import ua.lviv.dataart.dto.LuminositySensorDTO;
import ua.lviv.dataart.dto.SensorType;
import ua.lviv.dataart.dto.SoilSensorDTO;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class MessageSender {

    private static final Logger LOGGER = Logger.getLogger("MessageLogger");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final AirSensorDTO AIR_SENSOR_DTO = new AirSensorDTO(LocalDateTime.now(), 76.7, 31.1);
    private static final GeneralSensorDTO GENERAL_SENSOR_DTO = new GeneralSensorDTO(25.6, 45.6, 98d, 45.5, LocalDateTime.now());
    private static final LuminositySensorDTO LUMINOSITY_SENSOR_DTO = new LuminositySensorDTO(LocalDateTime.now(), 34.5);
    private static final SoilSensorDTO SOIL_SENSOR_DTO = new SoilSensorDTO(LocalDateTime.now(), 78.8, 45d);

    private static String airSensorSerialized;
    private static String luminositySensorSerialized;
    private static String generalSensorSerialized;
    private static String soilSensorSerialized;

    static {
        try {
            airSensorSerialized = MAPPER.writeValueAsString(AIR_SENSOR_DTO);
            LOGGER.log(Level.INFO, "Serialized AIR_SENSOR_DTO into JSON: {0}", airSensorSerialized);
            luminositySensorSerialized = MAPPER.writeValueAsString(LUMINOSITY_SENSOR_DTO);
            LOGGER.log(Level.INFO, "Serialized LUMINOSITY_SENSOR_DTO into JSON: {0}", luminositySensorSerialized);
            generalSensorSerialized = MAPPER.writeValueAsString(GENERAL_SENSOR_DTO);
            LOGGER.log(Level.INFO, "Serialized GENERAL_SENSOR_DTO into JSON: {0}", generalSensorSerialized);
            soilSensorSerialized = MAPPER.writeValueAsString(SOIL_SENSOR_DTO);
            LOGGER.log(Level.INFO, "Serialized SOIL_SENSOR_DTO into JSON: {0}", soilSensorSerialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        final SendMessageRequest sendMessageRequest = new SendMessageRequest();
        String amazonSqsUrl = System.getenv("AMAZON_SQS_URL");
        LOGGER.log(Level.INFO, "Queue from env variables: {0}", amazonSqsUrl);
        sendMessageRequest.withQueueUrl(amazonSqsUrl);
        String sensorType = System.getenv("SENSOR_TYPE");
        LOGGER.log(Level.INFO, "SENSOR_TYPE from env variables: {0}", sensorType);
        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
        messageAttributeValue.setStringValue(sensorType);
        messageAttributeValue.setDataType("String");
        sendMessageRequest.addMessageAttributesEntry("SENSOR_TYPE", messageAttributeValue);
        switch (SensorType.valueOf(sensorType)) {
            case AIR_SENSOR:
                sendMessageRequest.withMessageBody(airSensorSerialized);
                break;
            case SOIL_SENSOR:
                sendMessageRequest.withMessageBody(soilSensorSerialized);
                break;
            case GENERAL_SENSOR:
                sendMessageRequest.withMessageBody(generalSensorSerialized);
                break;
            case LUMINOSITY_SENSOR:
                sendMessageRequest.withMessageBody(luminositySensorSerialized);
                break;
            default:
                LOGGER.log(Level.SEVERE, "Wrong SENSOR_TYPE variable");
        }
        String sendingMessageDelayInMillis = System.getenv("SENDING_MESSAGE_DELAY_IN_MILLIS");
        LOGGER.log(Level.INFO, "SENDING_MESSAGE_DELAY_IN_MILLIS from env variables: {0}", sendingMessageDelayInMillis);
        while (true) {
            sqs.sendMessage(sendMessageRequest);
            LOGGER.log(Level.INFO, "Sent message: {0}", sendMessageRequest);
            try {
                Thread.sleep(Long.parseLong(sendingMessageDelayInMillis));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
