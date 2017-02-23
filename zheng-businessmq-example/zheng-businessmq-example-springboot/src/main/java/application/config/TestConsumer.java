package application.config;

import businessmq.consumer.AbstractConsumer;
import common.utility.DateUtility;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/10.
 */
public class TestConsumer implements AbstractConsumer {
    public void work(String s) {
        System.out.print(s+ DateUtility.getStrFromDate(new Date(),""));
    }
}
