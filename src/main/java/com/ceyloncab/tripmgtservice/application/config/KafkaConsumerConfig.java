package com.ceyloncab.tripmgtservice.application.config;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.ConfirmPickup;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.EndTrip;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.TripPriceForAllVehicles;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.UpDateStatusOnTrip;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private KafkaConsumerYamlConfig consumerYamlConfig;
    @Bean
    public RecordMessageConverter multiTypeConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        //TODO
        typeMapper.addTrustedPackages("com.ceyloncab.tripmgtservice.domain.entity.dto.kafka","*");
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("confirmPickup", ConfirmPickup.class);
        mappings.put("tripPriceForAllVehicles", TripPriceForAllVehicles.class);
        mappings.put("acceptTrip", UpDateStatusOnTrip.class);
        mappings.put("endTrip", EndTrip.class);
        typeMapper.setIdClassMapping(mappings);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
    @Bean
    public ConsumerFactory<String, Object> multiTypeConsumerFactory() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerYamlConfig.getBootstrapServers());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerYamlConfig.getEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerYamlConfig.getAutoCommitInterval());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerYamlConfig.getSessionTimeoutTime());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerYamlConfig.getMaxPollInterval());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerYamlConfig.getMaxPollRecords());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerYamlConfig.getOffsetCommitMethod());// earliest
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerYamlConfig.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "confirmPickup:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.ConfirmPickup," +
                "tripPriceForAllVehicles:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.TripPriceForAllVehicles," +
                "acceptTrip:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.UpDateStatusOnTrip," +
                "endTrip:com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.EndTrip");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "multiTypeKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> multiTypeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(multiTypeConsumerFactory());
        factory.setMessageConverter(multiTypeConverter());
        return factory;
    }
}