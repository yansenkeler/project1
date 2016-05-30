package com.fruit.common.xml;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;

/**
 * @author liyc
 * @time 2012-9-3 下午1:55:08
 * @annotation 解析帮助类
 */
public class XMLUtil {

	public static final String XMLTAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

//	/**
//	 * Dom通用解析类
//	 * @param xml 列表xml字符串
//	 * @param c 解析Model
//	 * @return 返回存放Model实例的list对象
//	 * @throws Exception
//	 */
//	public static ArrayList DomAnalysis(String xml,Class<?> c)
//	{
//		try{
//			Document document  = DocumentHelper.parseText(xml);
//			ArrayList list = new ArrayList();
//			Element root = document.getRootElement();
//			List focs = root.elements();
//			Object nc = c.newInstance();
//			Field[] fields = nc.getClass().getDeclaredFields();
//			int fLen = fields.length;
//			for(int i=0;i<focs.size();i++)
//			{
//				Element foc = (Element) focs.get(i);
//				nc = c.newInstance();
//				for(int j = 0 ; j < fLen; j++) {
//					String fieldName = fields[j].getName();
//					if (foc.element(fieldName) != null){
//						fields[j].set(nc,foc.elementText(fieldName));
//					}
//				}
//				list.add(nc);
//			}
//			return list;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public static ArrayList DomAnalysisCommon(String xml,Class<?> c)
//	{
//		try{
//			Document document = null;
//			if(xml.toLowerCase().startsWith(XMLTAG.toLowerCase()))
//			{
//				document = DocumentHelper.parseText(xml);
//			}
//			else
//			{
//				document = DocumentHelper.parseText(XMLTAG+xml);
//			}
//			ArrayList list = new ArrayList();
//			Element root = document.getRootElement();
//			List focs = root.elements();
//			Object nc = c.newInstance();
//			Field[] fields = nc.getClass().getDeclaredFields();
//			int fLen = fields.length;
//			for(int i=0;i<focs.size();i++)
//			{
//				Element foc = (Element) focs.get(i);
//				nc = c.newInstance();
//				for(int j = 0 ; j < fLen; j++) {
//					String fieldName = fields[j].getName();
//					if (foc.element(fieldName) != null){
//						fields[j].set(nc,foc.elementText(fieldName));
//					}
//				}
//				list.add(nc);
//			}
//			return list;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * 将String转换成InputStream
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in) throws Exception{

		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
		return is;
	}

	public static List DomAnalysis(Class<?> c,String rootItem,String claseItem,String xml) throws Exception, ParserConfigurationException, SAXException, IOException
	{
		//实例化一个SAXParserFactory对象
		SAXParserFactory factory=SAXParserFactory.newInstance();
		SAXParser parser;
		//实例化SAXParser对象，创建XMLReader对象，解析器
		parser=factory.newSAXParser();
		XMLReader xmlReader=parser.getXMLReader();
		//实例化handler，事件处理器
		SAXPraserHelper helperHandler=new SAXPraserHelper(c,"info");
		//解析器注册事件
		xmlReader.setContentHandler(helperHandler);
		//读取文件流
//		InputStream stream=getResources().openRawResource(R.raw.channels);

		InputStream stream = StringTOInputStream(xml);
		InputSource is=new InputSource(stream);
		//解析文件
		xmlReader.parse(is);
		return helperHandler.getList();
	}


}
