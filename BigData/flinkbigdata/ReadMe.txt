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
C:\zookeeper\server1\apache-zookeeper-3.6.1\bin\zkServer.sh
C:\zookeeper\server1\apache-zookeeper-3.6.1\bin\zkServer.cmd
C:\zookeeper\server1\apache-zookeeper-3.6.1\bin\zkCli.cmd -server 127.0.0.1:2181
[zk: localhost:2181(CONNECTED) 0]

C:\zookeeper\server2\apache-zookeeper-3.6.1\bin\zkServer.sh
C:\zookeeper\server2\apache-zookeeper-3.6.1\bin\zkServer.cmd
C:\zookeeper\server2\apache-zookeeper-3.6.1\bin\zkCli.cmd -server 127.0.0.1:2182
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
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic ordermain
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic ordermain
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic orderdetail
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic orderdetail
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic actdic
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user
.\bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181 --topic details
.\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list
__consumer_offsets
summer
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic summer
.\bin\windows\kafka-console-consumer.bat –-bootstrap-server localhost:9092 –-topic summer -–from-beginning(consumer or search groupId)
.\bin\windows\kafka-console-consumer.bat –-bootstrap-server localhost:9092 –-topic ordermain -–from-beginning
.\bin\windows\kafka-console-consumer.bat –-bootstrap-server localhost:9092 –-topic orderdetail -–from-beginning
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


MySQL:
1.Delete Data
C:\Program Files\MySQL\MySQL Server 8.0\data
2.Enter C:\Program Files\MySQL\MySQL Server 8.0\bin
mysqld --initialize
net start mysql
Canal:
C:\canal.deployer-1.1.5-SNAPSHOT\bin\startup.bat
