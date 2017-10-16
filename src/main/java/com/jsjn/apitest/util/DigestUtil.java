package com.jsjn.apitest.util;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 生成摘要用到的工具类
 * <p>
 * Created by yincongyang on 17/10/16.
 */
public class DigestUtil {
    /**
     * 等于符号标志
     */
    private static final String EQ_SYMBOL = "=";

    /**
     * 并且符号标志
     */
    private static final String AND_SYMBOL = "&";

    /**
     * javaBean转化为Map<String,String>
     *
     * @param obj javaBean
     * @return 返回转化过的Map
     * @throws Exception
     */
    private static Map<String, String> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object fieldObj = field.get(obj);
            String fieldStr = "";
            if (null != fieldObj) {
                fieldStr = fieldObj.toString();
            }
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            map.put(field.getName(), fieldStr);
        }

        return map;
    }

    /**
     * 将map按key字符串排序的treeMap
     *
     * @param map
     * @param keys
     * @return
     */
    private static TreeMap<String, String> treeMap(Map<String, String> map,
                                                   String... keys) {
        // 初始化字符串比较器
        Comparator<String> stringComparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        TreeMap<String, String> treeMap = new TreeMap<String, String>(
                stringComparator);
        treeMap.putAll(map);
        // 移除非摘要的key
        for (String key : keys) {
            treeMap.remove(key);
        }
        return treeMap;
    }

    /**
     * 将map转成a1=b1&a2=b2&a3=b3形式的字符串
     *
     * @param map map
     * @return string
     */
    public static String mapToString(Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue() == null ? "" : entry.getValue()
                    .trim();
            result.append(entry.getKey()).append(EQ_SYMBOL).append(value)
                    .append(AND_SYMBOL);
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString().trim();
    }

    /**
     * 1.根据key对传来的map数据排序,排除某些字符串Key值
     * 2.生成a1=b1&a2=b2&a3=b3形式的字符串
     * 3.根据规则返回待加签报文
     *
     * @param requsetType 请求类型，GET/POST
     * @param URI 请求的URl，如：/v1/custom/reconciliation_file_ready_notify
     * @param obj 需要排序的javaBean
     * @param keys 不参加排序的字段：默认为：signature
     * @return 返回待加签报文
     * @throws Exception
     */
    public static String buildSourceStr(String requsetType, String URI, Object obj, String... keys) throws Exception {

        //将除“signature”外的所有参数按key进行字典升序排列，并将排序后的参数(key=value)用&拼接起来
        Map<String, String> map = DigestUtil.objectToMap(obj);
        TreeMap<String, String> treeMap = treeMap(map, keys);
        String sortParams = mapToString(treeMap);
        System.out.println("排序得到的参数串为： "+sortParams);

        //按照requsetType + Encodes.urlEncode(url) + Encodes.urlEncode(sortParams) 顺序组装待加签报文
        String sourceStr = requsetType + Encodes.urlEncode(URI) + Encodes.urlEncode(sortParams);
        System.out.println("待加签报文为： "+sourceStr);

        return sourceStr;
    }
}
