# Auto commit kafka

# 1. Commit và offset

Khi consumer read message từ partition, nó báo cho Kafka biết offset của message cuối cùng mà nó consume. Offset này được lưu trong 1 topic: _consumer_offsets, nhờ vậy mà consumer có thể stop và restart lại mà không cần phải nhớ những message nào đã được consume

- Nếu offset đã commit < offset của message cuối cùng mà consumer process, thì các message có offset từ offset đã commit tới offset cuối cùng sẽ được process lần thứ 2

![](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in06.png)

- Nếu offset đã commit > offset của message cuối mà consumer process thì các message từ message cuối tới message có offset đã được commit sẽ bị miss
![](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in07.png)

Vậy việc quản lý offset ảnh hướng rất lớn tới consumer 

# 2. Auto commit

Khi tạo 1 consumer, nó sẽ có default properties sau để giúp thực hiện công việc bên trên:

```
enable.auto.commit
auto.commit.interval.ms
```


- enable.auto.commit default value: true
- auto.commit.interval.ms default value: 5000

Vậy default cứ 5s 1 consumer sẽ commit offset của nó cho Kafka hoặc bất kì khi nào data được fetch từ topic thì nó cũng commit offset cuối cùng.



Tuy nhiên 1 số trường hợp nó sẽ không diễn ra như vậy:
Vd: 

- Consumer khi đang chưa process xong message thứ 10 thì bị crash 
![](media/cap3.png)
Nhưng do auto commit đã commit offset với Kafka, nên lúc này offset tiếp theo nhận được là 11, mặc dù message offset thứ 10 chưa xử lí xong sẽ không được gửi lại
![](media/cap4.png)

- Consumer khi đã process xong message ở offset thứ 5 nhưng bị crash
![](media/cap1.png)
Khi consumer restart thì kafka gửi lại message đó, data sẽ bị thay đổi do process lại message này
![](media/cap2.png)


Vậy làm sao để commit offset diễn ra như mong muốn bất kể consumer bị crash?

Ta có thể thực hiện commit offset thử công ngay khi process xong nó.
Thay đổi value của enable.auto.commit property.

```
enable.auto.commit: false
```

API đơn giản và dễ dùng nhất để commit là `commitSync()`: sẽ commit offset cuối cùng được return bởi poll() và chỉ return 1 lần khi offset được commit, throw exception nếu commit fail. Và phải nhớ call `commitSync()` khi process xong tất cả các message, nếu không các message sẽ bị process lại lần nữa.

vd:
```
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(100);
    for (ConsumerRecord<String, String> record : records) {
        System.out.printf("topic = %s, partition = %d, offset =
            %d, customer = %s, country = %s\n",
            record.topic(), record.partition(),
            record.offset(), record.key(), record.value()); 1
    }
    try {
        consumer.commitSync(); 2
    } catch (CommitFailedException e) {
        log.error("commit failed", e) 3
    }
}
```

1. Process message: in tất cả message ra
2. Sau khi process các message xong: gọi `commitSync` để commit offset cuối cùng trước khi polling để lấy thêm message mới về
3. `commitSync` sẽ throw exception nếu commit bị fail


# 3. Reference:
https://medium.com/@danieljameskay/understanding-the-enable-auto-commit-kafka-consumer-property-12fa0ade7b65

https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/ch04.html

