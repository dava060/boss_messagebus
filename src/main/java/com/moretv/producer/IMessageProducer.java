package com.moretv.producer;

public interface IMessageProducer {
	public void send(Object object);
	public void send(String exchange,String routingKey,Object object);
}
