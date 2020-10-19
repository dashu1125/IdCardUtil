package com.chuangtu.idcard;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Test {


	public static void main(String[] args) {
		try {

			// 通过阿里orc服务获取参数
			Point[] p =  new Point[4];
			p[0] = new Point(104,263);
			p[1] = new Point(913,272);
			p[2] = new Point(911,1530);
			p[3] = new Point(114,1527);
			//头像中心
			Point center = new Point(553,1252);

            Map<String,Double> map = new HashMap<String, Double>();

            map.put("width",(double)370);
            map.put("height",(double)450);


            File file = new File("C:\\Users\\PC0825\\Desktop\\zhaopian(1)\\4.jpg");
            File desensitizationFace = IdCardUtil.desensitization(file, p, center, ProportioEnum.face,map);
            File desensitizationId = IdCardUtil.desensitization(desensitizationFace, p, center, ProportioEnum.idNumber,map);
            File desensitizationName = IdCardUtil.desensitization(desensitizationId, p, center, ProportioEnum.name,map);
            File desensitization = IdCardUtil.desensitization(desensitizationName, p, center, ProportioEnum.address,map);


            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(desensitization));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("E:\\fillOval4.jpg"));
            byte[]bys = new byte[1024];
            int len;
            while ((len=bis.read(bys))!=-1){
                bos.write(bys,0,len);
            }
            bos.close();
            bis.close();
            desensitizationFace.deleteOnExit();
            desensitizationId.deleteOnExit();
            desensitizationName.deleteOnExit();
            desensitization.deleteOnExit();
        } catch (Exception e) {
			System.err.println("出错了！！！！");
			e.printStackTrace();
		}

	}

}
