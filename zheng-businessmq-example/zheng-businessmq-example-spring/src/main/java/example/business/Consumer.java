package example.business;

import businessmq.consumer.AbstractConsumer;
import common.utility.DateUtility;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/27.
 */
public class Consumer implements AbstractConsumer {
    public void work(String s) {
        System.out.print("当前消息"+s+ DateUtility.getStrFromDate(new Date(),""));
    }
}
