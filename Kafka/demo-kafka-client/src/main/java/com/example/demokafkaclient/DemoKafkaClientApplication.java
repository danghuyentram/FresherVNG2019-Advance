package com.example.demokafkaclient;

import com.example.demokafkaclient.constant.IKafkaConstants;
import com.example.demokafkaclient.consumer.ConsumerCreator;
import com.example.demokafkaclient.producer.ProducerCreator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DemoKafkaClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoKafkaClientApplication.class, args);
		runProducer();
//		runConsumer();
	}

	static void runConsumer(){
		Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

		int noMessageFound = 0;

		while(true){
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000L);

			if(consumerRecords.count() == 0){
				noMessageFound ++;
				if(noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
					break;
				else
					continue;
			}

			// print each record
			consumerRecords.forEach(record -> {
//				System.out.println("Record key "+record.key());
//				System.out.println("Record value "+record.value());
//				System.out.println("Record partition "+record.partition());
//				System.out.println("Record offset "+ record.offset());
				System.out.println("\n Record: "+record.toString());
//				record.toString();
			});

			consumer.commitAsync();
		}
		consumer.close();
	}

	static void runProducer(){
		Producer<Long, String> producer = ProducerCreator.createProducer();

		for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {

			final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,Long.valueOf(index),"This is record " + index);
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		}
	}

}
