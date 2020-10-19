package com.chuangtu.idcard;

/**
 * 比例枚举
 */
public enum ProportioEnum {

	//身份证号
	idNumber(0.5416666666666667, 0.0805467372134039, 0.3157894736842105, 0.8055784832451499),
	//姓名
	name(0.2460850111856823, 0.0986524822695035, 0.1733780760626398, 0.104113475177305),
	//性别
	gender(0.0503355704697987, 0.0797872340425532, 0.1733780760626398, 0.2446808510638298),
	//民族
	national(0.0503355704697987, 0.0797872340425532,0.3713646532438479, 0.2446808510638298),
	//生日
	birthday(0.3557046979865772, 0.0797872340425532,0.1733780760626398,0.3687943262411348),
	//地址
	address(0.421834451901566, 0.2482269503546099, 0.1733780760626398, 0.4893617021276596),
	//面部
	face(0,0,0,0)
	;


	ProportioEnum(double targetWith , double targetHeight, double iocationWith, double iocationHeight) {
		this.targetWith = targetWith;
		this.targetHeight = targetHeight;
		this.iocationWith = iocationWith;
		this.iocationHeight = iocationHeight;
	}

	private double targetWith;
	private double targetHeight;
	private double iocationWith;
	private double iocationHeight;

	/**
	 * 获取目标对象的宽度比
	 * @return
	 */
	public double getTargetWith(){
		return targetWith;
	}

	/**
	 * 获取目标对象的高度比
	 * @return
	 */
	public double getTargetHeight(){
		return targetHeight;
	}
	/**
	 * 获取位置坐标的宽度比
	 * @return
	 */
	public double getIocationWith(){
		return iocationWith;
	}
	/**
	 * 获取位置坐标的高度比
	 * @return
	 */
	public double getIocationHeight(){
		return iocationHeight;
	}
}
