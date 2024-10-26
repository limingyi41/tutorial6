package nz.ac.vuw.swen301.tuts.log4j;

import java.util.Date;
import java.util.Objects;

public class Purchase {

	private final String retailer;
	private final double amount;
	private final Date date;

	// 构造函数，确保不可变性
	public Purchase(String retailer, double amount, Date date) {
		this.retailer = retailer;
		this.amount = amount;
		this.date = new Date(date.getTime());  // 深复制，防止外部修改 date
	}

	// 获取零售商名称
	public String getRetailer() {
		return retailer;
	}

	// 获取金额
	public double getAmount() {
		return amount;
	}

	// 获取交易日期
	public Date getDate() {
		return new Date(date.getTime());  // 返回一个新的 Date 对象，防止外部修改
	}

	// 重写 toString 方法，确保输出符合要求
	@Override
	public String toString() {
		// 格式化日志输出：Retailer=Pack and Save, Amount=188.00, Date=Sun Jun 03 00:00:00 CST 2018
		return String.format("Retailer=%s, Amount=%.2f, Date=%s", retailer, amount, date.toString());
	}

	// 重写 equals 方法
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Purchase other = (Purchase) obj;
		return Double.compare(amount, other.amount) == 0 &&
				Objects.equals(retailer, other.retailer) &&
				Objects.equals(date, other.date);
	}

	// 重写 hashCode 方法
	@Override
	public int hashCode() {
		return Objects.hash(retailer, amount, date);
	}
}
