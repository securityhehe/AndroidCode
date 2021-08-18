package com.octopus.excel;

import android.support.annotation.NonNull;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * export language String to excel file
 *
 * @author zshh create by 2019/5/10
 */
public class ExcelXmlUtils {
    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";
    public String excelPath;
    public String excelName;
    public String values[];
    public String xmlPath;
    private static HSSFWorkbook workbook;

    public ExcelXmlUtils(String excelPath, String sheetName, String xmlPath, String[] values) {
        this.excelPath = excelPath;
        this.excelName = sheetName;
        this.values = values;
        this.xmlPath = xmlPath;
    }

    /**
     * 导出多语言到excel表格。
     * <p>
     */
    public void exportXmlToExcel() throws IOException {
        workbook = new HSSFWorkbook();
        for (int i = 0; i < values.length; i++) {
            try {
                String[] split = values[i].split(",");
                String s = split[0];
                if (s.equals("key")) continue;
                this.xmlRead(String.format(xmlPath, s), (i));
            } catch (SAXException e) {
                e.printStackTrace()
                ;
            }
        }
        File xlsFile = new File("language.xls");
        FileOutputStream xlsStream;
        try {
            xlsStream = new FileOutputStream(xlsFile);
            workbook.write(xlsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private int row = 1;

    private void xmlRead(String fileName, int col) throws SAXException, IOException {
        row = 1;
        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ContentHandler contentHandler = new MyContentHandler(col);
        reader.setContentHandler(contentHandler);
        reader.parse(fileName);
    }

    public class MyContentHandler implements ContentHandler {

        private StringNode node;
        private StringBuffer buf;

        private int col;

        private MyContentHandler(int col) {
            this.col = col;
        }

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() {
            buf = new StringBuffer();
            try {
                writeExcelHead(workbook, values[col], col + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("*******开始解析文档*******");
        }

        public void endDocument() {
        }

        public void processingInstruction(String target, String instruction) {
        }

        public void startPrefixMapping(String prefix, String uri) {
        }

        public void endPrefixMapping(String prefix) {
        }

        public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) {
            node = new StringNode();
            node.setName(attributes.getValue(0));
        }

        public void characters(char[] chars, int start, int length) {
            //将元素内容累加到StringBuffer中
            buf.append(chars, start, length);
        }

        public void endElement(String namespaceURI, String localName, String fullName) {
            //打印出非空的元素内容并将StringBuffer清空
            if ("string".equals(fullName)) {
                node.setValue(buf.toString().trim());
                writeExcel(workbook, node, col);
            }
            buf.setLength(0);
        }

        public void ignorableWhitespace(char[] chars, int start, int length) {
        }

        public void skippedEntity(String name) {
        }
    }

    /**
     * 写excel 表格表格头部。
     *
     * @param workbook workbook
     * @param language language
     * @param col      col
     * @throws IOException exception
     */
    private void writeExcelHead(HSSFWorkbook workbook, String language, int col) throws IOException {
        HSSFSheet sheet;
        if ((sheet = workbook.getSheet("language")) == null) {
            sheet = workbook.createSheet("language");
        }
        HSSFRow row1;
        if ((row1 = sheet.getRow(0)) == null) {
            row1 = sheet.createRow(0);
        }
        row1.createCell(col).setCellValue(language);
        File xlsFile = new File("language.xlsx");
        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);
    }

    /**
     * 写excel 表格体积。
     *
     * @param workbook   workbook
     * @param stringNode stringNode
     * @param col        col
     */
    private void writeExcel(HSSFWorkbook workbook, StringNode stringNode, int col) {
        HSSFSheet sheet;
        if ((sheet = workbook.getSheet("language")) == null) {
            sheet = workbook.createSheet("language");
        }
        //sheet.getRow(row)
        if (col == 0) {
            HSSFRow row1 = sheet.createRow(row);
            row1.createCell(col).setCellValue(stringNode.name);
            row1.createCell(col + 1).setCellValue(stringNode.value);
            ++row;
        } else {
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                HSSFRow row1 = sheet.getRow(i);
                HSSFCell cell = row1.getCell(0);
                if (cell == null) {
                    continue;
                }
                if (cell.getStringCellValue().equals(stringNode.name)) {
                    row1.createCell(col + 1).setCellValue(stringNode.value);
                    break;
                }
            }
        }
    }

    /**
     * 获取后缀
     *
     * @param filepath filepath 文件全路径
     */
    private static String getSuffiex(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filepath.substring(index + 1);
    }


    public Map<String, StringNode> readExcel(String path, String sheetName, String key) throws IOException {
        Map<String, StringNode> stringNodeMap = new HashMap<>();
        boolean isOK = excelIsOk(path);
        if (isOK) {
            Sheet sheet1 = openExcel();
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("sheet 表名称为：可以不能为空");
            }
            int lastRowNum = sheet1.getLastRowNum(); //最后一行。
            boolean isFind = false;

            for (int i = 0; i < lastRowNum; i++) {
                Row row = sheet1.getRow(i);
                if(row==null ){
                    continue;
                }
                Cell cell = row.getCell(0);

                if (cell != null) {
                    if (cell.getCellType() == CellType.STRING) {
                        String stringCellValue = cell.getStringCellValue();
                        if (key.equals(stringCellValue.trim())) {

                            //找到对应的key
                            isFind = true;
                            for (int j = 1; j < 18; j++) {
                                //得到第一行的每一个单元格。
                                Row rowTitle = sheet1.getRow(0);
                                Cell cellTitle = rowTitle.getCell(j);

                                if (cellTitle != null) {

                                    String stringTitle = cellTitle.getStringCellValue();

                                    Cell cellValue = row.getCell(j);
                                    if (cellValue != null) {
                                        String stringCellValue1 = cellValue.getStringCellValue();
                                        if (stringCellValue1 != null && !stringCellValue.isEmpty()) {

                                            //找到Contents常量中value的值。
                                            for (int x = 0; x < values.length; x++) {
                                                boolean contains = values[x].contains(stringTitle);
                                                if (contains) {
                                                    StringNode stringNode = new StringNode();
                                                    stringNode.name = key;
                                                    stringNode.value = stringCellValue1;
                                                    stringNode.xmlFiileName = values[x].split(",")[0];
                                                    stringNode.execlName = values[x].split(",")[1];
                                                    // System.out.println(values[x]);
                                                    System.out.println(stringNode.xmlFiileName);
                                                    stringNodeMap.put(stringNode.xmlFiileName, stringNode);
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            if (!isFind) {
                System.err.println("没有找到需要更新的key:" + key);
            }
        }
        return stringNodeMap;
    }

    public List<String> readKeys() {
        try {
            Sheet sheet = openExcel();
            List<String> strings = readKey(sheet);
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> readKey(Sheet sheet) {
        List<String> keys = new ArrayList<>();
        int rowNumber = sheet.getLastRowNum();

        boolean findkey = false;
        for (int i = 0; i < rowNumber; i++) {
            Row row = sheet.getRow(i);
            if(row==null){
                continue;
            }
            int first = row.getFirstCellNum();
            int last = row.getLastCellNum();
            int keyIndex = 0;
            if (!findkey) {
                for (int cellIndex = first; cellIndex < last; i++) {
                    Cell cell = row.getCell(cellIndex);
                    if (cell.getCellType() == CellType.STRING) {
                        String key = cell.getStringCellValue();
                        if ("key".equals(key)) {
                            findkey = true;
                            keyIndex = cellIndex;
                            break;
                        }
                    }
                }
            } else {
                Cell cell = row.getCell(keyIndex);
                if(cell!=null){
                    String key = cell.getStringCellValue();
                    keys.add(key);
                }

            }
        }
        return keys;
    }


    @NonNull
    public Sheet openExcel() throws IOException {
        if (excelIsOk(excelPath)) {
            InputStream is = new FileInputStream(excelPath);
            Workbook wb = WorkbookFactory.create(is);
            if (is != null) {
                is.close();
            }
            if (wb != null) {
                wb.close();
            }

            Sheet sheet1 = wb.getSheet(excelName);
            if (sheet1 == null) {
                throw new IllegalArgumentException("sheet 表名称为：" + excelName + "不存在。。。");
            }
            return sheet1;
        }
        return null;
    }

    @NonNull
    private boolean excelIsOk(String path) {
        File xlsFile = new File(path);
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        if (!xlsFile.exists()) {
            throw new IllegalArgumentException("文件不存在。。。。。");
        }

        String suffiex = getSuffiex(path);
        if (StringUtils.isBlank(suffiex)) {
            throw new IllegalArgumentException("文件后缀不能为空");
        }

        if (!OFFICE_EXCEL_XLS.equals(suffiex) && !OFFICE_EXCEL_XLSX.equals(suffiex)) {
            throw new IllegalArgumentException("该文件非Excel文件");
        }
        return true;
    }


}
