package com.chuangtu.idcard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdcardTest {


    /**
     * 对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则直接返回
     * @param path
     * @return
     */
    public static String img_base64(String path){

        String imgBase64="";
        if (path.startsWith("http")){
            imgBase64 = path;
        }else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = new String(Base64.encodeBase64(content));
            } catch (IOException e) {
                e.printStackTrace();
                return imgBase64;
            }
        }

        return imgBase64;
    }

    /**
     * 获取身份证敏感信息坐标
     * @param imgFile
     * @param appcode
     * @return
     */
    public static JSONObject getIdcardPositionInfo(String imgFile , String appcode) {
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String method = "POST";

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");

        Map<String, String> querys = new HashMap<String, String>();
        // 对图像进行base64编码
        String imgBase64 = img_base64(imgFile);

        //configure配置
        JSONObject configObj = new JSONObject();
        configObj.put("side", "face");

        String config_str = configObj.toString();

        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        requestObj.put("image", imgBase64);
        if (configObj.size() > 0) {
            requestObj.put("configure", config_str);
        }
        String bodys = requestObj.toString();
        JSONObject res_obj=new JSONObject();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                /*System.out.println("Http code: " + stat);
                System.out.println("http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));*/
                res_obj.put("state",stat);
                res_obj.put("msg", EntityUtils.toString(response.getEntity()));
                return res_obj;
            }

            String res = EntityUtils.toString(response.getEntity());
            res_obj = JSON.parseObject(res);

            return res_obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res_obj;
    }

    public static void main(String[] args) {
        JSONObject idcardPositionInfo = getIdcardPositionInfo("C:\\Users\\PC0825\\Desktop\\zhaopian(1)\\2.jpg", "961e82c586aa47ec9ef3c48525b8697c");

        String s = idcardPositionInfo.toJSONString();
        IdcardPojo idcardPojo = JSONObject.parseObject(s, IdcardPojo.class);
        System.out.println(idcardPojo);
        //获取人脸中心点坐标
        List faceRect = idcardPojo.getFaceRect();
        Object obj = faceRect.get(0);
        String s1 = obj.toString();
        FaceRect faceRect1 = JSONObject.parseObject(s1, FaceRect.class);
        Object center = faceRect1.getCenter();
        String s2 = center.toString();
        Coordinate centers = JSONObject.parseObject(s2, Coordinate.class);
        Point centerPoint = new Point(Double.parseDouble(centers.getX()), Double.parseDouble(centers.getY()));
        System.out.println(centerPoint);
        //获取身份证四个顶点坐标
        Object[] cardRegion = idcardPojo.getCardRegion();


        if (cardRegion != null && cardRegion.length != 0) {
            Point[] points = new Point[4];

            int i = 0;
            for (Object o : cardRegion) {
                String s3 = o.toString();
                Coordinate coordinate1 = JSONObject.parseObject(s3, Coordinate.class);

                Point point = new Point(Double.parseDouble(coordinate1.getX()),Double.parseDouble(coordinate1.getY()));
                points[i] = point;
                i++;
            }
            for (Point point : points) {
                System.out.println(point);
            }
        }



    }
}
