package com.yougou.wfx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;

public class CsvUtils {

	public LabeledCSVParser csvParser; // csv解析器，对于第一行的表头信息，自动加载为索引关键字

    private int currLineNum = -1; // 文件所读到行数

    private String[] currLine = null; // 用来存放当前行的数据

    /*
     * 构造函数， Param: in InputStream 要解析的信息流 throws IOException
     */

    public CsvUtils(InputStream in) throws IOException {

        csvParser = new LabeledCSVParser(new ExcelCSVParser(in));
        currLineNum = csvParser.getLastLineNumber();
    }

    public CsvUtils(InputStreamReader in) throws IOException {

        csvParser = new LabeledCSVParser(new ExcelCSVParser(in));
        currLineNum = csvParser.getLastLineNumber();
    }
    /*
     * 检查是否还有数据 return ture 还有一行数据，false 没有数据
     */
    public boolean hasMore() throws IOException {
        currLine = csvParser.getLine();
        currLineNum = csvParser.getLastLineNumber();
        if (null == currLine) return false;
        return true;
    }

    /*
     * 返回当前行数据，关键字所指向的数据 param:String filedName 该行的表头 return:String 返回当前行数据，关键字所指向的数据
     */
    public Object getByFieldName(String fieldName, Class classtype) throws Exception {
        if (classtype.equals(Integer.class)) {
            String number = csvParser.getValueByLabel(fieldName);
            Object onumber=!StringUtils.isNotEmpty(number) ? new Integer(0) : new Integer(number);
            return onumber;
        }
        return csvParser.getValueByLabel(fieldName);
    }

    public String getByFieldName(String fieldName) {

        return csvParser.getValueByLabel(fieldName);
    }

    /*
     * 关闭解析器
     */
    public void close() throws IOException {
        csvParser.close();

    }

    /*
     * 读取当前行数据 return String[] 读取当前行数据
     */
    public String[] readLine() throws IOException {
        currLine = csvParser.getLine();

        currLineNum = csvParser.getLastLineNumber();

        return currLine;

    }

    public int getCurrLineNum() {

        return currLineNum;

    }

    /**
     * 通用获取所有的货品库存信息
     * 方法描述：TODO 方法实现描述 
     * @author 李准 2011-6-12 上午10:27:01
     * @date 2011-6-12 上午10:27:01
     * 
     * @param classpath
     * @param classvo
     * @return
     * @throws Exception
     */
    public List<Object> toJdbcParamList(String classpath, Class classvo) throws Exception {
        List<Object> items = new ArrayList<Object>();
        Field[] fields = classvo.getDeclaredFields();

        Object obj = null;
        while (hasMore()) {
            obj = classvo.newInstance();
            for (Field f : fields) {
                Method m = classvo.getMethod(getFiledMethodName(f.getName(), classvo), f.getType());
                m.invoke(obj, getByFieldName(f.getName(), f.getType()));
            }
            items.add(obj);
        }
        close();
        return items;
    }

    public List<Object> toJdbcParamList() throws Exception {
        List<Object> items = new ArrayList<Object>();
        Boolean flag = true;
        while (flag) {
            String[] objs = readLine();
            if (objs == null || objs.length == 0) {
                flag = false;
            } else {
                items.add(objs);
            }
        }
        return items;
    }

    /*
     * 返回一个根据编号跟货品对象的集合 @param classpath 实体类全路径 @param classvo 实体类的.class @param methodName M
     */
    public Map<String, Object> toJdbcParamMap(String classpath, Class classvo, String methodName) throws Exception {
        Map<String, Object> maps = new HashMap<String, Object>();
        Field[] fields = classvo.getDeclaredFields();

        Object obj = null;
        String methodvalue = null;
        while (hasMore()) {
            obj = classvo.newInstance();
            for (Field f : fields) {
                Method m = classvo.getMethod(getFiledMethodName(f.getName(), classvo), f.getType());
                if (methodName.equals(m.getName())) {
                    methodvalue = getByFieldName(f.getName(), f.getType()).toString();
                }
                m.invoke(obj, getByFieldName(f.getName(), f.getType()));
            }
            maps.put(methodvalue, obj);
        }
        close();
        return maps;
    }

    /**
     * 获取object的方法名称
     * * 方法描述：TODO 方法实现描述 
     * @author 李准 2011-6-11 下午03:33:59
     * @date 2011-6-11 下午03:33:59
     * 
     * @param filename
     * @param classvo
     * @return
     */
    public static String getFiledMethodName(String filename, Class<T> classvo) {
        StringBuffer str = new StringBuffer("set");
        java.lang.reflect.Method[] methods = classvo.getMethods();
        int strcount = filename.length();
        String subfilename = filename;
        String strname = str.append(filename.toUpperCase().substring(0, 1)).append(subfilename.substring(1, strcount)).toString();
        if (StringUtils.isNotEmpty(filename)) {
            for (Method m : methods) {
                if (strname.equals(m.getName())) {
                    return m.getName();
                }
            }
        }
        return strname;
    }

    /**
     * 测试
     * 方法描述：TODO 方法实现描述 
     * @author 李准 2011-6-12 上午11:40:33
     * @date 2011-6-12 上午11:40:33
     * 
     * @param args
     * @throws Exception
     */

}
