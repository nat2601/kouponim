package Corpus;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Company implements Serializable {
	private long id;
	private String compname;
	private String password;
	private String email;
	private Collection<Coupon> coupons;

	public Company(long id, String compName, String password, String email, Collection<Coupon> coupons) {
		this.id = id;
		this.compname = compName;
		this.password = password;
		this.email = email;
		this.coupons = coupons;
	}

	public Company() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compname;
	}

	public void setCompName(String compName) {
		this.compname = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", compName=" + compname + ", password=" + password + ", email=" + email
				+ ", coupons=" + coupons + "]";
	}

}
