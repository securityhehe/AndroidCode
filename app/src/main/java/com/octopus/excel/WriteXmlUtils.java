package com.octopus.excel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 *
 *  工具类方法，通过改工具，可以将excel中的多语言按照id的方法跟新多语言表。
 *
 */

public class WriteXmlUtils {

    public static  String path;
    public WriteXmlUtils(String path){
        this.path = path;
    }
    //读Excel表格。更新。
    public void updateLanguageValue(StringNode node) throws IOException {
        SAXReader sax = new SAXReader() ;
        String filePath = String.format(path, node.xmlFiileName);
        Document document= null;
        File file = createFile(filePath);
        try {
            document = sax.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
            try {
                //初始化根节点。
                init(file.getPath(),"resources");
                start();
                end();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally{
            if(document != null){
                try {
                    Document read = sax.read(file);
                    updateDoc(read,filePath,node);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateDoc(Document document,String filePath,StringNode node) throws Exception {
        Element rootElement = document.getRootElement();
        List elements = rootElement.elements("string");
        boolean isHas = false ;
        if(elements!=null&& elements.size() > 0){
            for(int i = 0 ;i< elements.size(); i++){
                Element element = (Element) elements.get(i);
                String name = element.attribute("name").getValue();
                if(name.equals(node.name)) {
                    System.out.println("language="+node.xmlFiileName+"-->key:"+node.name+"| value : ["+ element.getStringValue()+"] 被替换为 ["+node.value+"]");
                    System.out.println();
                    element.setText(node.value);
                    isHas = true;
                    break;
                }
            }
        }
        if(!isHas){
            Element elt = DocumentHelper.createElement("string");
            elt.addAttribute("name",node.name);
            elt.setText(node.value);
            System.out.println("language="+ node.xmlFiileName+" -->key:"+node.name+"| value : add ["+node.value+"]");
            System.out.println();
            rootElement.add(elt);
        }
        write(filePath,document);
    }

    public void write(String path,Document document) throws Exception {

		OutputStream out = null;
		XMLWriter xmlwriter = null;
		Document doc = document;
        try{
			// 输出格式
			OutputFormat outformat = new OutputFormat();
			// 指定XML编码
			outformat.setEncoding("UTF-8");
			outformat.setNewlines(true);
			outformat.setIndent(true);
			outformat.setTrimText(true);
			out = new FileOutputStream(path);
			xmlwriter = new XMLWriter(out, outformat);
			xmlwriter.write(doc);
		} catch (Exception e) {
			throw e;
		} finally {
            close(xmlwriter,out,null);
		}
	}

	private void close(XMLWriter xmlwriter, OutputStream out,InputStream is) {

		if (null != xmlwriter) {
			try {
				xmlwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			xmlwriter = null;
		}

		if (null != out) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null;
		}

		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			is = null;
		}
	}

    // 句柄
    private TransformerHandler handler = null;
    // 输出流
    private OutputStream outStream = null;
    // 根节点
    private String rootElement;

    public void init(String fileName, String rootElement) throws Exception {
        this.rootElement = rootElement;
        // 创建句柄，并设置初始参数
        createFile(fileName);
        SAXTransformerFactory fac = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        handler = fac.newTransformerHandler();
        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        outStream = new FileOutputStream(fileName);
        handler.setResult(new StreamResult(outStream));
        System.out.println("初始化成功");
    }

    private File createFile(String fileName) throws IOException {
         File file = new File(fileName);
        if(!file.exists()){
            String path = file.getPath();
            int i = path.lastIndexOf("\\");
            if(i > 0 ){
                System.out.println("path = "+ path);
                String parentPath = path.substring(0, i);
                File parentFile = new File(parentPath);
                parentFile.mkdirs();
                file.createNewFile();
            }
        }
        return file;
    }

    public void start() throws Exception {
        handler.startDocument();
        handler.characters("\n".toCharArray(), 0, "\n".length());//给子元素节点添加缩进
        handler.startElement("", "", rootElement, null);
        System.out.println("文档开始...");
    }

    public void end() throws Exception {
        handler.characters("\n".toCharArray(), 0, "\n".length());//给子元素节点添加缩进
        handler.endElement("", "", rootElement);
        handler.endDocument();
        outStream.close();
        System.out.println("文档结束！");
    }
}
