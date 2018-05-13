package com.hnust.wxsell.utils;

import java.util.Random;

/**
 * @author ZZH
 * @date 2018/4/3 0003 14:28
 **/
public class KeyUtil {
    /**
     * 生成唯一的主键
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
