package com.ceyloncab.tripmgtservice.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kafka.consumer")
@Getter
@Setter
public class KafkaConsumerYamlConfig {
    private String bootstrapServers;
    private String groupId;
    private Boolean enableAutoCommit;
    private Integer autoCommitInterval;
    private String offsetCommitMethod;
    private Integer sessionTimeoutTime;
    private Integer maxPollInterval;
    private Integer maxPollRecords;
    private String topicTrip;
    private String topicSelectVehicle;
}
