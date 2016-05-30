package com.fruit.common.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangchen on 15/7/13.
 */
public class SAXPraserHelper extends DefaultHandler {

    final int ITEM = 0x0005;

    List list;
    Class<?> c;
    Object nc;
    int currentState = 0;
    String currentFieldName;
    String subItemTag;

    boolean subItemStart = false;//子元素是否开始扫描

    public SAXPraserHelper(Class<?> c,String subItemTag) {
        this.c = c;
        this.subItemTag = subItemTag;
    }

    public List getList() {
        return list;
    }

    /*
     * 接口字符块通知
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        // super.characters(ch, start, length);
        String theString = String.valueOf(ch, start, length);
        if (currentState != 0) {
            try {
                Field field = nc.getClass().getField(currentFieldName);
                field.set(nc, theString);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            currentState = 0;
        }
        return;
    }

    /*
     * 接收文档结束通知
*/
    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
    }

    /*
     * 接收标签结束通知
*/
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
        if (localName.equals(subItemTag)){
            list.add(nc);
            subItemStart = false;
        }
    }

    /*
     * 文档开始通知
*/
    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        list = new ArrayList();
    }

    /*
     * 标签开始通知
*/
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        try {

            if (localName.equals(subItemTag)){
                nc = c.newInstance();
                subItemStart = true;
            }else if(subItemStart){
                Field[] fields = nc.getClass().getDeclaredFields();
                int fLen = fields.length;
                for(int j = 0 ; j < fLen; j++) {
                    String fieldName = fields[j].getName();
                    if (localName.equals(fieldName)) {
                        currentState = ITEM;
                        currentFieldName = fieldName;
                        return;
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

              //取属性
//            for (int i = 0; i < attributes.getLength(); i++) {
//                if (attributes.getLocalName(i).equals("id")) {
//                    chann.setId(attributes.getValue(i));
//                } else if (attributes.getLocalName(i).equals("url")) {
//                    chann.setUrl(attributes.getValue(i));
//                }
//            }
//        }
        currentState = 0;
        return;
    }
}