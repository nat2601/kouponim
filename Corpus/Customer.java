package Corpus;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Customer implements Serializable {
	private long id;
	private String custname;
	private String password;
	private Collection<Coupon> coupons;

	/**
	 * @param id
	 * @param custName
	 * @param password
	 * @param coupons
	 */
	public Customer(long id, String custName, String password, Collection<Coupon> coupons) {
		super();
		this.id = id;
		this.custname = custName;
		this.password = password;
		this.coupons = coupons;
	}

	/**
	 * 
	 */
	public Customer() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custname;
	}

	public void setCustName(String custName) {
		this.custname = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custname + ", password=" + password + ", coupons=" + coupons
				+ "]";
	}

}
