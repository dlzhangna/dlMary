package com.bjyt.flink.test.canal;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;

public class CanalClientUser {

public static void main(String args[]) {
  InetSocketAddress add=new InetSocketAddress(AddressUtils.getHostIp(),11111);
  System.out.println(add.getHostName());
  System.out.println(add.getPort());
  InetAddress addr=add.getAddress();//获得端口的ip；
  System.out.println(addr.getHostAddress());//返回ip；
  System.out.println(addr.getHostName());//输出端口名；
  CanalConnector connector = CanalConnectors.newSingleConnector(add, "user", "", "");
  int batchSize = 1000;
  int emptyCount = 0;
  try {
       connector.connect();
       connector.subscribe(".*\\..*");
       connector.rollback();
       int totalEmtryCount = 1200;
       while (emptyCount < totalEmtryCount) {
          Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
          long batchId = message.getId();
          int size = message.getEntries().size();
          if (batchId == -1 || size == 0) {
            emptyCount++;
            System.out.println("empty count : " + emptyCount);
            try {
               Thread.sleep(1000);
                } catch (InterruptedException e) {
                	e.printStackTrace();
               }
            } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
            }
            connector.ack(batchId); // 提交确认
            //connector.rollback(batchId); // 处理失败, 回滚数据
            }
           System.out.println("empty too many times, exit");
         } finally {connector.disconnect();}
     }
	private static void printEntry(@NotNull List<Entry> entrys) {
      for (Entry entry : entrys) {
      if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
          continue;
        }
      RowChange rowChage = null;
      try {
           rowChage = RowChange.parseFrom(entry.getStoreValue());
       } catch (Exception e) {
         throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),e);
       }
      EventType eventType = rowChage.getEventType();
      System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
      entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
      entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
      eventType));
      for (RowData rowData : rowChage.getRowDatasList()) {
        if (eventType == EventType.DELETE) {
           printColumn(rowData.getBeforeColumnsList());
        } else if (eventType == EventType.INSERT) {
           printColumn(rowData.getAfterColumnsList());
        } else {
           System.out.println("-------> before");
           printColumn(rowData.getBeforeColumnsList());
           System.out.println("-------> after");
           printColumn(rowData.getAfterColumnsList());
             }
           }
        }
	 }
	private static void printColumn(@NotNull List<Column> columns) {
     for (Column column : columns) {
       System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
      }
	}
	/*
	 * CN-00121535.ericsson.se
       11111
       100.121.90.192
       CN-00121535.ericsson.se
       ================> binlog[mysql-bin.000004:367] , name[fund1,user] , eventType : INSERT
       uid : 3    update=true
       name : mary    update=true
       empty count : 1
       empty count : 2
       empty count : 3
       empty count : 4
       empty count : 5
       empty count : 6
       empty count : 7
       empty count : 8
       ================> binlog[mysql-bin.000004:655] , name[fund1,user] , eventType : INSERT
       uid : 4    update=true
       name : tony    update=true
       empty count : 1
       empty count : 2
       empty count : 3
       empty count : 4
       empty count : 5
       empty count : 6
       empty count : 7
       empty count : 8
       empty count : 9
	 */
}