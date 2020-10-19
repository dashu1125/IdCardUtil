package com.chuangtu.idcard;

public class Point {

	double x;
	double y;

	public Point() {
	}

	Point(double x, double y)
	{
		this.x=x;
		this.y=y;
	}

	double getX() {
		return x;
	}

	void setX(double x) {
		this.x = x;
	}

	double getY() {
		return y;
	}

	void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {

		return "x==>" + x +"    y==>" + y;
	}
}
