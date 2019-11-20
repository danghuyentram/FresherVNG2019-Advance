# Redis

# 1. What is Redis?

Redis được biết đến như 1 data structures server. Nghĩa là Redis cũng cấp truy cập tới mutable data structure thông qua 1 set các command, redis dùng server-client model với TCP sockets và 1 simple protocol. Vậy nên các process khác nhau có thể query và modify cùng 1 data structure theo 1 shared way.

Data structures implemented trong Redis có 1 số properties đặc biệt sua:
- Redis quan tâm đến việc lưu trữ trên disk, ngay cả khi chúng luôn được served và modified trong server memory. Redis thì nhanh nhưng không bị biến mất
- Implementation của data structures tạo gánh nặng lên memory eficiency, nên data structures trong Redis sẽ dùng ít memory hơn data structures model tương tự dùng high level programing language.
- Redis cung cấp 1 số các feature để tìm trong database như replication, tunable level of durability, cluster, high availability

# 2. Redis data types
Redis không chỉ lưu trử key-value cơ bản với string-key và string-value. Trong redis value có thể giữ giá trị của các data structures phức tạp:
- Binary-safe strings
- Lists
- Sets
- Hashes
- Bit arrays (bitmaps)
- HyperLogLogs
- Streams

## 2.1 Redis keys
Redis keys là binary safe, nghĩa là có thể dùng bất kì chuỗi binary nào để làm key.

Một số rule để đặt keys:
- Keys dài thì không tốt. Vd: 1 key 1024 bytes thì không tốt cho bộ nhớ, bởi vì việc tìm kiếm key trong dataset tốn nhiều chi phí cho việc compare key.
- Keys quá ngắn cũng không tốt. 
- Keys nên gắn với schema. Vd: "object:type:id"
- ***Maximum allowed key size là 512MB***

## 2.2. Redis strings
Là data type duy nhất trên Memcached. String data type hữu dụng trong một số trường hợp như caching HTML fragment hoặc pages.

vd:
```
> set mykey somevalue
OK
> get mykey
"somevalue"
```

***Value không được lớn hơn 512MB***
- `SET`: set a string value, replace value hiện tại nếu trùng key, kể cả value đó không phải string-type. `SET` command cũng có thêm 1 số option:
  - `SET`: fail nếu key đó exists và ngược lại, nó sẽ success nếu key đó đã exists
    ```
        > set mykey newval nx
        (nil)
        > set mykey newval xx
        OK
    ```
  - automic increament: `INCR` command parses string value -> integer, tăng nó lên 1, cuối cùng là tự động set value xuống. `INCRBY`, `DECR` and `DECRBY` cũng tương tự. `INCR` là atomic kể cả khi nhiều client cùng thực hiện INCR với cùng 1 key thì cũng không thể xảy ra tranh chấp quyền điều khiển được. Vd: sẽ không bao giờ xảy ra việc client 1 reads "10" và client 2 read "10" cùng 1 lúc, cả 2 đều sẽ tăng lên 11, và sẽ set new value là 11. Vậy nên giá trị cuối cùng nhận được luôn là 12 và read-increment-set operation luôn được thực hiện kể cả khi các client khác không execute command cùng 1 lúc.
    ```
        > set counter 100
        OK
        > incr counter
        (integer) 101
        > incr counter
        (integer) 102
        > incrby counter 50
        (integer) 152
    ```
  - `GETSET`: set 1 new value cho key và trả về old value
  - `MSET` `MGET`: get set nhiều cặp key-value

| Sr.No | Command & Description                                                                                 |
|-------|-------------------------------------------------------------------------------------------------------|
| 1     | SET key valueThis command sets the value at the specified key.                                        |
| 2     | GET keyGets the value of a key.                                                                       |
| 3     | GETRANGE key start endGets a substring of the string stored at a key.                                 |
| 4     | GETSET key valueSets the string value of a key and return its old value.                              |
| 5     | GETBIT key offsetReturns the bit value at the offset in the string value stored at the key.           |
| 6     | MGET key1 [key2..]Gets the values of all the given keys                                               |
| 7     | SETBIT key offset valueSets or clears the bit at the offset in the string value stored at the key     |
| 8     | SETEX key seconds valueSets the value with the expiry of a key                                        |
| 9     | SETNX key valueSets the value of a key, only if the key does not exist                                |
| 10    | SETRANGE key offset valueOverwrites the part of a string at the key starting at the specified offset  |
| 11    | STRLEN keyGets the length of the value stored in a key                                                |
| 12    | MSET key value [key value ...]Sets multiple keys to multiple values                                   |
| 13    | MSETNX key value [key value ...]Sets multiple keys to multiple values, only if none of the keys exist |
| 14    | PSETEX key milliseconds valueSets the value and expiration in milliseconds of a key                   |
| 15    | INCR keyIncrements the integer value of a key by one                                                  |
| 16    | INCRBY key incrementIncrements the integer value of a key by the given amount                         |
| 17    | INCRBYFLOAT key incrementIncrements the float value of a key by the given amount                      |
| 18    | DECR keyDecrements the integer value of a key by one                                                  |
| 19    | DECRBY key decrementDecrements the integer value of a key by the given number                         |
| 20    | APPEND key valueAppends a value to a key                                                              |

### 2.2.1 Redis expires: keys with limited time to live
Có thể set timeout: limited time to live cho 1 key. Khi hết time, key tự động bị destroyed, giống như thực hiện `DEL` command

1 số thông tin về Redis expires:
- Có thể set bằng second hoặc millisecond
- Tuy nhiên expire time resolution luôn là 1 millisecond.
- Infor về expires sẽ được replicated và pesisted trên disk

```
> set key some-value
OK
> expire key 5
(integer) 1
> get key (immediately)
"some-value"
> get key (after some time)
(nil)
```

## 2.3 Redis lists
Redis lists implement Linked list. Vì implement theo Linked list nên insert ở đầu và cuối chỉ tốn constant time. 
Maximum length của 1 list là 2^32-1 phần tử

| Sr.No | Command & Description                                                                                                                     |
|-------|-------------------------------------------------------------------------------------------------------------------------------------------|
| 1     | BLPOP key1 [key2 ] timeoutRemoves and gets the first element in a list, or blocks until one is available                                  |
| 2     | BRPOP key1 [key2 ] timeoutRemoves and gets the last element in a list, or blocks until one is available                                   |
| 3     | BRPOPLPUSH source destination timeoutPops a value from a list, pushes it to another list and returns it; or blocks until one is available |
| 4     | LINDEX key indexGets an element from a list by its index                                                                                  |
| 5     | LINSERT key BEFORE|AFTER pivot valueInserts an element before or after another element in a list                                          |
| 6     | LLEN keyGets the length of a list                                                                                                         |
| 7     | LPOP keyRemoves and gets the first element in a list                                                                                      |
| 8     | LPUSH key value1 [value2]Prepends one or multiple values to a list                                                                        |
| 9     | LPUSHX key valuePrepends a value to a list, only if the list exists                                                                       |
| 10    | LRANGE key start stopGets a range of elements from a list                                                                                 |
| 11    | LREM key count valueRemoves elements from a list                                                                                          |
| 12    | LSET key index valueSets the value of an element in a list by its index                                                                   |
| 13    | LTRIM key start stopTrims a list to the specified range                                                                                   |
| 14    | RPOP keyRemoves and gets the last element in a list                                                                                       |
| 15    | RPOPLPUSH source destinationRemoves the last element in a list, appends it to another list and returns it                                 |
| 16    | RPUSH key value1 [value2]Appends one or multiple values to a list                                                                         |
| 17    | RPUSHX key valueAppends a value to a list, only if the list exists                                                                        |

Những trường hợp phổ biến dùng lists:
- Ghi nhớ lastest updates posted của user trên MXH
- Giao tiếp giữa các process dùng consumer-producer pattern nơi mà producer push item vào 1 list và consumer(worker) tiêu thụ item đó và execute action.

## 2.4 Redis Set
Redis set là tập các unique strings không có thứ tự, không cho phép các key trùng nhau. 
Redis set add, remove, kiểm tra tồn tại 1 key với O(1) time.
Maximum length của 1 list là 2^32-1 phần tử

| Sr.No | Command & Description                                                                                                                     |
|-------|-------------------------------------------------------------------------------------------------------------------------------------------|
| 1     | BLPOP key1 [key2 ] timeoutRemoves and gets the first element in a list, or blocks until one is available                                  |
| 2     | BRPOP key1 [key2 ] timeoutRemoves and gets the last element in a list, or blocks until one is available                                   |
| 3     | BRPOPLPUSH source destination timeoutPops a value from a list, pushes it to another list and returns it; or blocks until one is available |
| 4     | LINDEX key indexGets an element from a list by its index                                                                                  |
| 5     | LINSERT key BEFORE|AFTER pivot valueInserts an element before or after another element in a list                                          |
| 6     | LLEN keyGets the length of a list                                                                                                         |
| 7     | LPOP keyRemoves and gets the first element in a list                                                                                      |
| 8     | LPUSH key value1 [value2]Prepends one or multiple values to a list                                                                        |
| 9     | LPUSHX key valuePrepends a value to a list, only if the list exists                                                                       |
| 10    | LRANGE key start stopGets a range of elements from a list                                                                                 |
| 11    | LREM key count valueRemoves elements from a list                                                                                          |
| 12    | LSET key index valueSets the value of an element in a list by its index                                                                   |
| 13    | LTRIM key start stopTrims a list to the specified range                                                                                   |
| 14    | RPOP keyRemoves and gets the last element in a list                                                                                       |
| 15    | RPOPLPUSH source destinationRemoves the last element in a list, appends it to another list and returns it                                 |
| 16    | RPUSH key value1 [value2]Appends one or multiple values to a list                                                                         |
| 17    | RPUSHX key valueAppends a value to a list, only if the list exists                                                                        |

## 2.5 Redis Hash
Redis hashes map giữa string fields với string values. 
Redis có thể lưu tới 4 tỷ cặp field-value

| Sr.No | Command & Description                                                                                  |
|-------|--------------------------------------------------------------------------------------------------------|
| 1     | HDEL key field2 [field2]Deletes one or more hash fields.                                               |
| 2     | HEXISTS key fieldDetermines whether a hash field exists or not.                                        |
| 3     | HGET key fieldGets the value of a hash field stored at the specified key.                              |
| 4     | HGETALL keyGets all the fields and values stored in a hash at the specified key                        |
| 5     | HINCRBY key field incrementIncrements the integer value of a hash field by the given number            |
| 6     | HINCRBYFLOAT key field incrementIncrements the float value of a hash field by the given amount         |
| 7     | HKEYS keyGets all the fields in a hash                                                                 |
| 8     | HLEN keyGets the number of fields in a hash                                                            |
| 9     | HMGET key field1 [field2]Gets the values of all the given hash fields                                  |
| 10    | HMSET key field1 value1 [field2 value2 ]Sets multiple hash fields to multiple values                   |
| 11    | HSET key field valueSets the string value of a hash field                                              |
| 12    | HSETNX key field valueSets the value of a hash field, only if the field does not exist                 |
| 13    | HVALS keyGets all the values in a hash                                                                 |
| 14    | HSCAN key cursor [MATCH pattern] [COUNT count]Incrementally iterates hash fields and associated values |

## 2.6 Redis sorted set
Redis sorted set là mix giữa redis set và hash. Điểm khác nhau giữa sorted set và set là sorted set được sắp xếp theo score từ nhỏ tới lớn.

***Note***: sorted set được implement thông qua dual-ported data structure bao gồm 1 skip list và 1 hash table, nên bất kì khi nào ta add 1 element thì chỉ tốn O(logN) chí phí. 

| Sr.No | Command & Description                                                                                                               |
|-------|-------------------------------------------------------------------------------------------------------------------------------------|
| 1     | ZADD key score1 member1 [score2 member2]Adds one or more members to a sorted set, or updates its score, if it already exists        |
| 2     | ZCARD keyGets the number of members in a sorted set                                                                                 |
| 3     | ZCOUNT key min maxCounts the members in a sorted set with scores within the given values                                            |
| 4     | ZINCRBY key increment memberIncrements the score of a member in a sorted set                                                        |
| 5     | ZINTERSTORE destination numkeys key [key ...]Intersects multiple sorted sets and stores the resulting sorted set in a new key       |
| 6     | ZLEXCOUNT key min maxCounts the number of members in a sorted set between a given lexicographical range                             |
| 7     | ZRANGE key start stop [WITHSCORES]Returns a range of members in a sorted set, by index                                              |
| 8     | ZRANGEBYLEX key min max [LIMIT offset count]Returns a range of members in a sorted set, by lexicographical range                    |
| 9     | ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]Returns a range of members in a sorted set, by score                                  |
| 10    | ZRANK key memberDetermines the index of a member in a sorted set                                                                    |
| 11    | ZREM key member [member ...]Removes one or more members from a sorted set                                                           |
| 12    | ZREMRANGEBYLEX key min maxRemoves all members in a sorted set between the given lexicographical range                               |
| 13    | ZREMRANGEBYRANK key start stopRemoves all members in a sorted set within the given indexes                                          |
| 14    | ZREMRANGEBYSCORE key min maxRemoves all members in a sorted set within the given scores                                             |
| 15    | ZREVRANGE key start stop [WITHSCORES]Returns a range of members in a sorted set, by index, with scores ordered from high to low     |
| 16    | ZREVRANGEBYSCORE key max min [WITHSCORES]Returns a range of members in a sorted set, by score, with scores ordered from high to low |
| 17    | ZREVRANK key memberDetermines the index of a member in a sorted set, with scores ordered from high to low                           |
| 18    | ZSCORE key memberGets the score associated with the given member in a sorted set                                                    |
| 19    | ZUNIONSTORE destination numkeys key [key ...]Adds multiple sorted sets and stores the resulting sorted set in a new key             |
| 20    | ZSCAN key cursor [MATCH pattern] [COUNT count]Incrementally iterates sorted sets elements and associated scores                     |

## 2.7 Bitmap
Bitmaps không phải là 1 data type chính thức, nó là set của các operation hướng bit được định nghĩa trên String type. Vì string là binary safe và maximum length của nó là 512MB, nên thích hợp set up 2^32 bits khác nhau.

Một trong những điểm mạnh của bitmap là cung cấp nhiều space để lưu trữ infor hơn. Vd: 1 system với nhiều user khác nhau được xác định bởi incremental user IDs, nó khả thi khi nhớ 1 single bit info của 4 tỉ user chỉ với 512MB memory.

```
> setbit key 10 1
(integer) 1
> getbit key 10
(integer) 1
> getbit key 11
(integer) 0
```

- `SETBIT`: tham số thứ 1: số lượng bit, tham số thứ 2: value cần set bit, là 0 hoặc 1. 
- `GETBIT`: trả về giá trị của bit ở index thứ mấy. Out of range bit luôn return 0
- `BITOP`: gồm các bit operation giữa multiple key(bao gồm string value) và lưu nó vào 1 key đích
- `BITCOUNT`: count number of set bit trong 1 string
- `BITPOS`: tìm bit đầu tiên có giá trị 0 hoặc 1

Những trường hợp phổ biến dùng bitmap:
- Real time analytics of all kinds
- Storing space efficient but high performance boolean information associated with object IDs.

## 2.8 HyperLogLogs
HyperLoglogs là data structure xác suất dùng để đếm số phần tử khác nhau. Thuật toán này hy sinh độ chính xác để đổi lấy memory: 12kb per key với standard error là 0.81%. 

| Sr.No | Command & Description                                                                                          |
|-------|----------------------------------------------------------------------------------------------------------------|
| 1     | PFADD key element [element ...]Adds the specified elements to the specified HyperLogLog.                       |
| 2     | PFCOUNT key [key ...]Returns the approximated cardinality of the set(s) observed by the HyperLogLog at key(s). |
| 3     | PFMERGE destkey sourcekey [sourcekey ...]Merges N different HyperLogLogs into a single one.                    |



# 3. Command of redis
link: https://redis.io/commands

Use SCAN, Nerver user KEYS
| SCAN                                                                                                                                                                                                                                                                  | KEYS                                                                                                           |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| SCAN  cursor  [MATCH pattern]  [COUNT count]  [TYPE type]                                                                                                                                                                                                             | KEYS  pattern                                                                                                  |
| Time complexity: O(1) cho mỗi lần gọi. O(N) cho 1 vòng lặp hoàn thành, N là số phần tử trong collection                                                                                                                                                               | O(N) với N là số keys trong db                                                                                 |
| Cho phép incremental iteration, chỉ return 1 lượng nhỏ các element mỗi lần gọi, có thể dùng ở production thay cho KEYS hoặc SMEMBERS vì chúng nó block server 1 khoảng thời gian dài (có thể lên tới vài s) khi được call với 1 lượng collection key hoặc element lớn | Không nên dùng KEYS trong regular application code.  Nó sẽ làm giảm performance khi được executed với 1 db lớn |

https://redis.io/commands/scan

https://redis.io/commands/keys

# 4 Distrubuted locks with Redis

Redlock algorithm

Giả sử có N redis master, mỗi node thì hoàn toàn độc lập và không dungf replication hay bất kì coordination system ngầm nào. 

Để lấy 1 lock thì client sẽ thực hiện như sau:
1. Lấy current time in milliseconds
2. Cố gắng lấy lock trên tất cả N instances một cách tuần tự, dùng cùng 1 key name và 1 random value trên tất cả các instance. Trong bước này, khi setting lock trên mỗi instance, client dùng 1 timeout nhỏ hơn tổng thời gian lock auto-release để yêu cầu nó. VD: nếu thời gian auto-release là 10s thì timeout sẽ phải nằm trong khoảng 5-50 ms. Nó ngăn client bị block 1 khoảng thời gian dài khi cố giao tiếp với 1 redis node bị down: nếu 1 instance không available, ta nên có gắng giao tiếp với instance ASAP tiếp theo
3. Client sẽ tính số thời gian cần để lấy lock, bằng cách trừ đi current time ở step 1. Chỉ khi client lấy được lock trên hầu hết các instance(ít nhất là n/2 +1),và tổng thời gian cần để lấy lock nhỏ hơn thời gian hiệu lục của lock, thì lock sẽ được lấy
4. Nếu lock đã được lấy, thời gian hiệu lục còn lại của nó sẽ bằng thời gian hiệu lực lúc đầu trừ đi thơi gian cần để lấy lock ở bước 3
5. Nếu client failed khi lấy lock vì 1 số nguyên nhân ( không thể lấy lock ở N/2+1 instances hoặc thời gian hiệu lực bị âm), thì nó phải cố unlock trên tất cả instance

Thuật toán dựa trên giả thuyết không có clock được đồng bộ giữa các process, các local time trong mỗi process thì chạy giống nhau, với sai số nhỏ hơn auto-release time của lock. Giả thuyết này gần giống với real-world computer: mỗi computer có 1 local clock và chênh lệch giữa các clock là rất nhỏ.
Đảm bảo client giữ lock sẽ terminate công việc của nó thời gian hiệu lục của lock (step 3), trừ cho 1 khoảng thời gian (sự chênh lệch time giữa các process)

## 4.1 Retry on failure

Khi 1 client không thể lấy lock, nó nên thử lại sau 1 random deplay time để đông bộ với multiple clients cố lấy lock trên cùng 1 resource ở cùng 1 thời điểm. Ngoài ra, client càng cố lấy lock trên hầu hết các redis instance, thì window sẽ càng nhỏ vì split brain condition (và cần phải thử lại), thế nên lý tưởng nhất và client nên cố gửi SET command tới N instance trong cùng 1 lúc thông qua multiplexing(ghép kênh)

Điều quan trọng nữa là client khi lấy hầu hết lock fail, để release (1 phần) khóa thu đã lấy được càng sớm càng tốt, vì thế không cần phải chờ key hết hạn để lock có thể được lấy 1 lần nữa

## 4.2 Releasing lock

Release lock trên tât cả instance, không quan tâm tới việc client có lấy lock thành công từ instance đó hay không

link: https://redis.io/topics/distlock
http://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html

# 5. Redis persistance
Redis cung cấp 1 số các persistence option:
- RDB persistence biểu diễn point-in-time snapshots của dataset trong khoảng thời gian quy định
- AOF persistence log mỗi write opertaion nhận được từ server, nó sẽ được chạy lại khi server khỏi động, reconstructing original dataset. Các command được log lại dùng cùng 1 forrmat như chính redis protocol, append only. Redis có thể rewrite log in background khi nó quá lớn
- Có thể disable hoàn toàn persistence, nếu bạn muốn data chỉ tồn tại khi mà server running
- Có thể kết hợp AOF và RDB trên cùng 1 instance. Chú ý, với trường hợp này, khi Redis restart ÀO file sẽ được dùng để tái cấu trúc original dataset vì nó sẽ đảm báo nhất

## 5.1 RDB (Redis db)
### 5.1.1 Ưu điểm

- RDB là 1 singlefile rất gọn nhẹ ở 1 thời điểm đại diện cho redis data. RDB files hoàn hảo cho backups. Vd: bạn muốn lưu trữ các RDB file mỗi h trong vòng 24h, và save RDB snaphot cho mỗi ngày trong 30 ngày. Nó cho phép bạn dễ dàng lưu trữ các version khác nhau của dataset trong trường hợp gặp sự cố.
- RDB thì dùng tốt cho recovery khi gặp sự cố, single compact file có thể được transfer tới xa data center, hoặc tới Amazon S3(có thể được mã hóa)
- RDB tối đa hóa Redis performance khi việc mà Redis parent process cần persist là forking child và child đó sẽ làm các việc còn lại. Parent instance sẽ không thực hiện disk I/O
- RDB cho phép restart 1 big dataset nhanh hơn AOF

### 5.1.2 Khuyết điểm
- RDB không dùng tốt nếu bạn cần giảm data loss khi redis stop working (vd như mất điện). 
- RDB cần fork() thường xuyên để persist trên disk thông qua child process. Fork() có thể tốn thời gian với 1 dataset lớn, có thể dẫn tới Redis dừng phục vụ client trong vài ms hoặc vài s nếu dataset quá lớn và CPU performance không tốt. 

## 5.2 AOF (append only file)
### 5.2.1 Ưu điểm
- Dùng AOF sẽ bền vững hơn: có các fsync policy khác nhau: no fsync at all, fsync mỗi s, fsync mỗi query. Với default policy là fsync mỗi s write performance rất tốt (fsync được thực hiện thông qua background thread và main thread cố gắng thucjw hiện write khi no fsync đang diễn ra),
- AOF log chỉ append thêm log, nên không có seek, hay corruption problem khi bị mất điện. Kể cả nếu log bị được ghi 1 nữa command vì 1 số lí do (disk full,...) redis-check-aof tool có thể fix nó dễ dàng
- Redis tự động rewrite AOF in background khi nó quá lớn. Rewrite hoàn toàn safe vì khi Redis tiếp tục append vào old file, 1 file mới sẽ được tạo với 1 set tối thiểu các operation cần để tạo dataset hiện tại, khi file thứ 2 này đã sẵn sàng, Redis sẽ chuyển qua file này và append dòng mới vào nó
- AOF bao gồm log của tất cả các operation theo 1 cách dễ hiểu và parse format. 

### 5.2.2 Khuyết điểm
- AOF file thường lớn hơn RDB file với cùng 1 dataset
- AOF file thì chậm hơn RDB vì phù thuộc vào fsync policy. Nói chung với fysnc set là every second thì performance luôn rất cao, và với fsync disabled
 nó vẫn nhanh hơn RDB ngay cả khi chịu tải cao. Tuy nhiên RDB vẫn có thể cung cấp nhiều đảm bảo hơn về maximum latency ngay trong trường hợp chịu tải ghi lớn
- Redis AOF hoạt động dựa vào incremetally updating an existing state, như Mysql hoặc MongoDB, trong khi RDB snapshotting create mọi thứ từ scratch lần lặp đi lặp lại.
  - Chú ý ràng mỗi lần AOF được rewrite bởi Redis, nó được tái tạo lại từ scratch bắt đầu từ data thực tế trong dataset, làm cho khả năng chống bug mạnh hơn so vơi việc luôn append vào AOF file (hoặc 1 lần rewriten read old AOF thay vì read data trong memory)
  - Chưa có report nào từ user về việc AOF corruption đươc detected từ real world

# 6. How to configure redis?
Redis có thể start mà không cần configuration file, thông qua build-in default configuration. Tuy nhiên setup này chỉ nên dùng cho testing và development purpose

Cách thích hợp để configure Redis thông qua 1 Redis configuration file, gọi là `redis.conf`. `redis.conf` file bao gồm 1 số chỉ thị với format như sau:

```
keyword argument1 argument2 ... argumentN
```

Vd:
```
slaveof 127.0.0.1 6380
```

```
requirepass "hello world"
```

link: https://redis.io/topics/config

## 6.1 Passing argument thông qua command line
Từ Redis 2.6 đã để thể pass Redis configuration parameter thông qua command line, rất hữu ích cho testing

```
./redis-server --port 6380 --slaveof 127.0.0.1 6379
```

format giống với Redis configure nhưng thêm -- phía trước

## 6.2 Change Redis configuration trong khi server đang chạy
It is possible to reconfigure Redis on the fly without stopping and restarting the service, or querying the current configuration programmatically using the special commands CONFIG SET and CONFIG GET

Not all the configuration directives are supported in this way, but most are supported as expected. Please refer to the CONFIG SET and CONFIG GET pages for more information.

Note that modifying the configuration on the fly has no effects on the redis.conf file so at the next restart of Redis the old configuration will be used instead.

Make sure to also modify the redis.conf file accordingly to the configuration you set using CONFIG SET. You can do it manually, or starting with Redis 2.8, you can just use CONFIG REWRITE, which will automatically scan your redis.conf file and update the fields which don't match the current configuration value. Fields non existing but set to the default value are not added. Comments inside your configuration file are retained.

## 6.3 Configure Redis as a cache
If you plan to use Redis just as a cache where every key will have an expire set, you may consider using the following configuration instead (assuming a max memory limit of 2 megabytes as an example):

```
maxmemory 2mb
maxmemory-policy allkeys-lru
```

In this configuration there is no need for the application to set a time to live for keys using the EXPIRE command (or equivalent) since all the keys will be evicted using an approximated LRU algorithm as long as we hit the 2 megabyte memory limit.

Basically in this configuration Redis acts in a similar way to memcached. We have more extensive documentation about using Redis as an LRU cache.

# 7. How to optimize memory?
## 7.1 Special encoding of small aggregate data types

## 7.2 Using 32 bit instances
Redis compile với 32 big target dùng rất ít memory cho 1 key, vì pointer thì nhỏ nhưng instance có thể lên tới 4GB maximum memory usage. Để compile Redis thành 32 bit binary dùng make 32bit. RDB và AOF file đều tương thích với 32bit và 64bit instance 

## 7.3 Bit and byte level operations
Redis 2.2 có new bit và byte level operation mới:
`GETRANGE`, `SETRANGE`, `GETBIT` and `SETBIT`. Dùng các command này, có thể truy cập Redis string type như 1 random access array. 

Vd: khi app lưu thông tin identified user là 1 số integer tăng dần, có thể dùng bitmap để save infor về subscription của user trong mail list, set bit khi subscribed và clear nó khi unsubscribeb. Với 100 triệu user, data này chỉ tốn khoảng 12 MB trên RAM cho 1 Redis instance. 

Có thể dùng `GETRANGE` and `SETRANGE` để lưu 1 byte các infor cho mỗi user.  

## 7.4 Use hashes when possible
Small hashes được encoded chỉ chiếm 1 space rất nhỏ, nên dùng hashes bất cứ khi nào có thể

## 7.5 Using hashes to abstract a very memory efficient plain key-value store on top of Redis

Một vài key cũng dùng nhiều memory hơn 1 single key bao gồm 1 hash các field. 

## 7.6 Memory allocation
Redis manages memory:
- Redis không free up (return) memory cho OS khi key removed. 
- get nhiều nhất memory có thể, vì khi free thì Redis vẫn có thể dùng lại vùng bộ nhớ đó
- Đặt limit maxmemory nếu không Redis dần chiếm hết free memory
  
link: https://github.com/sripathikrishnan/redis-rdb-tools/wiki/Redis-Memory-Optimization


It makes Redis return an out of memory error for write commands if and when it reaches the limit - which in turn may result in errors in the application but will not render the whole machine dead because of memory starvation.

http://oldblog.antirez.com/post/redis-as-LRU-cache.html

# 8 Redisson
Redisson là Redis client for java.

## 8.1 Object
- ObjectHolder
- BinaryStreamHolder
- GeospatialHolder
- BitSet
- AtomicLong
- AtomicDouble
- Topic
- BloomFilter
- HyperLogLog

These distributed objects follow specifications from the java.util.concurrent.atomic package. They support lock-free, thread-safe and atomic operations on objects stored in Redis. Data consistency between applications/servers is ensured as values are not updated while another application is reading the object.

### 8.1.1 Atomic operation
There is a branch of research focused on creating non-blocking algorithms for concurrent environments. These algorithms exploit low-level atomic machine instructions such as compare-and-swap (CAS), to ensure data integrity.

A typical CAS operation works on three operands:

1. The memory location on which to operate (M)
2. The existing expected value (A) of the variable
3. The new value (B) which needs to be set
The CAS operation updates atomically the value in M to B, but only if the existing value in M matches A, otherwise no action is taken.

In both cases, the existing value in M is returned. This combines three steps – getting the value, comparing the value and updating the value – into a single machine level operation.

When multiple threads attempt to update the same value through CAS, one of them wins and updates the value. However, unlike in the case of locks, no other thread gets suspended; instead, they're simply informed that they did not manage to update the value. The threads can then proceed to do further work and context switches are completely avoided.

One other consequence is that the core program logic becomes more complex. This is because we have to handle the scenario when the CAS operation didn't succeed. We can retry it again and again till it succeeds, or we can do nothing and move on depending on the use case.

## 8.2 Collection
- Map
- Multimap
- Set
- SortedSet
- ScoredSortedSet
- LexSortedSet
- List
- Queue
- Deque
- BlockingQueue
- BoundedBlockingQueue
- BlockingDeque
- BlockingFairQueue
- DelayedQueue
- PriorityQueue
- PriorityDeque

## 8.3 

https://www.baeldung.com/redis-redisson

# 9 Spring boot start with redis
## 9.1 spring-boot-starter-data-redis

https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-starters/spring-boot-starter-data-redis/pom.xml

# 10 Data serialization
Serialization: chuyển từ 1 object thành 1 mảng byte. 
Deserialization: chuyển từ 1 mảng byte thành 1 object

![](https://media.geeksforgeeks.org/wp-content/cdn-uploads/gq/2016/01/serialize-deserialize-java.png)

## 10.1 Redis Protocol specification
Redis client giao tiếp với Redis server thông qua RESP(Redis Serialization Protocol). RESP có thể serialize các data type như: integers, strings, arrays, errors. Request được gửi từ client tới Redis server là 1 arrays string gồm các argument để execute 1 command. Redis reply 1 command-specific data type

Link: https://redis.io/topics/protocol

## 10.2 Data serialization redisson

Data serialization được sử dụng rộng rãi bởi Redisson để marshall and unmarshall bytes nhận được thông qua network link từ Redis server. Một số codecs có sẵn như sau:
Codec: một thiết bị hay chương trình nén data để truyền nhanh hơn và giải nén khi nhận được


| Codec class name                         | Description                                                                                                       |
|------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| org.redisson.codec.FstCodec              | [FST binary codec ](https://github.com/RuedigerMoeller/fast-serialization). Up to 10x faster than JDK Serialization codec.Default codec                                     |
| org.redisson.codec.JsonJacksonCodec      | [Jackson JSON codec](https://github.com/FasterXML/jackson).Stores type information in @class field                                                        |
| org.redisson.codec.MarshallingCodec      | [JBoss Marshalling](https://github.com/jboss-remoting/jboss-marshalling) binary codec                                                                                    |
| org.redisson.codec.AvroJacksonCodec      | [Avro](http://avro.apache.org/) binary json codec                                                                                            |
| org.redisson.codec.SmileJacksonCodec     | Smile binary json codec                                                                                           |
| org.redisson.codec.CborJacksonCodec      | [CBOR](http://cbor.io/) binary json codec                                                                                            |
| org.redisson.codec.MsgPackJacksonCodec   | [MsgPack](https://msgpack.org/) binary json codec                                                                                         |
| org.redisson.codec.IonJacksonCodec       | [Amazon Ion](https://amzn.github.io/ion-docs/) codec                                                                                                  |
| org.redisson.codec.KryoCodec             | [Kryo](https://github.com/EsotericSoftware/kryo) binary codec                                                                                                 |
| org.redisson.codec.SerializationCodec    | JDK Serialization binary codec                                                                                    |
| org.redisson.codec.LZ4Codec              | [LZ4](https://github.com/lz4/lz4-java) compression codec.Uses FstCodec for serialization by default                                                  |
| org.redisson.codec.SnappyCodec           | Netty's implementation of Snappy compression codec.Uses FstCodec for serialization by default                     |
| org.redisson.codec.SnappyCodecV2         | Snappy compression codec based on [snappy-java](https://github.com/xerial/snappy-java) project.Uses FstCodec for serialization by default                  |
| org.redisson.codec.TypedJsonJacksonCodec | Jackson JSON codec which doesn't store type id (@class field) during encoding and doesn't require it for decoding |
| org.redisson.client.codec.StringCodec    | String codec                                                                                                      |
| org.redisson.client.codec.LongCodec      | Long codec                                                                                                        |
| org.redisson.client.codec.ByteArrayCodec | Byte array codec                                                                                                  |
| org.redisson.codec.CompositeCodec        | Used to mix different codecs as one                                                                               |

link: https://github.com/redisson/redisson/wiki/4.-data-serialization

### 10.2.1 FST binary codec


### 10.2.2 Jackson JSON codec
link: https://github.com/FasterXML/jackson

Jackson known as the standard JSON library for Java (or JVM platform in general), or, as the "best JSON parser for Java." Or simply as "JSON for Java." More than that, Jackson is a suite of data-processing tools for Java (and the JVM platform), including the flagship streaming JSON parser / generator library, matching data-binding library (POJOs to and from JSON) and additional data format modules to process data encoded in Avro, BSON, CBOR, CSV, Smile, (Java) Properties, Protobuf, XML or YAML; and even the large set of data format modules to support data types of widely used data types such as Guava, Joda, PCollections and many, many more 

Core modules:
- [Streaming](https://github.com/FasterXML/jackson-core) ([docs](https://github.com/FasterXML/jackson-core/wiki)) ("jackson-core") defines low-level streaming API, and includes JSON-specific implementations
- [Annotations](https://github.com/FasterXML/jackson-annotations) ([docs](https://github.com/FasterXML/jackson-annotations/wiki)) ("jackson-annotations") contains standard Jackson annotations
- [Databind](https://github.com/FasterXML/jackson-databind) ([docs](https://github.com/FasterXML/jackson-databind/wiki)) ("jackson-databind") implements data-binding (and object serialization) support on streaming package; it depends both on streaming and annotations packages

Tutorial:
Link: https://www.baeldung.com/jackson

#### 10.2.2.1 Change Name of Field
Using @JsonProperty, @JsonSetter and @JsonGetter to rename property names

- Using @JsonProperty: Following example shows how to use @JsonProperty annotation to rename properties. This annotation can be used on fields or getters or setters.
  ```
    public class ExampleMain {
    public static void main(String[] args) throws IOException {
        Employee employee = new Employee();
        employee.setName("Trish");
        employee.setDept("Admin");

        //convert to json
        String jsonString = toJson(employee);
        System.out.println(jsonString);
        //convert to object
        Employee e = toEmployee(jsonString);
        System.out.println(e);
    }

    private static Employee toEmployee(String jsonData) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(jsonData, Employee.class);
    }

    private static String toJson(Employee employee) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(employee);
    }

    private static class Employee {
        @JsonProperty("employee-name")
        private String name;
        @JsonProperty("employee-dept")
        private String dept;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", dept='" + dept + '\'' +
                    '}';
        }
    }
  }
  ```
  Output:
  ```
    {"employee-name":"Trish","employee-dept":"Admin"}
    Employee{name='Trish', dept='Admin'}
  ```

- Using @JsonGetter and @JsonSetter: @JsonGetter and @JsonSetter are old alternatives to @JsonProperty.
  ```
  public class ExampleMain2 {
    public static void main(String[] args) throws IOException {
        Employee employee = new Employee();
        employee.setName("Trish");
        employee.setDept("Admin");

        //convert to json
        String jsonString = toJson(employee);
        System.out.println(jsonString);
        //convert to object
        Employee e = toEmployee(jsonString);
        System.out.println(e);
    }

    private static Employee toEmployee(String jsonData) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(jsonData, Employee.class);
    }

    private static String toJson(Employee employee) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(employee);
    }

    private static class Employee {
        private String name;
        private String dept;

        @JsonGetter("employee-name")
        public String getName() {
            return name;
        }

        @JsonSetter("employee-name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonGetter("employee-dept")
        public String getDept() {
            return dept;
        }

        @JsonSetter("employee-dept")
        public void setDept(String dept) {
            this.dept = dept;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", dept='" + dept + '\'' +
                    '}';
        }
    }
  }
  ```

Link: https://www.logicbig.com/tutorials/misc/jackson/renaming-properties.html


#### 10.2.2.2 Jackson Ignore Properties on Marshalling
- Ignore Fields at the Class Level: We can ignore specific fields at the class level, using the @JsonIgnoreProperties annotation and specifying the fields by name:
  ```
    @JsonIgnoreProperties(value = { "intValue" })
    public class MyDto {
    
        private String stringValue;
        private int intValue;
        private boolean booleanValue;
    
        public MyDto() {
            super();
        }
    
        // standard setters and getters are not shown
    }
  ```
  We can now test that, after the object is written to JSON, the field is indeed not part of the output:

  ```
    @Test
    public void givenFieldIsIgnoredByName_whenDtoIsSerialized_thenCorrect()
      throws JsonParseException, IOException {
      
        ObjectMapper mapper = new ObjectMapper();
        MyDto dtoObject = new MyDto();
    
        String dtoAsString = mapper.writeValueAsString(dtoObject);
    
        assertThat(dtoAsString, not(containsString("intValue")));
    }
  ```
- Ignore Field at the Field Level: We can also ignore a field directly via the @JsonIgnore annotation directly on the field:
  ```
    public class MyDto {
  
      private String stringValue;
      @JsonIgnore
      private int intValue;
      private boolean booleanValue;
  
      public MyDto() {
          super();
      }
  
      // standard setters and getters are not shown
  }
  ```
  We can now test that the intValue field is indeed not part of the serialized JSON output:
  ```
    @Test
    public void givenFieldIsIgnoredDirectly_whenDtoIsSerialized_thenCorrect() 
      throws JsonParseException, IOException {
      
        ObjectMapper mapper = new ObjectMapper();
        MyDto dtoObject = new MyDto();
    
        String dtoAsString = mapper.writeValueAsString(dtoObject);
    
        assertThat(dtoAsString, not(containsString("intValue")));
    }
  ```

# 11 Compare FstCodec, JsonJacksonCodec, KryoCodec
Đối với 1 số nhu cầu, chẳng hạn như lưu trữ dài hạn các serialized bytes, điều quan trọng là làm thế nào để serialization handle thay đổi cho các class.
- Forward compatibility (reading bytes serialized by newer classes)
- Backward compatibility (reading bytes serialized by older classes)

## 11.1 FSTCodec
### 11.1.1 Compatible

### 11.1.2 Performance

## 11.2 JsonJacksonCodec
### 11.2.1 Compatible
Cung cấp các annotation để add, rename, inject, ignore các property khi deserialization

- Dùng @JsonVersionedModel và @JsonSerializeToVersion annotation
https://github.com/jonpeterson/jackson-module-model-versioning

- Dùng @JsonSerialize và @JsonDeserialize
- Dùng @JsonProperty, @JsonSetter and @JsonGetter để rename property name
- Dùng @JsonIgnore và @JsonIgnoreProperties để ignore các property
- Dùng @JacksonInject để inject 1 value khi deserialization
- Dùng @JsonCreator để define constructor hoặc factory method cho deserialization
  

### 11.2.2 Performance

## 11.3 KryoCodec
### 11.3.1 Compatible
#### Kryo versioning and upgrading
Tuân theo 1 số rule sau:
1. Major version được increase nếu serialization compatibility( khả năng tương thích) bị phá vỡ ( data serialized của version trước sẽ không được deserialized với version mới)
2. Minor version được increase nếu binary hoặc source compatibility của documented public APi bị phá vỡ.


#### Serializers
Kryo cung cấp 1 số serializers với nhiều configuration option và level compatibility.

![](media/Serializers.png)


### 11.3.2 Performance
