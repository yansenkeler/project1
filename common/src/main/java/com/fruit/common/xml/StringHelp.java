package com.fruit.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.widget.EditText;

/**
 * @author liyc
 * @time 2012-1-8 上午12:50:14
 * @annotation 
 */
public class StringHelp {

	public static final String HTMLSTYLE1 = "<style>#oschina_title {color: #000000; margin-bottom: 6px;font-weight:bold;}#oschina_title img{vertical-align:middle;margin-right:6px;}#oschina_title a{color:#0D6DA8;}#oschina_outline {color: #707070; font-size:12px;}#oschina_outline a{color:#0D6DA8;}#epoint_sender{color:#000033; font-size:12px;}#oschina_software{color:#808080;font-size:12px}#oschina_body img {max-width: 300px;}#oschina_body{font-size:16px;max-width:300px;line-height:24px;} #oschina_body table{max-width:300px;}#oschina_body pre {font-size:9pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}.hr0{ height:1px;border:none;border-top:1px dashed #0066CC;}.hr1{ height:1px;border:none;border-top:1px solid #555555;}.hr2{ height:3px;border:none;border-top:3px double red;}.hr3{ height:5px;border:none;border-top:5px ridge green;}.hr4{ height:10px;border:none;border-top:10px groove skyblue;}</style>";
	public static final String OschinaStyle = "<style>#oschina_title {color: #000000; margin-bottom: 6px;font-weight:bold;font-size:18px;}#oschina_title img{vertical-align:middle;margin-right:6px;}#oschina_title a{color:#0D6DA8;}#oschina_outline {color: #707070; font-size:12px;}#oschina_outline a{color:#0D6DA8;}#epoint_sender{color:#000033; font-size:12px;}#oschina_software{color:#808080;font-size:12px}#oschina_body img {max-width: 300px;}#oschina_body{font-size:16px;max-width:300px;line-height:24px;} #oschina_body table{max-width:300px;}#oschina_body pre {font-size:9pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}</style>";
	public static final String HR_style1 = "<hr style=\"height:1px\" color=Silver>";
	public static final String HR_style2 = "<hr style=\"border-top: 2px dashed; border-bottom: 2px dashed; height: 2px\" color=black>";

	public static boolean isLength0(String s){
		if(s.trim().length()==0){
			return true;
		}
		return false;
	}

	public static String XMLSwitchedOrigion(String oldstring) {
		oldstring = oldstring.replace("&amp;","&");
		oldstring = oldstring.replace("&lt;","<");
		oldstring = oldstring.replace("&gt;",">");
		oldstring = oldstring.replace("&apos;","'");
		oldstring = oldstring.replace("&quot;","\"");
		return oldstring;
	}

	public static String XMLSwitched(String oldstring) {
		oldstring = oldstring.replace("&","&amp;");
		oldstring = oldstring.replace("<","&lt;");
		oldstring = oldstring.replace(">","&gt;");
		oldstring = oldstring.replace("'","&apos;");
		oldstring = oldstring.replace("\"","&quot;");
		return oldstring;
	}

	public static String filterXmlString(String origionStr) {
		origionStr = origionStr.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("'","&apos;").replace("\"", "&quot;");
		return origionStr;
	}

	/**
	 * 获取XML标签之间的属性
	 * 如:
	 * bs = "<name>epoint</name>"
	 * att = "name"
	 * return "epoint"
	 */
	public static String getXMLAtt(String bs,String att){
		String result = "";
		try{
			String  head = "<"+att+">";
			String tail = "</"+att+">";
			result = bs.substring(bs.indexOf(head)+head.length(),bs.indexOf(tail));
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return result;
	}

	/**
	 * @author liyc
	 * @time 2012-3-6 上午10:31:32
	 * @annotation 获取标签如 xml = <root><outs><out>beijing</out></outs></root>
	 * bs = xml ,att = "outs"
	 * return <outs><out>beijing</out></outs>
	 */
	public static String getXMLAttOut(String bs,String att){
		try{
			String bs1 = getXMLAtt(bs, att);
			return "<"+att+">"+bs1+"</"+att+">";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public static String getAttOut(String bs,String att){
		String s = "";
		try{
			s = bs.substring(bs.indexOf("<"+att+">"), bs.indexOf("</"+att+">")+("</"+att+">").length());
		}catch(Exception e){
			e.printStackTrace();
		}
		return s;
	}


	public static String get2AttMid(String bs,String q,String h){
		try{
			return bs.substring(bs.indexOf(q)+q.length(),bs.indexOf(h));
		}catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	//人员选择后List转字符串
	public static String[] dealChooseListToString(List<Map<String,String>> clst){
		String snname = "";
		String sguid = "";
		int size = clst.size();
		if(size>0){
			for(int i=0;i<size;i++){
				if(i<size-1){
					snname += clst.get(i).get("name")+";";
					sguid += clst.get(i).get("guid")+";";
				}
				else if(i==size-1){
					snname += clst.get(i).get("name");
					sguid += clst.get(i).get("guid");
				}
			}
		}
		String[] info = new String[2];
		info[0] = snname;
		info[1] = sguid;
		return info;
	}

	public static List<Map<String,String>> String2List(String names,String guids){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(names==null||guids==null||"".equals(names)||"".equals(guids)){
			return new ArrayList<Map<String,String>>();
		}
		String[] s_name = names.split(";");
		String[] s_guid = guids.split(";");
		for(int i=0;i<s_name.length;i++){
			Map<String,String> map = new HashMap<String, String>();
			map.put("name", s_name[i]);
			map.put("guid", s_guid[i]);
			list.add(map);
		}
		return list;
	}

	public static boolean isEditTextBlank(EditText et){
		if(et.getText().toString().trim().length()==0){
			return true;
		}
		else{
			return false;
		}
	}

	public static boolean isEqualBlank(String s){
		s = s==null?"":s;
		if(s.equals("")){
			return true;
		}
		return false;
	}

	/**
	 * 截除字符串最后一个标志，如abc;，将末尾;截去
	 * @param s 原始字符
	 * @param Symbol 结尾需要截取的字符
	 * @return 截除完成的字符
	 */
	public static String cutLastSymbol(String s,String Symbol)
	{
		if(s.endsWith(Symbol))
		{
			return s.substring(0, s.length()-1);
		}
		return s;
	}

	public static int versionToInteger(String versionstr){
		try{
			String[] ss = versionstr.split("\\.");
			String sv = "";
			for(String s:ss){
				sv += s;
			}
			return Integer.parseInt(sv);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 过滤CDATA字符包裹
	 * @param oldStr
	 * @return
	 */
	public static String filterCDATA(String oldStr)
	{
		return oldStr.replace("<![CDATA[", "").replace("]]>", "");
	}

	/**
	 * 截取XML标签内容，并作CDATA过滤
	 * @param bs
	 * @param tag
	 * @return
	 */
	public static String getXMLInFilterCData(String bs,String tag)
	{
		return filterCDATA(getXMLAtt(bs, tag));
	}
	
	/**
	 * byte[]转换成字符串
	 * @param b
	 * @return
	 */
	public static String byte2HexString(byte[] b)
	{
		StringBuffer sb = new StringBuffer();
		int length = b.length;
		for (int i = 0; i < length; i++) {
			String stmp = Integer.toHexString(b[i]&0xff);
			if(stmp.length() == 1)
				sb.append("0"+stmp);
			else
				sb.append(stmp);
		}
		return sb.toString();
	}


	/**
	 * 16进制转换成byte[]
	 * @param hexString
	 * @return
	 */
	public static byte[] String2Byte(String hexString)
	{
		if(hexString.length() % 2 ==1)
			return null;
		byte[] ret = new byte[hexString.length()/2];
		for (int i = 0; i < hexString.length(); i+=2) {
			ret[i/2] = Integer.decode("0x"+hexString.substring(i,i+2)).byteValue();
		}
		return ret;
	}

	/**
	 * 获取一定长度的随机字符串
	 * @param length 指定字符串长度
	 * @return 一定长度的字符串
	 */
	public static String getRandomStringByLength(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
