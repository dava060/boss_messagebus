package com.moretv.producer.impl;

import java.io.Serializable;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.moretv.producer.IMessageProducer;

public class MessageProducerImpl implements IMessageProducer{
	private RabbitTemplate rabbitTemplate;
	public void send(Object object) {
		if(isSerializable(object)) this.rabbitTemplate.convertAndSend(object);
	}
	
	public void send(String exchange, String routingKey, Object object) {
		if(isSerializable(object)) this.rabbitTemplate.convertAndSend(exchange, routingKey, object);
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	private Boolean isSerializable(Object mess){
		if(!(mess instanceof Serializable))
			try {
				throw new Exception("消息对象需要序列化");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return true;
	}
}
