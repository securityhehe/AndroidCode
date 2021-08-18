package com.octopus.test;

import com.octopus.excel.ExcelXmlUtils;
import com.octopus.excel.StringNode;
import com.octopus.excel.WriteXmlUtils;

import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static String excelPath = "C:\\project\\AndroidCode\\app\\language.xls";
    public static String tableName = "language";
    //public static final String languageFilePath = "C:\\Users\\shaohua\\Desktop\\project\\android-call-demo4\\skWeiChatBaidu\\src\\main\\res\\%s\\strings.xml";
  // public static final String languageFilePath =  "C:\\Users\\shaohua\\Desktop\\project\\android-call-demo4\\skWeiChatBaidu\\src\\main\\res\\%s\\strings.xml";
   public static final String languageFilePath = "C:\\Users\\shaohua\\Desktop\\project\\android-call-demo4\\easyPhotos\\src\\main\\res\\%s\\strings.xml";
    public static final String writeXmlPath = "C:\\project\\AndroidCode\\aa\\%s\\strings.xml";
    public static final String values[] = new String[]{
             "values,默认"
            , "values-zh-rHK,繁体hk"
            , "values-in,印尼"
            , "values-en,英文"
            , "values-vi,越南"
            , "values-pt-rPT,葡萄牙"
            , "values-es-rES,西班牙"
    };



    /**
     *
     * 将Android项目的多语言导出到excel
     */
    @Test
    public void exportLanguage() {
        ExcelXmlUtils utils = new ExcelXmlUtils("","",languageFilePath,values);
        try {
            utils.exportXmlToExcel();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    public void readKey() {
        ExcelXmlUtils utils = new ExcelXmlUtils("C:\\project\\AndroidCode\\app\\language.xls","language",languageFilePath,values);
        List<String> strings = utils.readKeys();
        System.out.println("change size:" + strings.size());

    }

    @Test
    public void writeString(){
        WriteXmlUtils update = new WriteXmlUtils(languageFilePath);
        ExcelXmlUtils utils = new ExcelXmlUtils(excelPath, tableName,languageFilePath,values);
        List<String> array = utils.readKeys();
        for(int i = 0 ; i< array.size();i++) {
            Map<String, StringNode> stringNodeMap = null;
            try {
                stringNodeMap = utils.readExcel(excelPath, tableName, array.get(i));
                Set<String> key = stringNodeMap.keySet();
                Iterator<String> iterator = key.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    StringNode stringNode = stringNodeMap.get(next);
                    update.updateLanguageValue(stringNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
