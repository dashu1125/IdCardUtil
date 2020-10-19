package com.chuangtu.idcard;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

/**
 * 图片处理类
 * @author hty
 *
 */
public class IdCardUtil {


	public static File desensitization(File file, Point[] p, Point center, ProportioEnum proportion, Map<String,Double> map) throws Exception{

		if ("face".equals(proportion.name())){

            //获取马赛克区域的长和宽
            double width = map.get("width");
            double height = map.get("height");
            int radius = 0;
            if (width>height){
                radius = (int) (width/2);
            }else {
                radius = (int) (height/2);
            }
			//获取人脸顶点坐标
			Point[] points = getFacePoint(radius,center);
			//给人脸加马赛克
            File faceFile = mosaicFace(file,points,radius,20);
			if (faceFile != null) {
				file = faceFile;
				return file;
			}
		}
		int firstIdex = getInterval(p ,center);

		Point first = p[firstIdex];

		double with = getLength(first, p[getIndex(firstIdex, p.length, 1)]);
		double height = getLength(first, p[getIndex(firstIdex, p.length, p.length -1)]);
		double xAngle = getAngle(first, p[getIndex(firstIdex, p.length, 1)]);
		double yAngle = getAngle(first, p[getIndex(firstIdex, p.length, p.length -1)]);

		//身份证号长宽
		double targetWith = with * proportion.getTargetWith();
		double targetHeight = height * proportion.getTargetHeight();

		//位置坐标长宽
		double locationWith = with * proportion.getIocationWith();
		double locationHeight = height * proportion.getIocationHeight();

		double y = first.y + locationWith * Math.sin(xAngle) + locationHeight * Math.sin(yAngle);
		double x = (first.x + locationHeight * Math.cos(yAngle))  + locationWith * Math.cos(xAngle)  ;

		Point a = new Point(x ,y);
		Point b = getPoint(a, targetWith, xAngle);
		Point c = getPoint(b, targetHeight, yAngle);
		Point d = getPoint(a, targetHeight, yAngle);

		return mosaic(file,a,b,c,d,15);

	}

    private static File mosaicFace(File file,Point[] points,Integer radius,int mosaicSize) throws IOException {
        if (!file.isFile()) {
            throw new RuntimeException("传入的不是文件");
        }
        int index = file.getName().lastIndexOf(".");
        String suffix = file.getName().substring(index + 1);


        BufferedImage bi = ImageIO.read(file); // 读取该图片
        BufferedImage spinImage = new BufferedImage(bi.getWidth(),
                bi.getHeight(), BufferedImage.TYPE_INT_RGB);

        //2. 设置绘制的马赛克块个数
        int count = (int)Math.floor( radius*2/ mosaicSize) +1; // 方向绘制个数

        //3. 绘制马赛克(绘制矩形并填充颜色)
        Graphics2D gs = spinImage.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.drawImage(bi, 0, 0, null);

        //起点坐标
        Point tmp = points[0];


        for (int i = 0; i < count; i++) {

            Point newline = tmp;
            for (int j = 0; j < count; j++) {
                //马赛克矩形格大小
                double mwidth = mosaicSize;
                double mheight = mosaicSize;
                //矩形颜色取中心像素点RGB值
                double centerX = tmp.x;
                double centerY = tmp.y;
                if (mwidth % 2 == 0) {
                    centerX += mwidth / 2;
                } else {
                    centerX += (mwidth - 1) / 2;
                }
                if (mheight % 2 == 0) {
                    centerY += mheight / 2;
                } else {
                    centerY += (mheight - 1) / 2;
                }
                Color color = new Color(bi.getRGB((int) centerX, (int) centerY));
                gs.setColor(color);
                Point tmp2 = new Point(tmp.x+mwidth,tmp.y);
                Point tmp3 = new Point(tmp.x+mwidth,tmp.y+mwidth);
                Point tmp4 = new Point(tmp.x,tmp.y+mwidth);

                if(j == 0){
                    newline = tmp4;
                }

                gs.fill(getCutPath(tmp, tmp2, tmp3, tmp4));
                tmp = tmp2;
            }
            tmp = newline;
        }
        gs.dispose();
        File outputfile = new File(UUID.randomUUID()+"."+suffix);
        ImageIO.write(spinImage, suffix, outputfile);
        return outputfile;
    }

    //获取人脸顶点坐标
	private static Point[] getFacePoint(int radius, Point center) {
		Point[] points = new Point[4];
        Point p1 = new Point(center.x - radius, center.y - radius);
        Point p2 = new Point(center.x + radius, center.y - radius);
        Point p3 = new Point(center.x + radius, center.y + radius);
        Point p4 = new Point(center.x - radius, center.y + radius);

		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		points[3] = p4;
		return points;
	}

	/**
	 * 给图片指定位置打马赛克
	 * @param file 图片位置
	 * @param mosaicSize 马赛克尺寸，即每个矩形的长宽
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static File mosaic(File file,Point p1, Point p2, Point p3, Point p4, int mosaicSize) throws IOException {
		if (!file.isFile()) {
			throw new RuntimeException("传入的不是文件");
		}
		int index = file.getName().lastIndexOf(".");
		String suffix = file.getName().substring(index + 1);


		BufferedImage bi = ImageIO.read(file); // 读取该图片
		BufferedImage spinImage = new BufferedImage(bi.getWidth(),
				bi.getHeight(), BufferedImage.TYPE_INT_RGB);

		double width = getLength(p1, p2);
		double height = getLength(p1, p4);

		//2. 设置各方向绘制的马赛克块个数
		int xcount = (int)Math.floor( width/ mosaicSize) +1; // 方向绘制个数
		int ycount = (int)Math.floor( height/ mosaicSize) +1; // y方向绘制个数


		//3. 绘制马赛克(绘制矩形并填充颜色)
		Graphics2D gs = spinImage.createGraphics();
		gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gs.drawImage(bi, 0, 0, null);
		//Color color = new Color(255,255,255);
		//gs.setColor(color);
		//gs.fill(getCutPath(p1, p2, p3, p4));
		double xAngle = getAngle(p1 ,p2);
		double yAngle = getAngle(p1 ,p4);
		Point tmp = new Point(p1.x, p1.y);


		for (int i = 0; i < ycount; i++) {

			Point newline = tmp;
			for (int j = 0; j < xcount; j++) {
				//马赛克矩形格大小
				double mwidth = mosaicSize;
				double mheight = mosaicSize;
				/*if(i == xcount - 1){   //横向最后一个比较特殊，可能不够一个size
					mwidth = width - tmp.x;
				}
				if(j == ycount - 1){  //同理
					mheight = height - tmp.y;
				}*/
				//矩形颜色取中心像素点RGB值
				double centerX = tmp.x;
				double centerY = tmp.y;
				if (mwidth % 2 == 0) {
					centerX += mwidth / 2;
				} else {
					centerX += (mwidth - 1) / 2;
				}
				if (mheight % 2 == 0) {
					centerY += mheight / 2;
				} else {
					centerY += (mheight - 1) / 2;
				}
				Color color = new Color(bi.getRGB((int) centerX, (int) centerY));
				gs.setColor(color);
				Point tmp2 = getPoint(tmp, mwidth,xAngle);
				Point tmp3 = getPoint(tmp2, mheight,yAngle);
				Point tmp4 = getPoint(tmp, mheight,yAngle);

				if(j == 0){
					newline = tmp4;
				}


				gs.fill(getCutPath(tmp, tmp2, tmp3, tmp4));
				tmp = tmp2;
			}
			tmp = newline;
		}

		gs.dispose();
		File outputfile = new File(UUID.randomUUID()+"."+suffix);
		ImageIO.write(spinImage, suffix, outputfile);

		return outputfile;
	}

	/**
	 * 获取区域路径
	 *
	 * @return
	 */
	public static GeneralPath getCutPath(Point p1, Point p2, Point p3, Point p4) {
		GeneralPath path = new GeneralPath();
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p4.x, p4.y);
		path.lineTo(p1.x, p1.y);

		return path;
	}


	/**
	 * 与x轴角度
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getAngle(Point p1, Point p2) {
		return  Math.atan2((p2.y - p1.y), (p2.x - p1.x));
	}


	/**
	 * 获取指定区域的点坐标
	 * @param p
	 * @param length
	 * @param angle
	 * @return
	 */
	public static Point getPoint(Point p, double length, double angle) {
		double x = length * Math.cos(angle) + p.x;
		double y = (length * Math.sin(angle)) + p.y;


		return new Point(x,y);
	}


	/**
	 * 获取两点间长度
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getLength(Point p1, Point p2) {
		return Math.sqrt(Math.abs((p1.getX() - p2.getX())* (p1.getX() - p2.getX())+(p1.getY() - p2.getY()) * (p1.getY() - p2.getY())));

	}
	
	
	/**
			* 返回身份证左上角坐标
	 * @return
			 */
	public static  int getIndex(int current,int length, int size){
		int next = current;

		for(int i =0; i<size; i++){
			if(next == length -1){
				next = 0;
			}else {
				next ++;
			}

		}

		return next;

	}

	/**
	 * 返回身份证左上角坐标
	 * @param center
	 * @return
	 */
	public static  int getInterval(Point[] p, Point center){

		double with = getLength(p[0], p[1]);
		double height = getLength(p[0], p[3]);

		// p[0] - p[1], p[2]-p[3]
		if(with > height){
			Point minpoint = minpoint(p[0], p[1]);
			Point minpoint2 = minpoint(p[2], p[3]);
			if(check(p[0],minpoint,minpoint2,p[3],center)){
				return 2;
			}else {
				return 0;
			}

			//p1-p4 , p2-p3
		}else {
			Point minpoint = minpoint(p[0], p[3]);
			Point minpoint2 = minpoint(p[1], p[2]);
			if(check(p[0],minpoint,minpoint2,p[1],center)){
				return 3;
			}else {
				return 1;
			}
		}

	}


	//两点坐标的中点
	public static Point minpoint(Point a,Point b){
		double x=(a.getX()+b.getX())/2;
		double y=(a.getY()+b.getY())/2;
		return new Point(x, y);
	}


	/**
	 * 一个点是否在多边形内
	 * @return
	 */
	private static boolean check(Point p1, Point p2, Point p3, Point p4, Point center) {
		Point2D.Double center2 = new Point2D.Double(center.x, center.y);
		Point2D.Double d1 = new Point2D.Double(p1.x, p1.y);
		Point2D.Double d2 = new Point2D.Double(p2.x, p2.y);
		Point2D.Double d3 = new Point2D.Double(p3.x, p3.y);
		Point2D.Double d4 = new Point2D.Double(p4.x, p4.y);


		GeneralPath peneralPath = new GeneralPath();


		peneralPath.moveTo(d1.x, d1.y);
		peneralPath.lineTo(d2.x, d2.y);
		peneralPath.lineTo(d3.x, d3.y);
		peneralPath.lineTo(d4.x, d4.y);
		peneralPath.lineTo(d1.x, d1.y);
		peneralPath.closePath();
		// 测试指定的 Point2D 是否在 Shape 的边界内。
		return peneralPath.contains(center2);
	}

}