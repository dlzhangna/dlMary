Provider-RestFul:
1.http://localhost:8001/fund/get/166023
2.http://localhost:8001/fund/list
3.http://localhost:8001/fund/insertOne
4.http://localhost:8001/fund/updateByFundCode/008974
5.http://localhost:8001/fund/delete/008974
6.http://localhost:8001/fund/discovery

Consumer:
1.http://localhost/consumer/fund/get/166023
2.http://localhost/consumer/fund/list
3.http://localhost/consumer/fund/delete/008973
4.http://localhost/consumer/fund/updateByFundCode/008971
5.http://localhost/consumer/fund/updateByEntity
6.http://localhost/consumer/fund/insert
7.http://localhost/consumer/fund/discovery

Feign:
1.http://localhost/consumer/fund/get/166023
2.http://localhost/consumer/fund/get/166999
Eureka
http://localhost:7001/
**************[microservicecloud-fund]
MICROSERVICECLOUD-FUND	100.98.103.17	8001	http://100.98.103.17:8001
http://eureka7001.com:7001/


Host:
C:\Windows\System32\drivers\etc\hosts
127.0.0.1 eureka7001.com
127.0.0.1 eureka7002.com
127.0.0.1 eureka7003.com

H2:
http://localhost:6001/h2-console

Dashboard:
http://localhost:9001/hystrix
http://localhost:8001/hystrix.stream

Zuul:
127.0.0.1 zuul.com
http://localhost:9527/microservicecloud-fund/fund/get/166023
http://zuul9527.com:9527/microservicecloud-fund/fund/get/166023
http://zuul9527.com:9527/ctyhFund/fund/get/166023
http://zuul9527.com:9527/bjyt/ctyhFund/fund/get/166023

Git Hub:
https://github.com/
https://github.com/dlzhangna/microservicecloud-config.git
127.0.0.1 cloudConfig3344.com
http://config3344.com:3344/application-dev.properties
127.0.0.1 configClient3355.com
http://configClient3355.com:8201/config

Config Server:
http://config3344.com:3344/application-dev.yml
http://localhost:8888/application-dev.yml
http://config3344.com:3344/application-test.yml
http://config3344.com:3344/application/dev/master
http://config3344.com:3344/master/application-dev

Config Client:
http://configclient3355.com:8201/config(profile=dev in bootstrap.yml)
http://configclient3355.com:8202/config(profile=test in bootstrap.yml)

Config fund provider:
http://localhost:2001/fund/get/166023
http://localhost:2001/fund/get/166999
http://localhost:2001/fund/list

Zipkin-server:
http://127.0.0.1:4001/

SpringCloud Bus:
start 3344,3355,3366 service
http://configclient3355.com:8201/config
http://configclient3366.com:8202/config

https://www.cnblogs.com/rmxd/p/11586667.html
https://blog.csdn.net/wtdm_160604/article/details/83720391

RabbitMQ:
C:\rabbitmq_server-3.8.2\sbin\rabbitmq-server.bat
http://localhost:15672
Redis:
1.C:\Redis-x64-3.2.100\redis.windows.conf requirepass root
2.C:\Redis-x64-3.2.100->redis-server.exe redis.windows.conf
127.0.0.1:6379
3.redis-cli -h 127.0.0.1
4.config set requirepass "root"
5.auth root
6.keys *(AUTH root)
7.shutdown,exit

ES:
C:\elasticsearch-7.5.0\bin\elasticsearch.bat
C:\elasticsearch\master\elasticsearch-7.5.0\bin elasticsearch.bat
C:\elasticsearch\slave\slave1\elasticsearch-7.5.0\bin elasticsearch.bat
C:\elasticsearch\slave\slave2\elasticsearch-7.5.0\bin elasticsearch.bat
C:\elasticsearch\elasticsearch-head
npm install
npm run start
http://localhost:9100/
http://localhost:9200/
Health:
http://127.0.0.1:9200/_cluster/health
{
    "cluster_name": "elasticsearch",
    "status": "yellow",
    "timed_out": false,
    "number_of_nodes": 3,
    "number_of_data_nodes": 3,
    "active_primary_shards": 3,
    "active_shards": 3,
    "relocating_shards": 0,
    "initializing_shards": 0,
    "unassigned_shards": 3,
    "delayed_unassigned_shards": 0,
    "number_of_pending_tasks": 0,
    "number_of_in_flight_fetch": 0,
    "task_max_waiting_in_queue_millis": 0,
    "active_shards_percent_as_number": 50.0
}
http://127.0.0.1:9200/_cluster/health?level=indices
{
    "cluster_name": "elasticsearch",
    "status": "yellow",
    "timed_out": false,
    "number_of_nodes": 3,
    "number_of_data_nodes": 3,
    "active_primary_shards": 3,
    "active_shards": 3,
    "relocating_shards": 0,
    "initializing_shards": 0,
    "unassigned_shards": 3,
    "delayed_unassigned_shards": 0,
    "number_of_pending_tasks": 0,
    "number_of_in_flight_fetch": 0,
    "task_max_waiting_in_queue_millis": 0,
    "active_shards_percent_as_number": 50.0,
    "indices": {
        "doit": {
            "status": "yellow",
            "number_of_shards": 3,
            "number_of_replicas": 1,
            "active_primary_shards": 3,
            "active_shards": 3,
            "relocating_shards": 0,
            "initializing_shards": 0,
            "unassigned_shards": 3
        }
    }
}
http://127.0.0.1:9200/_cluster/health?level=shards
{
    "cluster_name": "elasticsearch",
    "status": "yellow",
    "timed_out": false,
    "number_of_nodes": 3,
    "number_of_data_nodes": 3,
    "active_primary_shards": 3,
    "active_shards": 3,
    "relocating_shards": 0,
    "initializing_shards": 0,
    "unassigned_shards": 3,
    "delayed_unassigned_shards": 0,
    "number_of_pending_tasks": 0,
    "number_of_in_flight_fetch": 0,
    "task_max_waiting_in_queue_millis": 0,
    "active_shards_percent_as_number": 50.0,
    "indices": {
        "doit": {
            "status": "yellow",
            "number_of_shards": 3,
            "number_of_replicas": 1,
            "active_primary_shards": 3,
            "active_shards": 3,
            "relocating_shards": 0,
            "initializing_shards": 0,
            "unassigned_shards": 3,
            "shards": {
                "0": {
                    "status": "yellow",
                    "primary_active": true,
                    "active_shards": 1,
                    "relocating_shards": 0,
                    "initializing_shards": 0,
                    "unassigned_shards": 1
                },
                "1": {
                    "status": "yellow",
                    "primary_active": true,
                    "active_shards": 1,
                    "relocating_shards": 0,
                    "initializing_shards": 0,
                    "unassigned_shards": 1
                },
                "2": {
                    "status": "yellow",
                    "primary_active": true,
                    "active_shards": 1,
                    "relocating_shards": 0,
                    "initializing_shards": 0,
                    "unassigned_shards": 1
                }
            }
        }
    }
}
Download curl, put curl.exe to C:\Windows\System32
Solved health value is red or yellow:
curl -XGET localhost:9200/_cat/shards?h=index,shard,prirep,state,unassigned.reason
doit 2 p STARTED
doit 2 r UNASSIGNED CLUSTER_RECOVERED
doit 1 p STARTED
doit 1 r UNASSIGNED CLUSTER_RECOVERED
doit 0 p STARTED
doit 0 r UNASSIGNED CLUSTER_RECOVERED
curl "localhost:9200/_cat/shards?v"
index shard prirep state      docs store ip        node
doit  2     p      STARTED       0  283b 127.0.0.1 master
doit  2     r      UNASSIGNED
doit  1     p      STARTED       0  283b 127.0.0.1 master
doit  1     r      UNASSIGNED
doit  0     p      STARTED       0  283b 127.0.0.1 master
doit  0     r      UNASSIGNED
C:\>curl -XGET localhost:9200/_cluster/allocation/explain?pretty
{
  "index" : "doit",
  "shard" : 2,
  "primary" : false,
  "current_state" : "unassigned",
  "unassigned_info" : {
    "reason" : "CLUSTER_RECOVERED",
    "at" : "2020-04-11T02:39:10.629Z",
    "last_allocation_status" : "no_attempt"
  },
  "can_allocate" : "no",
  "allocate_explanation" : "cannot allocate because allocation is not permitted to any of the nodes",
  "node_allocation_decisions" : [
    {
      "node_id" : "SUQPgmGNSBmhjKSs67G4uQ",
      "node_name" : "master",
      "transport_address" : "127.0.0.1:9300",
      "node_attributes" : {
        "ml.machine_memory" : "17015447552",
        "xpack.installed" : "true",
        "ml.max_open_jobs" : "20"
      },
      "node_decision" : "no",
      "deciders" : [
        {
          "decider" : "same_shard",
          "decision" : "NO",
          "explanation" : "the shard cannot be allocated to the same node on which a copy of the shard already exists [[doit][2], node[SUQPgmGNSBmhjKSs67G4uQ], [P], s[STARTED], a[id=AjJwxjwmQyulhv-dnF7jkA]]"
        },
        {
          "decider" : "disk_threshold",
          "decision" : "NO",
          "explanation" : "the node is above the low watermark cluster setting [cluster.routing.allocation.disk.watermark.low=85%], using more disk space than the maximum allowed [85.0%], actual free: [7.641921141431713%]"
        }
      ]
    },
    {
      "node_id" : "os7bA9cQTHKf71jVVmMAHg",
      "node_name" : "slave1",
      "transport_address" : "127.0.0.1:9301",
      "node_attributes" : {
        "ml.machine_memory" : "17015447552",
        "ml.max_open_jobs" : "20",
        "xpack.installed" : "true"
      },
      "node_decision" : "no",
      "deciders" : [
        {
          "decider" : "disk_threshold",
          "decision" : "NO",
          "explanation" : "the node is above the low watermark cluster setting [cluster.routing.allocation.disk.watermark.low=85%], using more disk space than the maximum allowed [85.0%], actual free: [7.641921141431713%]"
        }
      ]
    },
    {
      "node_id" : "rxCpJp_dSvyAd_XAey2TOA",
      "node_name" : "slave2",
      "transport_address" : "127.0.0.1:9302",
      "node_attributes" : {
        "ml.machine_memory" : "17015447552",
        "ml.max_open_jobs" : "20",
        "xpack.installed" : "true"
      },
      "node_decision" : "no",
      "deciders" : [
        {
          "decider" : "disk_threshold",
          "decision" : "NO",
          "explanation" : "the node is above the low watermark cluster setting [cluster.routing.allocation.disk.watermark.low=85%], using more disk space than the maximum allowed [85.0%], actual free: [7.641921141431713%]"
        }
      ]
    }
  ]
}
1.
localhost:9200/doit/_settings
{
  "settings": {
    "index.unassigned.node_left.delayed_timeout": "5m"
  }
}
2.
curl -H "Content-Type: application/json" -XPUT "localhost:9200/_cluster/settings?pretty" -d "{"""transient""":{"""cluster.routing.allocation.enable""":"""all"""}}"
{
  "acknowledged" : true,
  "persistent" : { },
  "transient" : {
    "cluster" : {
      "routing" : {
        "allocation" : {
          "enable" : "all"
        }
      }
    }
  }
}
3.
curl -H "Content-Type: application/json" -XPUT "localhost:9200/doit/_settings?pretty" -d "{ """"number_of_replicas""":2}"
{
  "acknowledged" : true
}
4.
curl -H "Content-Type: application/json" -XPOST "localhost:9200/_cluster/reroute?pretty" -d "{"""commands""":[{"""allocate_empty_primary""":{"""index""":"""doit""","""shard""":0,"""node""":"""slave1""","""accept_data_loss""":"""true"""}}]}"
{
  "error" : {
    "root_cause" : [
      {
        "type" : "illegal_argument_exception",
        "reason" : "[allocate_empty_primary] primary [doit][0] is already assigned"
      }
    ],
    "type" : "illegal_argument_exception",
    "reason" : "[allocate_empty_primary] primary [doit][0] is already assigned"
  },
  "status" : 400
}
5.curl -H "Content-Type: application/json" -XPUT "localhost:9200/_cluster/settings" -d "{"""transient""":{"""cluster.routing.allocation.disk.watermark.low""":"""90%"""}}"
{"acknowledged":true,"persistent":{},"transient":{"cluster":{"routing":{"allocation":{"disk":{"watermark":{"low":"90%"}}}}}}}
cassandra的安装
https://blog.csdn.net/sunxiaoju/article/details/83817586 
SpringBatch-chunk:
http://localhost:6001/invokejob
JobParametersDemoConfiguration:
Right click this file->Run as->Run Configurations->Arguments->program arguments:info=myInfoFromClient

elasticsearch-service.bat后面还可以执行这些命令
install: 安装Elasticsearch服务
remove: 删除已安装的Elasticsearch服务（如果启动则停止服务）
start: 启动Elasticsearch服务（如果已安装）
stop: 停止服务（如果启动）
manager:启动GUI来管理已安装的服务

Zookper:
C:\apache-zookeeper-3.6.1\apache-zookeeper-3.6.1\bin\zkServer.sh
C:\apache-zookeeper-3.6.1\apache-zookeeper-3.6.1\bin\zkServer.cmd
C:\apache-zookeeper-3.6.1\apache-zookeeper-3.6.1\bin\zkCli.cmd -server 127.0.0.1:2181
[zk: localhost:2181(CONNECTED) 0]
Search all nodes:
ls /
Search status of node:
stat /zookeeper
Delete node:
delete /katy

Kafka:
kafka_2.10-0.8.2-beta.jar这样的文件，其中2.10是Scala版本，0.8.2-beta是Kafka版本。
打开Windows PowerShell
C:\kafka_2.12-2.3.0下，
.\bin\windows\kafka-server-start.bat .\config\zookeeper.properties
.\bin\windows\kafka-server-start.bat .\config\server.properties
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic summer
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic details
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic ordermain
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic orderdetail
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user
.\bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181 --topic details
.\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list
__consumer_offsets
summer
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic summer
.\bin\windows\kafka-console-consumer.bat –-bootstrap-server localhost:9092 –-topic summer -–from-beginning(consumer or search groupId)
.\bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --list(find consumer)
.\bin\windows\kafka-consumer-groups.bat --describe --bootstrap-server localhost:9092 --group s10(See offset)			 
.\bin\windows\kafka-console-consumer.bat --topic __consumer_offsets --bootstrap-server localhost:9092 --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" --consumer.config .\config\consumer.properties --from-beginning
.\bin\windows\kafka-configs.bat --zookeeper localhost:2181 --describe --entity-type topics --entity-name summer(See delete policy-save date,store size,offset)
hadoop:
C:\hadoop-2.8.3\sbin
start-all.cmd
jps
http://localhost:8088/cluster
http://localhost:50070/dfshealth.html#tab-overview
直接用zipkin,是通过http将数据传给zipkin server的，可以用RabbitMQ传输
因为现在zipkin的调用数据都是存在内存中的，一旦zipkin server重启，则意味着之前的都没有了，在这并发高的，一会就把内存挤爆了，所以最终zipkin的数据是要持久化的，要么mysql，
这里采用ES,毕竟在大数据检索面前ES比mysql好很多很多
还有在页面请求量大的时候zipkin和ES直接联通存数据，肯定会阻塞，这里就用kafka来解决这个问题

消息中间的几大应用场景
1、异步处理
比如用户在电商网站下单，下单完成后会给用户推送短信或邮件，发短信和邮件的过程就可以异步完成。因为下单付款是核心业务，发邮件和短信并不属于核心功能，并且可能耗时较长，所以针对这种业务场景可以选择先放到消息队列中，有其他服务来异步处理。

2、应用解耦：
假设公司有几个不同的系统，各系统在某些业务有联动关系，比如 A 系统完成了某些操作，需要触发 B 系统及 C 系统。如果 A 系统完成操作，主动调用 B 系统的接口或 C 系统的接口，可以完成功能，但是各个系统之间就产生了耦合。
用消息中间件就可以完成解耦，当 A 系统完成操作将数据放进消息队列，B 和 C 系统去订阅消息就可以了。这样各系统只要约定好消息的格式就好了。

3、流量削峰
比如秒杀活动，一下子进来好多请求，有的服务可能承受不住瞬时高并发而崩溃，所以针对这种瞬时高并发的场景，在中间加一层消息队列，把请求先入队列，然后再把队列中的请求平滑的推送给服务，或者让服务去队列拉取。

4、日志处理
kafka 最开始就是专门为了处理日志产生的。
当碰到上面的几种情况的时候，就要考虑用消息队列了。如果你碰巧使用的是 RabbitMQ 或者 kafka ，而且同样也是在使用 Spring Cloud ，那可以考虑下用 Spring Cloud Stream。

Kafka和rocketMq的区别
https://www.jianshu.com/p/c474ca9f9430
https://www.cnblogs.com/ynyhl/p/11320797.html
https://blog.csdn.net/qq_36908872/article/details/102706889

SpringCloud Stream:
stream实现了消息中间件的使用，我们发现只有在两处地址和RabbitMQ有耦合，第一处是pom文件中的依赖，第二处是application.properties中的RabbitMQ的配置信息，而在具体的业务处理中并没有出现任何RabbitMQ相关的代码，这时如果我们要替换为Kafka的话我们只需要将这两处换掉即可，即实现了中间件和服务的高度解耦

Spring batch:

Spring Batch 是开发批处理程序的开源和轻量级的解决方案。在企业应用中，每天需要对大量不同的事务进行业务处理，这些业务处理应该自动化，不需要人为干预。这时批处理可以派上用场，通过对预先定义的数据集作为输入，对数据进行处理，最后产生数据输出并更新数据目标。

Listeners provided by SpringBatch, from Job level to Item level
JobExecutionListener(before...,after...)
StepExecutionListener(before...,after...)
ChunkListener(before...,after...,error...) 一批一批
ItemReadListener,ItemProcessListener,ItemWriteListener(before...,after...,error):读数据，处理数据，写数据

Reading data from DB:
pagination,JdbcPagingItemReader

Reading from flat files:
Set Lines to skip
Set resource
Set line tokenizer how to parse the line to get the FieldSet(similar to ResultSet)
Reading from XML file:StaxEventItemReader

多个文件就是把多个文件转成一个文件，用一个代理循环执行
Reader的异常处理:
ExecutionContext JobRepository
每次执行后都会把step的状态存储到JobRepository中，然后机器重启后会populate到ExecutionContext中，这样就会从某个failed的step往下重新执行
open():step开始的时候调用
update():step执行完事
close():all chunk of the data is done
开始open(),每次读10条数据(chunk=10)(Reader),然后处理这10条数据(Process)，然后写数据(Writer),成功了，此时调用update(),如果failed,不调用update(),在读10条数据，直到100，最侯调用close()
写入到数据库:
Neo4jItemWriter
MongoItemWriter
RepositoryItemWriter
HibernateItemWriter
JdbcBatchItemWriter
JpaItemWriter
GemfireItemWriter
错误重试:
faultTolerant()
retry()
retryLimit()

org.springframework.retry.RetryException: Non-skippable exception in recoverer while processing; nested exception is com.bjyt.springcloud.skip.CustomRetryableException: Process failed.Attempt: 1
That is correct behavior for Spring Batch's partitioning. The PartitionHandler in the master step evaluates the results of all steps at once when they have all returned (or timed out). With regards to what happened in the slaves, those logged errors would be a leading cause to me. However, the definitive answer should be in the job repository (assuming you're using a database backed implementation). When a step fails (even a partitioned slave), the exception is stored there.

Skip Listener:
不仅要跳过错误，还要把错误元素和错误信息记录下来用于以后分析

JobOperator:
JobOperator提供了比JobLauncher更好的功能,本质上是封装了jobLauncher,不像jobLauncher，是springBoot初始化好的，它需要我们自己提供jobParametersConverter,jobRepository等
1.Spring boot与Spring cloud't match
https://www.cnblogs.com/zhuwenjoyce/p/10261079.html
2.Install lomback
https://blog.csdn.net/yiyijianxian/article/details/80156910
java -jar lombok-1.16.22.jar

Flink：
https://flink.apache.org/
https://ci.apache.org/projects/flink/flink-docs-release-1.10/dev/projectsetup/java_api_quickstart.html
AkkaRpcService
mvn archetype:generate  -DarchetypeGroupId=org.apache.flink  -DarchetypeArtifactId=flink-quickstart-java  -DarchetypeVersion=1.10.0
mvn eclipse:clean
mvn eclipse:eclipse
socketTextStream
fromElements
fromCollection
fromParallelCollection
generateSequence
Kafka要用0.11以上的，最好用1.0.0来保证唯一性
1.cd C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\bin\ start-cluster.bat
2.http://localhost:8081/#/overview
3.C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\bin>flink.bat run ../examples/batch/WordCount.jar
                                                  flink.bat run D:\flink\flink-1.9.0\examples\batch\WordCount.jar -input D:\flink\flink-1.9.0\README.txt -output D:\flink\flink-1.9.0\README_CountWord_Result.txt
4.copy(cp) flink-shaded-hadoop-2-uber-2.7.5-10.0.jar to C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\lib
for i in {2..3}; do scp flink-shaded-hadoop-2-uber-2.7.5-10.0.jar node-$i.51doit.cn:$PWD;done
5.cd C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\bin\ stop-cluster.bat
6.cd C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\bin\ start-cluster.bat
7.jps
oneFlink support exact once:
1.Kafka can record offset by itself
2.Flink support rollback or override(after restart,rollback failed data)
Flink can manage Kafka's offset and store middle result
barring:the whole process end, submit/update the offset(even data submit the Redis, also don't update offset)
transaction:only update offset when transaction successful
Flink:state(状态):middle data/middle result, we can store these stata to a safe HDFS
https://blog.csdn.net/qq_15103197/article/details/82876688
StateBackEnd:
The state will be store to JobManager's memory default,also can store to TaskManager local file system, also can be stored to HDFS
CheckPointing:Triger store middle result to StateBackEnd system,it is executed Periodically
Flink UI->SavePoint path:continue computer from last checkpoint
Command->flink run -c calss -p parrel -m jobmanager address -s from savepoint
barring:The data will be divided many spice, for example,1,2,3,4, there is id match 1,2,3,4, if all of 1,2,3,4 successful, then the will updated offset and checkpoint will recover the data from offset, if any of 1,2,3,4 has exception, then won't update the offset,checkpoint won't recover the data from updated offset
SavePoint:point to special savepoint, then recover the data from this checkpoint path
SpringCloud DATA FLOW:
cd C:\Java\jdk1.8.0_251\bin
1.$ java -jar spring-cloud-skipper-server-1.1.2.RELEASE.jar
      http://localhost:8888
2.$ java -jar spring-cloud-dataflow-server-local-1.7.3.RELEASE.jar --spring.cloud.dataflow.features.skipper-enabled=true
3.$ java -jar spring-cloud-dataflow-shell-1.7.3.RELEASE.jar --dataflow.mode=skipper
  
  localhost:9393/dashboard
app register --name http --type source --uri maven://org.springframework.cloud.stream.app:http-source-rabbit:1.2.0.RELEASE
app register --name log --type sink --uri maven://org.springframework.cloud.stream.app:log-sink-rabbit:1.1.0.RELEASE
stream create --name httptest --definition "http --server.port=9000 | log" --deploy
http post --target http://localhost:9000 --data "hello world"
Log:C:\Users\ENZNHXX\AppData\Local\Temp\spring-cloud-deployer-5949404450957201234\httptest-1584177268041\httptest.log-v1\stderr_0.log
    C:\Users\ENZNHXX\AppData\Local\Temp\spring-cloud-deployer-5949404450957201234\httptest-1584177268041\httptest.log-v1\stdout_0.log
app register --name timestamp --type task --uri maven://org.springframework.cloud.task.app:timestamp-task:1.3.0.RELEASE
task create --name printTimeStamp --definition "timestamp"
TimestampTaskConfiguration$TimestampTask : 2018-02-28 16:42:21.051
TimestampTaskConfiguration$TimestampTask : 2020-03-14 17:39:49.281
log:C:\Users\ENZNHXX\AppData\Local\Temp\printTimeStamp4167791768298478222\240648731513031\printTimeStamp-02b42302-090a-40c9-b647-8ed718cb698f
    C:\Users\ENZNHXX\AppData\Local\Temp\printTimeStamp5938089572237300659\115190261003412\printTimeStamp-b2e1cfaf-4533-430d-bb34-c7b616754e6c

task launch printTimeStamp
task execution list
task execution status

Python:
set path=C:\Python27
print("Hello")
python --version

data_file_directories:
     - C:\apache-cassandra-3.11.6\data
commitlog_directory: C:\apache-cassandra-3.11.6\commitlog
saved_caches_directory: C:\apache-cassandra-3.11.6\saved_caches

C:\apache-cassandra-3.11.6\bin>cqlsh.bat -u 'cassandra' -p 'cassandra';
describe keyspaces
CREATE KEYSPACE clouddata WITH REPLICATION = { 'class' : 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1' } AND DURABLE_WRITES = true;
USE clouddata;
CREATE TABLE book  (id uuid PRIMARY KEY,isbn text,author text,title text);
describe tables

INSERT INTO user (id,user_name) VALUES (1,'sxj');
select * from user;
delete from user where id=2;

app import --uri https://dataflow.spring.io/kafka-maven-latest
stream create cassandrastream --definition "http --server.port=8888 --spring.cloud.stream.bindings.output.contentType='application/json' | cassandra --ingestQuery='insert into book (id, isbn, title, author) values (uuid(), ?, ?, ?)' --keyspace=clouddata" --deploy
http post --contentType 'application/json' --data '{"isbn": "1599869772", "title": "The Art of War", "author": "Sun Tzu"}' --target http://localhost:8888
DB:
Navicat
Docker:
https://hub.docker.com/editions/community/docker-ce-desktop-windows/->Get Docker
https://id.docker.com/
dlzhangna/dlzhangna
C:\DockerConfig\docker-compose.yml


set DATAFLOW_VERSION=1.7.3.RELEASE
docker-compose up
docker ps //查看当前运行中的容器
docker ps -a //查看所有运行过的容器
docker inspect containerId(容器ID或容器名)//查看对应容器的具体配置信息
docker port containerId //查看对应容器端口映射
docker run --name containerName -it -p 80:80 -d // --name是为容器取一个别名，-p 80:80是端口映射，将宿主机的80端口映射到容器的80端口上，-d是指后台运行容器，即容器启动后不会停止，-it是-i 和-t的合并，以交互模式运行容器。
docker images //查看所有镜像
docker exec -it containerName /bin/bash //进入已启动的容器内，新启一个进程，执行命令。
docker stop containerName // 停止一个容器
docker start -i containerName //重启启动一个运行过的容器
docker rm containerName //移除一个容器

阿里云:
https://dev.aliyun.com/search.html
dlMary/dl091208

Ubuntu:
检查：
1.Unix内核：uname -a
2.检查Device Mapper(存储驱动):ls -l /sys/class/misc/device-mapper
sudo /etc/init.d/ssh start
sudo ifconfig -a
cat /etc/issue:Ubuntu 18.04.4 LTS \n \l

eth3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
      inet 172.20.91.97  netmask 255.255.255.240  broadcast 172.20.91.111
find / -name docker.service	  
启动Docker服务：sudo service docker start
停止Docker服务：sudo service docker stop
重启Docker服务：sudo service docker restart

$vim ~/.bashrc
export DOCKER_HOST=tcp://localhost:4243

vi /etc/default/docker
vi /etc/systemd/system/multi-user.target.wants/docker.service

sudo systemctl daemon-reload
Gao De:
https://lbs.amap.com/console/show/picker
15640868521
Exception:
SEVERE: Shutdown broker because all log dirs in C:\kafka_2.12-2.3.0\kafka_2.12-2.3.0logs have failed
delete all the contents in kafka log and delete all the contents in the zoo.cfg dataDir
Big Data:
C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\myConfig\config.properties

MySQL:
1.Delete Data
C:\Program Files\MySQL\MySQL Server 8.0\data
2.Enter C:\Program Files\MySQL\MySQL Server 8.0\bin
mysqld --initialize
net start mysql
Canal:
C:\canal.deployer-1.1.5-SNAPSHOT\bin\startup.bat
MVN：
cd JDV-ST-OSS-RC-Provisioning
C:\ccbs-switch\st-ccbs-switch-code\JDV-ST-OSS-RC-Provisioning>mvn clean
C:\ccbs-switch\st-ccbs-switch-code\JDV-ST-OSS-RC-Provisioning>mvn install -Dmaven.test.skip=true