package com.ceyloncab.tripmgtservice.application.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.producer.bootstrapServers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Object> multiTypeProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //TODO
        configProps.put(JsonSerializer.TYPE_MAPPINGS,
                "confirmPickup:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.ConfirmPickupPublishResponse," +
                        "allVehiclePrice:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.TripPriceVehiclePublishResponse," +
                        "acceptTrip:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.AcceptTripPublishResponse," +
                        "updateTrip:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.UpdateTripPublishResponse," +
                        "endTrip:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.EndTripPublishResponse");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(multiTypeProducerFactory());
    }
}