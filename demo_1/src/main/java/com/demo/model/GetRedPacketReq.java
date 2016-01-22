package com.demo.model;

/**
 * 存储红包信息请求
 * @author xiang.cheng
 *
 */
public class GetRedPacketReq {

	/**
	 * 艺龙卡号
	 */
	private long cardNo;
	
	/**
	 * 消息类型
	 * 1：红包到账  2：红包过期
	 */
	private int type;
	
	/**
	 * 红包面值
	 */
	private String faceValue;
	
	/**
	 * 红包名称
	 */
	private String packetName;
	
	/**
	 * 红包充值类型
	 */
	private int rechargeType;
	
	/**
	 * 红包充值名称
	 */
	private String rechargeName;
	
	/**
	 * 红包使用截止日期
	 */
	private int useDeadline;
	
	/**
	 * 红包过期时间（type=2）
	 */
	private int limitTimeToUse;

  /**
   * couponType=1的是折扣，对应的是折扣8.8折，couponType=2是满减，满减100.00元，
   */
  private int couponType=2;

  /**
   * 红包的业务渠道，1是 业务平台 2是国际酒店
   */
  private int businessType =1;

  public int getBusinessType() {
    return businessType;
  }

  public void setBusinessType(int businessType) {
    this.businessType = businessType;
  }

  public long getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

  public int getCouponType() {
    return couponType;
  }

  public void setCouponType(int couponType) {
    this.couponType = couponType;
  }

  public String getFaceValue() {
    return faceValue;
  }

  public void setFaceValue(String faceValue) {
    this.faceValue = faceValue;
  }

  public String getPacketName() {
		return this.packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

	public int getRechargeType() {
		return this.rechargeType;
	}

	public void setRechargeType(int rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getRechargeName() {
		return this.rechargeName;
	}

	public void setRechargeName(String rechargeName) {
		this.rechargeName = rechargeName;
	}

	public int getUseDeadline() {
		return this.useDeadline;
	}

	public void setUseDeadline(int useDeadline) {
		this.useDeadline = useDeadline;
	}

	public int getLimitTimeToUse() {
		return this.limitTimeToUse;
	}

	public void setLimitTimeToUse(int limitTimeToUse) {
		this.limitTimeToUse = limitTimeToUse;
	}
	
}
