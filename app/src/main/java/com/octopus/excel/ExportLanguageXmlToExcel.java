package com.octopus.excel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import java.util.HashMap;
import java.util.Map;

/**
 * export language String to excel file
 * @author  zshh create by 2019/5/10
 *
 */
public class ExportLanguageXmlToExcel extends Constent{


    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    private static HSSFWorkbook workbook;

    /**
     * 导出多语言到excel表格。
     */
    public static void main(String[] args) throws IOException, SAXException {
        ExportLanguageXmlToExcel exportLanguageXmlToExcel = new ExportLanguageXmlToExcel();
        workbook = new HSSFWorkbook();
        for(int i= 0 ;i < values.length; i++){
            exportLanguageXmlToExcel.xmlRead(String.format(languageFilePath,values[i]),(i));
        }
        File xlsFile = new File("language.xlsx");
        FileOutputStream xlsStream;
        try {
            xlsStream = new FileOutputStream(xlsFile);
            workbook.write(xlsStream);
        } catch (IOException e) {
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
        private MyContentHandler(int col){
           this.col = col;
        }

        public void setDocumentLocator(Locator locator) { }

        public void startDocument() {
            buf = new StringBuffer();
            try {
                writeExcelHead(workbook,values[col],col+1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("*******开始解析文档*******");
        }

        public void endDocument() { }

        public void processingInstruction(String target, String instruction) { }

        public void startPrefixMapping(String prefix, String uri) { }

        public void endPrefixMapping(String prefix) { }

        public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) {
            node  = new StringNode();
            node.setName(attributes.getValue(0));
        }

        public void characters(char[] chars, int start, int length)   {
            //将元素内容累加到StringBuffer中
            buf.append(chars, start, length);
        }

        public void endElement(String namespaceURI, String localName, String fullName) {
            //打印出非空的元素内容并将StringBuffer清空
            if("string".equals(fullName)) {
                node.setValue(buf.toString().trim());
                writeExcel(workbook, node, col);
            }
            buf.setLength(0);
        }

        public void ignorableWhitespace(char[] chars, int start, int length){ }

        public void skippedEntity(String name) {}
    }

    /**
     * 写excel 表格表格头部。
     * @param workbook workbook
     * @param language language
     * @param col col
     * @throws IOException exception
     */
    private void writeExcelHead(HSSFWorkbook workbook, String language, int col) throws IOException {
        HSSFSheet sheet;
        if((sheet = workbook.getSheet("language"))==null){
            sheet   = workbook.createSheet("language");
        }
        HSSFRow row1;
        if((row1 =sheet.getRow(0))==null){
            row1 = sheet.createRow(0);
        }
        row1.createCell(col).setCellValue(language);
        File xlsFile = new File("language.xlsx");
        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);
    }

    /**
     * 写excel 表格体积。
     * @param workbook workbook
     * @param stringNode stringNode
     * @param col col
     */
    private void writeExcel(HSSFWorkbook workbook,StringNode stringNode ,int col) {
        HSSFSheet sheet;
        if((sheet = workbook.getSheet("language"))==null){
            sheet   = workbook.createSheet("language");
        }
        //sheet.getRow(row)
        if(col == 0) {
            HSSFRow row1 = sheet.createRow(row);
            row1.createCell(col).setCellValue(stringNode.name);
            row1.createCell(col + 1).setCellValue(stringNode.value);
            ++row;
        }else{
            for(int i = 0 ; i< sheet.getLastRowNum(); i++){
                HSSFRow row1 = sheet.getRow(i);
                HSSFCell cell = row1.getCell(0);
                if(cell == null){
                    continue;
                }
                if(cell.getStringCellValue().equals(stringNode.name)){
                    row1.createCell(col + 1).setCellValue(stringNode.value);
                    break;
                }
            }
        }
    }

    /**
     * 获取后缀
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
        return filepath.substring(index + 1, filepath.length());
    }


    public Map<Integer,StringNode> readExcel(String path, String sheetName, String key) throws IOException {
        Map<Integer,StringNode> stringNodeMap = new HashMap<>();
        File xlsFile = new File(path);
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        if(!xlsFile.exists()){
            throw new IllegalArgumentException("文件不存在。。。。。");
        }

        String suffiex = getSuffiex(path);
        if (StringUtils.isBlank(suffiex)) {
            throw new IllegalArgumentException("文件后缀不能为空");
        }

        if (!OFFICE_EXCEL_XLS.equals(suffiex) || !OFFICE_EXCEL_XLSX.equals(suffiex)) {
            throw new IllegalArgumentException("该文件非Excel文件");
        }

        InputStream is = new FileInputStream(path);
        Workbook wb = WorkbookFactory.create(is);
        if (is != null) {
            is.close();
        }
        if (wb != null) {
            wb.close();
        }

        Sheet sheet1 = wb.getSheet(sheetName);
        if(sheet1 == null){
            throw new IllegalArgumentException("sheet 表名称为："+sheetName +"不存在。。。");
        }

        if(key == null || key.isEmpty()){
            throw new IllegalArgumentException("sheet 表名称为：可以不能为空");
        }

        int lastRowNum = sheet1.getLastRowNum();
        boolean isFind = false;
        for(int i = 0 ; i< lastRowNum;i++){
            Row row = sheet1.getRow(i);
            Cell cell = row.getCell(0);
            if(cell.getCellType() == CellType.STRING){
                String stringCellValue = cell.getStringCellValue();
                if(key.equals(stringCellValue.trim())){
                    isFind = true;
                    for(int j=1;j <values.length + 1; j++){
                        String stringCellValue1 = cell.getStringCellValue();
                        if(stringCellValue1 !=null && !stringCellValue.isEmpty()){
                            StringNode stringNode = new StringNode();
                            stringNode.name  = key;
                            stringNode.value = stringCellValue1;
                            stringNodeMap.put(j-1,stringNode);
                        }
                    }
                }
            }
        }

        if(!isFind) {
            System.err.println("没有找到需要更新的key:"+key);
        }
        return stringNodeMap;
    }
}
