package com.ceyloncab.tripmgtservice.external.serviceimpl;

import com.ceyloncab.tripmgtservice.domain.boundary.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void publishMsgToTopic(String topic, Object msg){
        try {
            kafkaTemplate.send(topic,msg);
        }catch (Exception ex){
            log.error("Error occurred in publish message to kafkaTopic:{}",topic);
        }
    }
}
