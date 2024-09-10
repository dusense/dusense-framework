package net.dusense.framework.core.tool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class CommonFunction {
    /** 把目标对像转成字符串 */
    public static String convert(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 去掉日期格式中的T +80
     *
     * @return
     */
    public static String convetDate(Object obj) {
        String date = convert(obj);
        return date.replaceAll("T", " ").replace("T", " ").replaceAll("\\+[\\w\\d\\:]*$", "");
    }

    /**
     * 把目标对像转成整数
     *
     * @param obj
     */
    public static int parseInt(Object obj) {
        String value = convert(obj);
        if ("".equals(value)) {
            return 0;
        }
        try {
            double tmp = Double.parseDouble(value);
            return (int) tmp;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 把目标对像转成数字
     *
     * @param obj
     */
    public static double parseDouble(Object obj) {
        String value = convert(obj);
        if ("".equals(value)) {
            return 0;
        }
        try {
            double tmp = Double.parseDouble(value);
            return tmp;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 把目标对像转成数字保留小数点后几位
     *
     * @param obj
     */
    public static double parseDouble(Object obj, int size) {
        String value = convert(obj);
        if ("".equals(value)) {
            return 0;
        }
        try {
            String suff = "00000000";
            double tmp = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#." + suff.substring(0, size));
            tmp = Double.parseDouble(df.format(tmp));
            return tmp;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 把目标对像转成数字保留小数点后几位
     *
     * @param obj
     */
    public static double parseDoubleg(Object obj, int size) {
        String value = convert(obj);
        if ("".equals(value)) {
            return 0;
        }
        try {
            String suff = "00000000";
            double tmp = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#." + suff.substring(0, size));
            value = df.format(tmp / 1024);
            tmp = Double.parseDouble(value);
            return tmp;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 返回对应该MAP中的对象
     *
     * @param map 存放参数的map对象
     * @param path 路径
     * @return 返回拼接好的url
     */
    public static Object getPathObject(Map map, String path) {
        String paths[] = path.split("/");
        Object obj = map;
        for (int i = 0; i < paths.length; i++) {
            obj = getOjbect(obj, paths[i]);
        }
        return obj;
    }

    public static Object getOjbect(Object obj, String name) {
        Object tmpobj = obj;
        if (obj == null) return null;
        if (tmpobj instanceof Map) {
            tmpobj = ((Map) tmpobj).get(name);
        }
        if (tmpobj instanceof List) {
            tmpobj = ((List) tmpobj).get(0);
        }
        return tmpobj;
    }

    /**
     * 组装url 默认不拼接空值
     *
     * @param map 存放参数的map对象
     * @param names 需要拼结的参数名
     * @return 返回拼接好的url
     */
    public static String createUrl(Map map, String names) {
        return createUrl(map, names, false);
    }

    /**
     * 组装url 默认不拼接空值
     *
     * @param map 用于存放参数的map
     * @param alis 名称和别名的对应
     * @return 返回拼接好的url
     */
    public static String createUrl(Map map, Map alis) {
        return createUrl(map, alis, false);
    }

    /**
     * 组装url
     *
     * @param map 需要拼装的参数
     * @param names map 参数中需要拼装的字段
     * @param b 是否拼装空值
     * @return 返回拼装好的URL
     */
    public static String createUrl(Map map, String names, boolean b) {
        String name[] = names.split(",");
        StringBuffer sb = new StringBuffer("");
        String tmpName = "";
        String value = "";
        for (int i = 0; i < name.length; i++) {
            tmpName = name[i];
            value = convert(map.get(tmpName));
            if (!"".equals(value) || b) {
                sb.append("&" + tmpName + "=" + value);
            }
        }
        return sb.toString();
    }

    /**
     * 组装url
     *
     * @param map 输入的参数
     * @param alis 名称和别名的对应
     * @param b 是否拼装空值
     * @return 拼装好的url
     */
    public static String createUrl(Map map, Map alis, boolean b) {

        StringBuffer sb = new StringBuffer("");
        Set names = alis.keySet();
        Iterator it = names.iterator();
        String tmpName = "";
        String value = "";
        while (it.hasNext()) {
            tmpName = (String) it.next();
            value = convert(map.get(alis.get(tmpName)));
            if (!"".equals(value) || b) {
                sb.append("&" + tmpName + "=" + value);
            }
        }
        return sb.toString();
    }

    /**
     * 组装url
     *
     * @param pix 前辍
     * @param index 序号
     * @param param 需要拼装的参数和值 是否拼装空值
     * @return 拼装速有顺号的URL
     */
    public static String createIndexUrl(String pix, int index, Map param) {
        return createIndexUrl(pix, index, param, false);
    }

    /**
     * 组装url
     *
     * @param pix 前辍
     * @param index 序号
     * @param param 需要拼装的参数和值
     * @param b 是否拼装空值
     * @return 拼装速有顺号的URL
     */
    public static String createIndexUrl(String pix, int index, Map param, boolean b) {

        StringBuffer sb = new StringBuffer("");
        Set names = param.keySet();
        Iterator it = names.iterator();
        String tmpName = "";
        String value = "";
        while (it.hasNext()) {
            tmpName = (String) it.next();
            value = convert(param.get(tmpName));
            if (!"".equals(value) || b) {
                sb.append("&" + pix + index + "." + tmpName + "=" + value);
            }
        }
        return sb.toString();
    }

    public static String parseResultStr(String strUrl) {
        URL url;
        String map = null;
        HttpURLConnection conn = null;
        try {
            log.debug("Common request url:" + strUrl);
            long startTime = System.currentTimeMillis();
            url = new URL(strUrl);

            conn = (HttpURLConnection) url.openConnection();
            InputStream isResult = conn.getInputStream();
            long endTime = System.currentTimeMillis();
            log.debug("Common request 共用： " + (endTime - startTime) + "毫秒");
            // 放到map中
            byte[] rsBytes = new byte[4]; // IOUtils.toByteArray(isResult);
            map = new String(rsBytes);
            isResult.close();
        } catch (Exception e) {
            log.error("CommonFunction parseUrl Error!", e);
        } finally {
            if (conn != null) {
                // conn = null;
                conn.disconnect();
            }
        }
        return map;
    }

    /**
     * 依据XML a/b[key=value][key2=value]/b 等模式路径进行查找 取得返回原生数据类型结构
     *
     * @param map
     * @param xmlPath
     * @return
     */
    public static Object getObjByPath2(Map map, String xmlPath) {

        if (map != null && xmlPath != null) {
            int spindex = xmlPath.indexOf("/");
            String newXmlPath = null;
            String nodeName = null;
            Map conditions = null;
            // 针对以'/' 字符开始得操作去除之
            while (spindex == 0) {
                xmlPath = xmlPath.substring(1);
                spindex = xmlPath.indexOf("/");
                if (spindex == 0) continue;
                else break;
            }

            if (spindex > 0) {
                nodeName = xmlPath.substring(0, spindex);
                newXmlPath = xmlPath.substring(spindex + 1);
            } else if (xmlPath.length() > 0) {

                nodeName = xmlPath;
            }

            // 对当前Node中的属性过滤的操作 请以[key=value][key2=value]为模式条件
            if (nodeName != null) {
                conditions = getFilterConditons(nodeName);
                if (!conditions.isEmpty()) {
                    nodeName = nodeName.replaceAll("\\[([^\\[]*)\\]", "");
                }
            }

            if (map instanceof Map) {
                Object nodeObj = map.get(nodeName);
                if (newXmlPath != null && !"".equalsIgnoreCase(newXmlPath)) {
                    if (nodeObj instanceof List) {
                        List list = (List) nodeObj;
                        if (!conditions.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                Map tempMap = (Map) list.get(i);
                                Iterator iterator = conditions.keySet().iterator();
                                boolean isMatched = false;
                                while (iterator.hasNext()) {
                                    String conditonKey = (String) iterator.next();
                                    String conditionValue = (String) conditions.get(conditonKey);
                                    if (conditionValue.equalsIgnoreCase(
                                            (String) tempMap.get(conditonKey))) {
                                        isMatched = true;
                                    } else {
                                        isMatched = false;
                                        break; // 对任何and条件无法匹配去除之
                                    }
                                }
                                if (isMatched) {
                                    map = tempMap;
                                    break;
                                } else {
                                    map = null;
                                }
                            }
                        } else if (!list.isEmpty()) map = (Map) list.get(list.size() - 1);
                        else map = null;

                    } else if (nodeObj instanceof Map) {
                        map = (Map) nodeObj;
                    } else {
                        map = null;
                        System.out.println("UNKNOW OBJECT TYPE!!");
                    }

                } else {
                    /**
                     * 原生数据不应处理类型 if(nodeObj instanceof List ){ List nodeList=(List)nodeObj;
                     * if(nodeList.size()==1) nodeObj=nodeList.get(0); }
                     */
                    if (nodeObj instanceof List) {
                        List list = (List) nodeObj;
                        if (!conditions.isEmpty()) {
                            List newResult = new ArrayList();
                            for (int i = 0; i < list.size(); i++) {
                                Map tempMap = (Map) list.get(i);
                                Iterator iterator = conditions.keySet().iterator();
                                boolean isMatched = false;
                                while (iterator.hasNext()) {
                                    String conditonKey = (String) iterator.next();
                                    String conditionValue = (String) conditions.get(conditonKey);
                                    if (conditionValue.equalsIgnoreCase(
                                            (String) tempMap.get(conditonKey))) {
                                        isMatched = true;
                                    } else {
                                        isMatched = false;
                                        break; // 对任何and条件无法匹配去除之
                                    }
                                }
                                if (isMatched) {
                                    newResult.add(tempMap);
                                }
                            }
                            nodeObj = newResult;
                        }
                    }
                    return nodeObj;
                }
            } else {
                return null;
            }
            return getObjByPath2(map, newXmlPath);
        } else {
            return map;
        }
    }

    /**
     * 依据XML a/b[key=value][key2=value]/b 等模式路径进行查找map 的结构对象
     *
     * @param map
     * @param xmlPath
     * @return
     */
    public static Object getObjByPath(Object map, String xmlPath) {

        if (map != null && xmlPath != null) {
            int spindex = xmlPath.indexOf("/");
            String newXmlPath = null;
            String nodeName = null;
            Map conditions = null;
            if (spindex > 0) {
                nodeName = xmlPath.substring(0, spindex);
                newXmlPath = xmlPath.substring(spindex + 1);
            } else if (xmlPath.length() > 0) {
                // 针对最后1个'/' 字符操作
                nodeName = xmlPath;
            }
            // 对当前Node中的属性过滤的操作 请以[key=value][key2=value]为模式条件
            if (nodeName != null) {
                conditions = getFilterConditons(nodeName);
                if (!conditions.isEmpty()) {
                    nodeName = nodeName.replaceAll("\\[([^\\[]*)\\]", "");
                }
            }

            if (map instanceof Map) {
                map = ((Map) map).get(nodeName);
                if (map instanceof List) {
                    List list = (List) map;
                    if (!conditions.isEmpty() && !list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            Map tempMap = (Map) list.get(i);
                            Iterator iterator = conditions.keySet().iterator();
                            boolean isMatched = false;
                            while (iterator.hasNext()) {
                                String conditonKey = (String) iterator.next();
                                String conditionValue = (String) conditions.get(conditonKey);
                                if (conditionValue.equalsIgnoreCase(
                                        (String) tempMap.get(conditonKey))) {
                                    isMatched = true;
                                } else {
                                    isMatched = false;
                                    break; // 对任何and条件无法匹配去除之
                                }
                            }
                            if (isMatched) {
                                map = tempMap;
                                break;
                            } else {
                                map = null;
                            }
                        }
                    } else {
                        if (!list.isEmpty()) map = list.get(list.size() - 1);
                        else map = null;
                    }
                }
            } else {
                return null;
            }

            return getObjByPath(map, newXmlPath);
        } else {
            return map;
        }
    }

    /**
     * 取得[key=value][key2=value]...模式的条件结果集
     *
     * @param sourceStr
     * @return
     */
    public static Map getFilterConditons(String sourceStr) {
        Map conditons = new HashMap();
        Pattern pattern = Pattern.compile("\\[([^\\[]*)\\]");
        Matcher matcher = pattern.matcher(sourceStr);
        while (matcher.find()) {
            String condition = matcher.group();
            if (!"".equalsIgnoreCase(condition) && condition.length() > 3) {
                condition = condition.substring(1, condition.length() - 1);
                String tem[] = condition.split("=");
                if (tem.length == 2) conditons.put(tem[0], tem[1]);
            }
        }

        return conditons;
    }

    /** 返回LIST中的值 */
    @SuppressWarnings("unchecked")
    public static String getListValue(List list, String key) {
        String value = "";
        Map map;
        try {
            for (int i = 0; i < list.size(); i++) {
                map = (Map) list.get(i);
                if (key.equals(map.get("description"))) {
                    value = (String) map.get("value");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return value;
    }

    /** 用指定符号分割集合 */
    public static String listJoin(List list, String char_) {
        String str = "";
        try {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) str += list.get(i);
                else str += char_ + list.get(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return str;
    }

    /** 过滤掉LIST中不要的值 */
    public static List getListFilter(List list) {
        List paramList = new ArrayList();
        Map map;
        for (int i = 0; list.size() < i; i++) {
            map = (Map) list.get(i);
            paramList.add(map.get("SRCATTRVAL"));
        }
        return paramList;
    }

    /**
     * 是否匹配字符串与正则表达式
     *
     * @param strSource
     * @param regex
     * @return
     */
    public static boolean matchStr(String strSource, String regex) {
        if (!StringUtils.isEmpty(strSource) && !StringUtils.isEmpty(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(strSource);
            return matcher.matches();
        }
        return false;
    }

    public static void copyProperties(Map<String, Object> dest, Object obj) {

        if (obj == null) {
            return;
        }
        dest = dest == null ? new HashMap<String, Object>() : dest;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                dest.put(key, value);
            }
            // 过滤class属性
            dest.remove("class");

        } catch (Exception e) {
            System.out.println("copyProperties Error " + e);
        }
    }
}
