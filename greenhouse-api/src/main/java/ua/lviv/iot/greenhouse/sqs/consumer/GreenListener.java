package ua.lviv.iot.greenhouse.sqs.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import ua.lviv.iot.greenhouse.dto.air_sensor.AirSensorDTO;
import ua.lviv.iot.greenhouse.dto.general_sensor.GeneralSensorDTO;
import ua.lviv.iot.greenhouse.dto.luminosity_sensor.LuminositySensorDTO;
import ua.lviv.iot.greenhouse.dto.soil_sesnor.SoilSensorDTO;
import ua.lviv.iot.greenhouse.models.AirSensor;
import ua.lviv.iot.greenhouse.models.GeneralSensor;
import ua.lviv.iot.greenhouse.models.LuminositySensor;
import ua.lviv.iot.greenhouse.models.SensorType;
import ua.lviv.iot.greenhouse.models.SoilSensor;
import ua.lviv.iot.greenhouse.services.AirSensorService;
import ua.lviv.iot.greenhouse.services.GeneralSensorService;
import ua.lviv.iot.greenhouse.services.LuminositySensorService;
import ua.lviv.iot.greenhouse.services.SoilSensorService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GreenListener {

    private final LuminositySensorService luminositySensorService;
    private final AirSensorService airSensorService;
    private final GeneralSensorService generalSensorService;
    private final SoilSensorService soilSensorService;

    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void loadMessageFromSQS(String message, @Header("SENSOR_TYPE") String sensorType) {
        log.info("Message received: {}", message);
        final ObjectMapper MAPPER = new ObjectMapper();
        try {
            switch (SensorType.valueOf(sensorType)) {
                case AIR_SENSOR:
                    AirSensorDTO airSensorDTO = MAPPER.readValue(message, AirSensorDTO.class);
                    log.info("Received message {} of type {}", airSensorDTO, sensorType);
                    AirSensor airSensorData = airSensorService.createSensorData(airSensorDTO);
                    log.info("Created object {}", airSensorData);
                    break;
                case SOIL_SENSOR:
                    SoilSensorDTO soilSensorDTO = MAPPER.readValue(message, SoilSensorDTO.class);
                    log.info("Received message {} of type {}", soilSensorDTO, sensorType);
                    SoilSensor soilSensorData = soilSensorService.createSensorData(soilSensorDTO);
                    log.info("Created object {}", soilSensorData);
                    break;
                case GENERAL_SENSOR:
                    GeneralSensorDTO generalSensorDTO = MAPPER.readValue(message, GeneralSensorDTO.class);
                    log.info("Received message {} of type {}", generalSensorDTO, sensorType);
                    GeneralSensor generalSensorData = generalSensorService.createSensorData(generalSensorDTO);
                    log.info("Created object {}", generalSensorData);
                    break;
                case LUMINOSITY_SENSOR:
                    LuminositySensorDTO luminositySensorDTO = MAPPER.readValue(message, LuminositySensorDTO.class);
                    log.info("Received message {} of type {}", luminositySensorDTO, sensorType);
                    LuminositySensor luminositySensorData = luminositySensorService.createSensorData(luminositySensorDTO);
                    log.info("Created object {}", luminositySensorData);
                    break;
                default:
                    log.error("Wrong SENSOR_TYPE variable");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
