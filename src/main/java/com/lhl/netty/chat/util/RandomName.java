package com.lhl.netty.chat.util;

import java.util.Random;

public class RandomName {

    public String getRandomName(){
        /*String[] nameList = { "胡淇钧","周光远","尧智越","完晶","诸奥翔","年舒","丘照南","宫恩","不婷","融美含","告润茁","荀芳宁",
                "卯爱棋","贡玉宁","洋泽勋","尉兴娜","弘宜","闵之","左俊杰","仲雅雯","武博硕","赵梓希","茆乐萱","希光星","濯智营",
                "宣晨曦","栋翌喆","邴玉浩","泰一哲","丛艺涵","左丘奕洳","诗凯","让志丹","纳喇一然","季熙晨","樊一","营潇郡","红鉴恒",
                "绳逸翔","绪嘉木","纳光磊","化柯欣","杨树果","盘千","说洋泽","波世玉","隗树涵","似一鸣","史柯汝","妫哲玮","强舒昕",
                "崔悦轩","畅羽沫","及宏骞","昔诗","窦月","士一钧","蒿炳诺","连久","称博文","禄皓","计世杰","涂宇","苗玉涵","琴洋博",
                "本普涵","青涵","舜秋羽","隐云辰","蒉世豪","胥汉霖","刁洪滨","相小秋","菅一鸣","犁晟华","塞泉润","戚斯","顾宇杰",
                "羽明璨","缑玉曼","轩辕东旭","祢智","方梦森","宛庆晨","漆睿","闭馨阳","芒函","段干皓","琦一"};*/
        String[] nameList = {"大师","来来","超超","豫哥","希希","烁烁","轩轩"};
        String name = nameList[new Random().nextInt(nameList.length)];
//        while(cacheName.indexOf(name)!=-1) {
//            name = nameList[new Random().nextInt(nameList.length)];
//        }
        return name;
    }

}
