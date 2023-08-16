package com.ceyloncab.tripmgtservice.domain.boundary;

public interface KafkaProducerService {

    void publishMsgToTopic(String topic, Object msg);
}
