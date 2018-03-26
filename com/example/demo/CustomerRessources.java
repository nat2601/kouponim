package com.example.demo;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Corpus.Coupon;
import Corpus.Customer;
import DBDAO.CustomerDBDAO;
import Exceptions.CouponSystemExceptions;
import Facade.CustomerFacade;

@CrossOrigin(origins = "*")
@RestController
public class CustomerRessources {
	Customer cust1 = new Customer();;
	CustomerFacade cust = new CustomerFacade();
	CustomerDBDAO cdb = new CustomerDBDAO();

	@RequestMapping(value = "/purchasecoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity purchasecoupon(@RequestBody Coupon c) throws CouponSystemExceptions {

		try {
			cust.purchaseCoupon(c);
		} catch (CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<>("Coupon Purchased", HttpStatus.OK);

	}

	@RequestMapping(value = "/getallpurchasedcoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Collection<Coupon> getallpurchased() throws CouponSystemExceptions, SQLException {
		Collection<Coupon> coupons;
		try {
			coupons = cdb.getallcustcoupon();
		} catch (CouponSystemExceptions e) {
			throw e;

		}
		System.out.println(coupons.toString());
		return coupons;

	}

}
