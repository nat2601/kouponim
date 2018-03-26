package com.example.demo;

import Corpus.Coupon;
import Corpus.CouponType;
import DBDAO.CouponDBDAO;
import Facade.CompanyFacade;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class CompanyRessources {

	CompanyFacade comp = new CompanyFacade();
	CouponDBDAO cd = new CouponDBDAO();

	// Create Coupon
	@RequestMapping(value = "/createcoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity createCoupon(@RequestBody Coupon coupon)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		try {
			comp.createCoupons(coupon);
		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

	// Remove Coupon
	@RequestMapping(value = "/removecoupon", method = RequestMethod.DELETE, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity removeCoupon(@RequestBody Coupon c)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		try {
			comp.removeCoupon(c);
		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<>("Succes", HttpStatus.OK);

	}

	// Update Coupon
	@RequestMapping(value = "/updatecoupon", method = RequestMethod.PUT, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public void updateCoupon(@RequestBody Coupon coupon) throws ParseException {
		comp.updateCoupon(coupon);
	}

	@RequestMapping(value = "/getallcoupon", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody Collection<Coupon> getCoupon()
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		Collection<Coupon> coupons = new ArrayList<>();
		try {
			coupons = cd.getAllCoupon();
		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}
		System.out.println(coupons);
		return coupons;

	}

	@RequestMapping(value = "/getcoupon/{id}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Coupon> getCoupon(@PathVariable("id") int id)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		Coupon couponByid = new Coupon();
		try {
			couponByid = comp.getCoupon(id);

		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<Coupon>(couponByid, HttpStatus.OK);

	}

	@RequestMapping(value = "/getcoupon", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Collection<Coupon>> getCouponByType(@RequestParam("type") @RequestBody CouponType type)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		Collection<Coupon> couponType = new ArrayList<>();
		try {
			couponType = comp.getCouponByType(type);
		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}

		return new ResponseEntity<Collection<Coupon>>(couponType, HttpStatus.OK);
	}

	@RequestMapping(value = "/getcouponbyprice/{maxprice}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Collection<Coupon>> getcouponByprice(@PathVariable("maxprice") long maxprice)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		Collection<Coupon> coup = new ArrayList<>();
		try {
			coup = comp.getCouponByprice(maxprice);
		} catch (Exceptions.CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<Collection<Coupon>>(coup, HttpStatus.OK);
	}

	@RequestMapping(value = "/getcouponbyenddate", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody Collection<Coupon> getCouponByTheDateEnd(@RequestParam("date") @RequestBody String date)
			throws CouponSystemExceptions, Exceptions.CouponSystemExceptions {
		Collection<Coupon> coupons = new ArrayList<>();
		try {
			coupons = comp.getCouponByTheDateEnd(date);
		} catch ( Exceptions.CouponSystemExceptions e) {
			throw e;
		}

		return coupons;

	}

	@RequestMapping(value = "/getcompanycoupon/{compId}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody Collection<Coupon> getCompanyCoupons(@PathVariable("compId") long compId) {
		Collection<Coupon> coupons = comp.getCompanyCoupons(compId);
		;
		return coupons;
	}

}
