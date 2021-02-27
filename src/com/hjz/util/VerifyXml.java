package com.hjz.util;

import org.dom4j.Attribute;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyXml {

    /**
     * 验证组合件级二维码标题
     *
     * @param list 二维码内容
     * @param language 界面显示语言
     * @return
     */
    public  String verifyComproduct(List<String> list,String language) {
        SAXReader reader = new SAXReader();

        File file = new File("./configuration.xml");

        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            List<Element> systemElements = root.elements();
            for (Element system: systemElements) {
                //判断系统级信息通过编号对应，需要判断list里的list[0],list[1],list[2],list[3],对应到xml的taskcode,coderegion,type,number
                //所属系统编号不对应则跳出此循环
                if(!list.get(3).equals(system.attributeValue("number")))continue;
                //判断任务代号、所属地域、装备类型,不相等则跳出返回false
                if(!list.get(0).equals(system.attributeValue("taskcode"))){
                    //判断语言
                    if (language.equals("Chinese")){
                        return "任务代号错误";
                    }else if (language.equals("English")){
                        return "Wrong task code";
                    }else if (language.equals("Arabic")){
                        return "رمز المهمة خاطئ";
                    }
                }
                if(!list.get(1).equals(system.attributeValue("coderegion"))){
                    if (language.equals("Chinese")){
                        return "编码地域错误";
                    }else if (language.equals("English")){
                        return "Coding region error";
                    }else if (language.equals("Arabic")){
                        return "خطأ منطقة الترميز";
                    }
                }
                if(!list.get(2).equals(system.attributeValue("type"))){
                    if (language.equals("Chinese")){
                        return "装备类型错误";
                    }else if (language.equals("English")){
                        return "Wrong equipment type";
                    }else if (language.equals("Arabic")){
                        return "نوع المعدات خاطئ";
                    }
                }
                List<Element> firstSystemElements = system.elements();
                //判断组合件信息通过编号对应，这里需要判断list[8]，list[7]，对应到xml里的code,name
                for (Element firstSystem: firstSystemElements) {

                    if (list.get(8).equals(firstSystem.attributeValue("code")) && list.get(7).equals(firstSystem.attributeValue("name"))){

                        if (language.equals("Chinese")){
                            return "配置文件中存在该组合件级产品";
                        }else if (language.equals("English")){
                            return "The assembly-level product exists in the configuration file";
                        }else if (language.equals("Arabic")){
                            return "المنتج على مستوى التجميع موجود في ملف التكوين";
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (language.equals("Chinese")){
            return "未在配置中查询到相关信息，验证失败";
        }else if (language.equals("English")){
            return "Related information is not queried in the configuration, and the verification fails";
        }else if (language.equals("Arabic")){
            return "لم يتم الاستعلام عن المعلومات ذات الصلة في التكوين ، وفشل التحقق";
        }
        return "false";
    }


    /**
     * 验证整机级二维码标题
     *
     * @param list 二维码内容
     * @param language 界面显示语言
     * @return
     */
    public String verifyMproduct(List<String> list,String language) {
        SAXReader reader = new SAXReader();

        File file = new File("./configuration.xml");

        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            List<Element> systemElements = root.elements();
            for (Element system: systemElements) {
                //判断系统级信息通过编号对应，需要判断list里的list[0],list[1],list[2],list[3],对应到xml的taskcode,coderegion,type,number
                //所属系统编号不对应则跳出此循环
                if(!list.get(3).equals(system.attributeValue("number")))continue;
                //判断任务代号、所属地域、装备类型,不相等则跳出返回false
                if(!list.get(0).equals(system.attributeValue("taskcode"))){
                    //判断语言
                    if (language.equals("Chinese")){
                        return "任务代号错误";
                    }else if (language.equals("English")){
                        return "Wrong task code";
                    }else if (language.equals("Arabic")){
                        return "رمز المهمة خاطئ";
                    }
                }
                if(!list.get(1).equals(system.attributeValue("coderegion"))){
                    if (language.equals("Chinese")){
                        return "编码地域错误";
                    }else if (language.equals("English")){
                        return "Coding region error";
                    }else if (language.equals("Arabic")){
                        return "خطأ منطقة الترميز";
                    }
                }
                if(!list.get(2).equals(system.attributeValue("type"))){
                    if (language.equals("Chinese")){
                        return "装备类型错误";
                    }else if (language.equals("English")){
                        return "Wrong equipment type";
                    }else if (language.equals("Arabic")){
                        return "نوع المعدات خاطئ";
                    }
                }
                List<Element> firstSystemElements = system.elements();
                //判断组合件信息通过编号对应，这里需要判断list[4]，对应到xml里first-system的number
                for (Element firstSystem: firstSystemElements) {
                    if(!list.get(4).equals(firstSystem.attributeValue("number")))continue;
                    List<Element> secondSystemElements = firstSystem.elements();
                    //进入分系统层循环
                    for (Element secondSystem: secondSystemElements){
                        //判断组合件信息通过编号对应，这里需要判断list[5]，对应到xml里的second-system 的number
                        if(!list.get(5).equals(secondSystem.attributeValue("number")))continue;
                        //进入整机级循环判断整机级信息
                        List<Element> thirdSystemElements = secondSystem.elements();
                        for (Element thirdSystem: thirdSystemElements){
                            //判断名称以及代号是否一致
                            if (list.get(8).equals(thirdSystem.attributeValue("code")) && list.get(7).equals(thirdSystem.attributeValue("name"))){
                                if (language.equals("Chinese")){
                                    return "配置文件中存在该整机级产品";
                                }else if (language.equals("English")){
                                    return "The machine-level product exists in the configuration fileication is successful";
                                }else if (language.equals("Arabic")){
                                    return "المنتج على مستوى الجهاز موجود في ملف التكوين";
                                }
                            }
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (language.equals("Chinese")){
            return "未在配置中查询到相关信息，验证失败";
        }else if (language.equals("English")){
            return "Related information is not queried in the configuration, and the verification fails";
        }else if (language.equals("Arabic")){
            return "لم يتم الاستعلام عن المعلومات ذات الصلة في التكوين ، وفشل التحقق";
        }
        return "";
    }

    /**
     * 验证零件级二维码标题
     *
     * @param list 二维码内容
     * @param language 界面显示语言
     * @return
     */
    public  String verifyCproduct(List<String> list,String language) {
        SAXReader reader = new SAXReader();

        File file = new File("./configuration.xml");

        try {
            Document document = reader.read(file);
            Element root = document.getRootElement();
            List<Element> systemElements = root.elements();
            for (Element system: systemElements) {
                //判断系统级信息通过编号对应，需要判断list里的list[0],list[1],list[2],list[3],对应到xml的taskcode,coderegion,type,number
                //所属系统编号不对应则跳出此循环
                if(!list.get(3).equals(system.attributeValue("number")))continue;
                //判断任务代号、所属地域、装备类型,不相等则跳出返回false
                if(!list.get(0).equals(system.attributeValue("taskcode"))){
                    //判断语言
                    if (language.equals("Chinese")){
                        return "任务代号错误";
                    }else if (language.equals("English")){
                        return "Wrong task code";
                    }else if (language.equals("Arabic")){
                        return "رمز المهمة خاطئ";
                    }
                }
                if(!list.get(1).equals(system.attributeValue("coderegion"))){
                    if (language.equals("Chinese")){
                        return "编码地域错误";
                    }else if (language.equals("English")){
                        return "Coding region error";
                    }else if (language.equals("Arabic")){
                        return "خطأ منطقة الترميز";
                    }
                }
                if(!list.get(2).equals(system.attributeValue("type"))){
                    if (language.equals("Chinese")){
                        return "装备类型错误";
                    }else if (language.equals("English")){
                        return "Wrong equipment type";
                    }else if (language.equals("Arabic")){
                        return "نوع المعدات خاطئ";
                    }
                }
                List<Element> firstSystemElements = system.elements();
                //判断组合件信息通过编号对应，这里需要判断list[4]，对应到xml里first-system的number
                for (Element firstSystem: firstSystemElements) {
                    if(!list.get(4).equals(firstSystem.attributeValue("number")))continue;
                    List<Element> secondSystemElements = firstSystem.elements();
                    //进入分系统层循环
                    for (Element secondSystem: secondSystemElements){
                        //判断组合件信息通过编号对应，这里需要判断list[5]，对应到xml里的second-system 的number
                        if(!list.get(5).equals(secondSystem.attributeValue("number")))continue;
                        //进入整机级循环判断整机级信息
                        List<Element> thirdSystemElements = secondSystem.elements();
                        for (Element thirdSystem: thirdSystemElements){
                            //判断所属整机信息是否正确
                            if(!list.get(6).equals(thirdSystem.attributeValue("number")))continue;
                            List<Element> Elements = thirdSystem.elements();
                            for (Element element: Elements){
                                //判断名称以及代号是否一致
                                if (list.get(8).equals(element.attributeValue("code")) && list.get(7).equals(element.attributeValue("name"))){
                                    if (language.equals("Chinese")){
                                        return "配置文件中存在该零件级产品";
                                    }else if (language.equals("English")){
                                        return "The part-level product exists in the configuration file";
                                    }else if (language.equals("Arabic")){
                                        return "المنتج على مستوى الجزء موجود في ملف التكوين";
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (language.equals("Chinese")){
            return "未在配置中查询到相关信息，验证失败";
        }else if (language.equals("English")){
            return "Related information is not queried in the configuration, and the verification fails";
        }else if (language.equals("Arabic")){
            return "لم يتم الاستعلام عن المعلومات ذات الصلة في التكوين ، وفشل التحقق";
        }
        return "";
    }
//测试主函数
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<String>();
        list.add("XX11");list.add("0");list.add("1");list.add("1");
        list.add("1");list.add("1");list.add("1");list.add("某某综合分析测试设备某某数据接收测试软件");
        list.add("R/1XX111-XX.XX/01-XX"); list.add("DP012020010111890711"); list.add("20200101");
        list.add("1");
        System.out.println(list);
        //System.out.println(verifyComproduct(list));
        //System.out.println(verifyCproduct(list));






    }



}
