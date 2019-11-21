package com.example.demorediscounter.service;

import com.example.demorediscounter.entity.Counter;
import com.example.demorediscounter.repository.CounterRepository;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CounterService {
    @Autowired
    CounterRepository counterRepository;

//    @Bean(destroyMethod = "shutdown")
//    RedissonClient redissonClient1() {
//        Config config = new Config();
//        config.useClusterServers()
////                .setAddress("localhost:30002");
//                .addNodeAddress("redis://127.0.0.1:30001" ,"redis://127.0.0.1:30002")
//                .addNodeAddress("redis://127.0.0.1:30003", "redis://127.0.0.1:30004")
//                .addNodeAddress("redis://127.0.0.1:30005", "redis://127.0.0.1:30006");
//        return  Redisson.create(config);
//    }
//
//    @Autowired


    RedissonClient redissonClient = Redisson.create();
    long count =0;


    public long setCounterAtomic(){

        System.out.println("get counter");
        RAtomicLong atomicLong = redissonClient.getAtomicLong("counter");
        return atomicLong.incrementAndGet();
    }

    public long setCounter(){
        return count++;
    }

    @Transactional
    public void saveCounterToDB(){
        Optional<Counter> counters = counterRepository.findById(1L);
        Counter counter = new Counter();
        counter.setId(counters.get().getId());

        if(count == 0){
            RAtomicLong counterRedis = redissonClient.getAtomicLong("counter");
            counter.setCounterNow(counterRedis.get());

        }
        else
            counter.setCounterNow(count);
        counterRepository.save(counter);
    }


    @Scheduled(fixedDelay = 1000*5)
    public void saveCounterToDBAfter5Minutes(){
        System.out.println("Save counter to db");
        saveCounterToDB();
    }


    public  long testDownCluster(){
        Config config = new Config();
        config.useClusterServers()
//                .setAddress("localhost:30002");
                .addNodeAddress("localhost:12001" ,"localhost:12002")
                .addNodeAddress("localhost:12003", "localhost:12004")
                .addNodeAddress("localhost:12005", "localhost:12006");
        RedissonClient redissonClient1 = Redisson.create(config);

//        RBucket<String> bucket = redissonClient1.getBucket("stringObject");
//        bucket.set("Rommel is the object value");
//        RMap<String, String> map = redissonClient1.getMap("theMap");
//        map.put("mapKey", "Risa is map value");
//        String objValue = bucket.get();
//        System.out.println("The object value is: " + objValue);
//        String mapValue = map.get("mapKey");
//        System.out.println("The map value is: " + mapValue);

        System.out.println("get counter");
        RAtomicLong atomicLong = redissonClient1.getAtomicLong("counter");
        long number = atomicLong.incrementAndGet();

        redissonClient1.shutdown();
        return number ;
    }


}
