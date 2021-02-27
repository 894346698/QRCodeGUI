package com.hjz.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hjz.util.CreatAndReadExcel;

public class verify {

	/**
	 * 验证文件标题
	 * 
	 * @param str 文件名
	 * @param excel2003List excel文件
	 * @param language 框架语言
	 * @return
	 */
	public static String verify_name(String str,List<List<Object>> excel2003List,String language) {
			//输出结果集合

			String result = "";
			if (language.equals("Chinese")){
				result+="文件格式正确     标题格式错误";
			}else if (language.equals("English")){
				result+="The file format is correct      The title format is wrong";
			}else if (language.equals("Arabic")){
				result+="تنسيق الملف صحيح تنسيق العنوان خاطئ";
			}

			String name = null,title = null,mid = null,belong = null,date = null;
			try {
				
				title = str.substring(0, str.indexOf("_"));
				mid = str.substring(str.indexOf("_")+1,str.lastIndexOf("_") );
				date = str.substring(str.lastIndexOf("_")+1,str.indexOf(".") );
			} catch (StringIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return result;
			}
			
			if(title.equals(excel2003List.get(0).get(1).toString()+"基本信息文件")) {
			}else {
				return result;
			}
			if(mid.equals(excel2003List.get(0).get(3).toString()+"+"+excel2003List.get(0).get(5).toString())) {
			}else {
				return result;
			}
			if(date.equals(excel2003List.get(0).get(7).toString())) {
			}else {
				return result;
			}

		if (language.equals("Chinese")){
			result="文件格式正确     标题格式正确";
		}else if (language.equals("English")){
			result="The file format is correct      The title format is correct";
		}else if (language.equals("Arabic")){
			result="تنسيق الملف صحيح تنسيق العنوان صحيح";
		}
		return result;
	}
	
	/**
	 * 验证文件内容
	 * 
	 * @param excel excel文件
	 * @return
	 */
	public static boolean verify_content(List<List<Object>> excel) {
		boolean result;
		if(excel.get(0).get(0).toString().equals("产品名称") && excel.get(0).get(2).toString().equals("产品代号") && excel.get(0).get(4).toString().equals("产品编号") &&
				excel.get(0).get(6).toString().equals("出厂日期") && excel.get(1).get(0).toString().equals("设计单位") && excel.get(1).get(2).toString().equals("生产单位") &&
				excel.get(2).get(0).toString().equals("任务代号") && excel.get(2).get(2).toString().equals("编码地域") && excel.get(2).get(4).toString().equals("装备类型") &&
				excel.get(3).get(0).toString().equals("所属系统") && excel.get(3).get(2).toString().equals("所属一级子系统") && excel.get(3).get(4).toString().equals("所属二级子系统") &&
				excel.get(3).get(6).toString().equals("所属三级子系统") ) {	
			//result += "内容正确";
		}else {
			return false;
		}
		
		
		
		return true;
		
	}
	
	/* 
	  * 判断是否为整数  
	  * @param str 传入的字符串  
	  * @return 是整数返回true,否则返回false  
	*/
	public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	  }
	
	/***
     * 判断字符串是否是yyyyMMdd格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isRqFormat(String mes){
        String format = "([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
            matcher = pattern.matcher(mes);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    
    }
	/***
	 * 转换excel变成List类型
	 * @param excellist 从excel中读取的list
	 * @param list 转换成验证格式的list
	 */
	public static void excelistToList(List<List<Object>> excellist,List<String> list){
		list.add(excellist.get(2).get(1).toString());
		list.add(excellist.get(2).get(3).toString());
		list.add(excellist.get(2).get(5).toString());
		list.add(excellist.get(3).get(1).toString());
		list.add(excellist.get(3).get(3).toString());
		list.add(excellist.get(3).get(5).toString());
		list.add(excellist.get(3).get(7).toString());
		list.add(excellist.get(0).get(1).toString());
		list.add(excellist.get(0).get(3).toString());
		list.add(excellist.get(0).get(5).toString());
		list.add(excellist.get(0).get(7).toString());
	}

	//测试主函数
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		String str = "20201311";
//		int n1 = Integer.valueOf(str);
//		System.out.print(isRqFormat(str));
//	}
}
