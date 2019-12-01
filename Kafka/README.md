# Kafka

# 1. What is kafka? Concepts?
## 1.1 What is kafka?
Apache Kafka là 1 distributed streaming platform

1 streaming platform có 3 khả năng chính:
- Publish và subscribe các stream record, giống với message queue hay enterprise mesaging system
- Lưu trữ các stream record với fault-tolerant 1 cách kiên cố
- Process các stream record khi nó đến

Kafka thường được dùng cho 2 broad classes của application:
- Build real-time streaming data pipeline để get data giữa systems hoặc application 1 cách tin cậy
- Buld real-time streaming application để transform hoặc react tới stream data

## 1.2 Concepts
Một số concept:
- Kafka được run như 1 cluster trên 1 hoặc nhiều server nên có thể span multiple datacenter
- Kafka cluster lưu trữ các stream records trong các category gọi là topic
- Mỗi record bao gồm 1 key - 1 value - 1 timestamp


![](https://kafka.apache.org/23/images/kafka-apis.png)

Kafka gồm 5 core API:
- Producer API: cho phép 1 application publish 1 stream các record tới 1 hoặc nhiều Kafka topic
- Consumer API: cho phép 1 application subscribe tới 1 hoặc nhiều topic và process stream các record mà producer đưa tới
- Streams API: cho phép 1 application hoạt động như 1 stream processor, consuming 1 input stream từ 1 hoặc nhiều topic và producing 1 output stream tới 1 hay nhiều ouput topic, chuyển dổi hiệu quả các input streams thành các output streams
- Connector API: cho phép build và run tái sử dụng lại producer và consumer connect Kafka topic còn tồn tại trong applications và data systems. Vd: 1 connector liên quan tới database có thể capture mọi thay đổi trong 1 table
- AdminClient API: cho phép quản lí và inspect các topic, các broker và các Kafka object khác

Trong Kafka, communication giữa client và server là high-performance, dùng TCP protocol. 

### 1.2.1 Producer API
Producer APi cho phép application gửi stream data tới các topic trong Kafka cluster

[Javadoc](https://kafka.apache.org/23/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html)

Dùng producer api, add maven dependency sau:
```
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 1.2.2 Consumer API
Consumer API cho phép application read stream data từ các topic trong Kafka cluster

[Javadoc](https://kafka.apache.org/23/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html)

Dùng consumer api, add maven dependency sau:
```
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 1.2.3 Streams API
Stream API cho phép chuyển stream data từ input topic sang thành output topic
[Javadoc](https://kafka.apache.org/23/javadoc/index.html?org/apache/kafka/streams/KafkaStreams.html)

https://kafka.apache.org/23/documentation/streams/

```
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-streams</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 1.2.4 Connect APi
Connect APi cho phép implement các connector liên tục pull từ 1 số source data system vào Kafka và push từ Kafka tới 1 số sink data system
[Javadoc](https://kafka.apache.org/23/javadoc/index.html?org/apache/kafka/connect)

### 1.2.5 Topic và log
1 topic là 1 category hoặc feed name là nơi mà các record được pusblish tới. Topic Kafka luôn là multi-subscriber: 1 topic có thể có 0, 1 hoặc nhiều consumer subscribe tới data được lưu trên đó

Mỗi topic, Kafka cluster maintain 1 partitioned log như sau:
![](https://kafka.apache.org/23/images/log_anatomy.png)

Mỗi partition đều có thứ tự, trình tự các record không thay đổi và luôn được nối vào tạo thành 1 structured commit log. Các record trong các partitions được assigned 1 sequential id number gọi là offset, nó là duy nhất và phân biệt với các record khác trong cùng 1 partition

Kafka cluster durably persists tất cả published records - kể cả khi nó được consumed hay không - dùng 1 configurable retention period. 
Vd: nếu retention policy được set 2 ngày, và sau 2 ngày 1 record được publish, sẵn sàng để được consum, sau đó thì nó sẽ bị xóa khỏi space. Kafka performance thực sự không đổi liên quan đến datasize nên việc lưu data 1 thời gian dài không có vấn đề gì

![](https://kafka.apache.org/23/images/log_consumer.png)

Thực tế, metadata duy nhất được giữ lại trên mỗi consumer basis là offset hoặc position của consumer đó trên log. 
- Offset này được controlled bởi consumer: thông thường, 1 các offset của consumer sẽ tăng tuyến tính khi nó read các record, nhưng thực tế, khi position được controll bởi consumer, nó có thể consume các records theo bất kì thứ tự nào nó thích. 
Vd: 1 consumer có thể reset older ofset để reprocess data cũ hoặc skip ahead tới record gần đây nhất và bắt đầu consuming từ "bây giờ"

Vì thế các Kafka consumers có thể tới và đi mà không ảnh hưởng tới các cluster hoặc consumer khác. 

Các partition trên log có những mực đích sau:
- Cho phép log scale dựa vào size fit với 1 single server. Mỗi cá nhân partition phải fit với các server chứa nó, tuy nhiên, 1 topic có thể có nhiều partition nên nó có thể handle 1 lượng data tùy ý. 
- Các partition hoạt động như 1 đơn vị song song và thêm vào đó 1 ít

### 1.2.6 Distribution
Các partitions của 1 log được distributed trên các server của Kafka cluster với mỗi server handle data và request tới 1 shared partitions. Mỗi partition được replicated thông qua 1 configurable number của server cho việc fault tolenrance.

Mỗi partition có 1 server "leader" và 0 hoặc nhiều server "followers". 
- Leader handle mọi read và write request tới partition. 
- Follower passively replicate leader. 
Nếu leader fails, 1 trong số những follower sẽ auto trở thành leader mới.
Mỗi server là leader cho 1 số partition và là 1 follower cho một số khác nên load luôn được balance tốt bên trong 1 cluster

### 1.2.7 Producers
Producers publish data tới các topics. 1 producer chịu trách nhiệm chọn ra record nào sẽ được assign tới partition nào bên trong 1 topic. Việc này có thể được thực hiện theo kiểu round-robin chỉ đơn gian là để balance load hoặc nó có thể được thực hiện theo 1 số sematic partition function (dựa trên 1 số key trong record). 

### 1.2.8 Consumer
Các consumer tự dán nhãn nó với 1 consumer group name, và mỗi record được publish tới 1 topic sẽ được deliver tới 1 consumer instance trong mỗi subscribing consumer group. Các consumer instance có thể ở các process khác nhau trên các máy khác nhau

Nếu tất cả các consumer instance ở 1 consumer group, thì các record sẽ được load balance hiệu quả giữa các consumer instances

Nếu tất cả các consumer instance ở các consumer group khác nhau, thì mỗi record sẽ được broadcast tới tất cả các consumer process

![](https://kafka.apache.org/23/images/consumer-groups.png)

Vd: 2 server Kafka cluster bao gồm 4 partition (P0-P3) với 2 consumer groups. Consumer group A gồm 2 consumer instance, group B gồm 4 instance/

Các topic có 1 lượng nhỏ consumer group, mỗi consumer group ứng với 1 "logical subscriber". Mỗi group bao gồm các consumer instance để scalability và fault tolerance. Điều này không khác gì publish-subscribe semantics trong đó subcscriber là 1 cluster của các consumer thay vì 1 single process

Cách consumption (tiêu thụ) được implemented bởi Kafka bằng cách chia các partition trên log cho các consumer instance để mỗi instance đều là 1 consumer độc quyền của fair share các partition tại bất kì thời điểm nào. 

Process maintain membership trong 1 group được handle bởi Kaka protocol dynamically. 
- Nếu các instance mới join vào 1 group, no sẽ lấy 1 vài partition từ các member khác trong group. 
- Nếu 1 instance dies, các partition của nó sẽ được distrubuted tới các instance còn lại

Kafka chỉ cũng cấp tổng số order trên các record trong cùng 1 partition, chứ không phải giữa các partition khác nhau trong cùng 1 topic. Thứ tự mỗi partition cộng với khả năng partition dât theo key là đủ cho hầu hết các application. Tuy nhiên, nếu bạn yêu cầu tổng order các record thì có thể lấy được từ 1 topic mà chỉ có 1 partition, nghĩa là chỉ có 1 consumer process trong 1 consumer group

### 1.2.9 Commit log
Kafka có thể được dùng như 1 loại external commit-log cho distributed system. Log giúp replicate data giữa các node và hoạt động như 1 re-syncing mechanism để các failed node restore lại data của nó. 
Log compaction feature trong Kafka hỗ trợ điều đó.



#### Log compaction


#### Log compaction basic
Dưới đây là structure của 1 Kafka log với offset cho mỗi message
![](https://kafka.apache.org/23/images/log_cleaner_anatomy.png)


# 2. Kafka as queuing or publish-subscribe model?
Mesaging truyền thống có 2 model:
- Queuing
- Publish-subscribe

|             | Queing                                                                                       | Publish-subscribe                                                   |
|-------------|----------------------------------------------------------------------------------------------|---------------------------------------------------------------------|
|             | 1 pool các consumer có thể read từ 1 server và mỗi record gửi tới 1 trong số chúng           | mỗi record được broadcast tới tất cả consumer                       |
| Ưu điểm     | Cho phép chia việc processing data cho multiple consumer instance, giúp scale processing     | Cho phép broadcast data tới multiple process                        |
| Khuyết điểm | queues không phải là subscriber, 1 process chỉ read data 1 lần duy nhất và data đó sẽ mất đi | Không thể scale processing vì mọi message đều đi tới mọi subscriber |

Consumer group concept trong Kafka generalizes 2 concept.
- Như 1 queue các consumer group cho phép chia processing thành 1 tập các process (các member của consumer group)
- Như 1 publish-subscribe: Kafka cho phép broadcast các message tới multiple consumer groups

Ưu điểm của Kafka model là mọi topic đều có cả 2 tính chất này:
- Có thể scale processing
- Multi-subscriber

Queue truyền thống lưu trữ record theo thứ tự trên server và trao nó theo thứ tự cho multiple consumer. Tuy nhiên, mặc dù server gửi các record theo thứ tự, những các record này được deliver asynchronously tới consumer, nên chúng có thể tới các consumer không theo thứ tự. Có nghĩa là thứ tự các record bị mất khi consumption song song. Có thể dùng cơ chế consumer độc quyền chỉ cho phép 1 process được consume từ 1 queue, tuy nhiên sẽ không xử lí song song được.

Kafka đã khắc phục nhược điểm trên. Dựa vào cơ chế: song song - partition - trong các topics, Kafka vừa đảm bảo về thứ tự vừa load balancing cho 1 pool các consumer process. Điều này có được nhờ asign các partition trong 1 topic cho 1 consumer group, vậy nên mỗi partition sẽ được consume bởi 1 consumer duy nhất trong group. Bằng cách này, nó đảm bảo 1 consumer là 1 reader duy nhất của partition đó và consume data theo thứ tự. Trong khi đó các partition luôn được load balance giữa các consumer instance. Chú ý: tuy nhiên số consumer instance trong consumer group <= số partition

# 3. Kafka as a Storage System
Bất kì message queue nào cho phép publish message tách rời với việc consume nó là 1 storage system hiệu quả cho các in-flight message.

Data được write bởi Kafka thì được write xuống disk và được replicate để fault-tolerance. Kafka cho phép các producers chờ sự xác nhận để 1 write không được xem như hoàn thành cho tới khi nó được full replicate và được đảm bảo persist ngay cả khi server được write fail.

Disk structure Kafka có thể scale tốt, Kafka perfom như nhau kể cả có 50Kb hay 50TB persistent data trên server.


# 4. Demo
- setup kafka server locally
http://www.siddharthpandey.net/set-up-apache-kafka-locally/


# 5. Kafka with spring boot
https://www.confluent.io/blog/apache-kafka-spring-boot-application/

## 5.1 @KafkaListener annotation
https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/KafkaListener.html

Annotation đánh dấu 1 method là target của Kafka message listener trong 1 specified topic.

Processing @KafkaListener được thực hiện bằng cách registering [KafkaListenerAnnotationBeanPostProcessor](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/KafkaListenerAnnotationBeanPostProcessor.html), hoặc thuận tiện hơn là dùng @EnableKafka annotation

## 5.2 @EnableKafka annotation
https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/EnableKafka.html

@EnableKafka enable detection @KafkaListener annotation trong bất kì bean nào trong container mà Spring quản lý, dùng cho spring-core, còn spring-boot có org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration nên không cần annotation này.

# 6 Microservice
## 6.1 Đặc điểm
- Decoupling – Các service trong một hệ thống phần lớn được tách rời. Vì vậy, toàn bộ ứng dụng có thể dễ dàng được xây dựng, thay đổi và thu nhỏ.
- Componentization – Microservices được coi là các thành phần độc lập có thể dễ dàng thay thế và nâng cấp.
- Business Capabilities – mỗi một thành phần trong kiến trúc microservice rất đơn giản và tập trung vào một nhiệm vụ duy nhất.
- Autonomy – các lập trình viên hay các nhóm có thể làm việc độc lập với nhau trong quá trình phát triển.
- Continous Delivery – Cho phép phát hành phần mềm thường xuyên, liên tục.
Responsibility .
- Decentralized Governance – không có mẫu chuẩn hóa hoặc bất kỳ mẫu công nghệ nào. Được tự do lựa chọn các công cụ hữu ích tốt nhất để có thể giải quyết vấn đề.
- Agility – microservice hỗ trợ phát triển theo mô hình Agile.

## 6.2 Ưu điểm
- Mỗi microservice được chia nhỏ để tập trung vào 1 business function cụ thể hoặc 1 business requirement
- Microservice có thể được phát triển độc lập bởi 1 team nhỏ
- Microservice có tính loose-coupling, mỗi service là độc lập với nhau
- Microservice có thể phát triển với nhiều ngôn ngữ khác nhau
- 1 new member có thể join vào project dễ dàng
- Microservice chỉ gồm business logic không bao gồm HTML, CSS
- Dễ dàng tích hợp 3rd-party
- Mỗi service có thể có db riêng

## 6.3 Khuyết điểm
- Microservice có thể dẫn được với có quá nhiều operation
- Việc quản lý distributed system thì phức tạp
- Số lượng service càng lớn thì vấn đề về quản lí cũng phức tạp hơn


