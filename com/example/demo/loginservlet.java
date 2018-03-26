package com.example.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import CouponSystemSingleton.CouponSystem;
import Exceptions.CouponSystemExceptions;
import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CustomerFacade;

@Controller
public class loginservlet {
	@RequestMapping(value = "/loginservlet", method = RequestMethod.GET)
	public @ResponseBody String doget(@RequestParam String uname) {
		return "hello" + uname;
	}

	@RequestMapping(value = "/loginservlet", method = RequestMethod.POST)
	public @ResponseBody ModelAndView dopost(@RequestParam String uname, @RequestParam String pwd,
			@RequestParam String usertype, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// CouponSYSTEM
		CouponSystem cs = CouponSystem.getinstance();
		// Connection

		if (usertype.equals("Administrator")) {
			try {

				HttpSession session = request.getSession();
				session.setAttribute("Auth", "Admin");
				AdminFacade admin = (AdminFacade) cs.login(uname, pwd, ClientType.ADMINISTRATOR);
				return new ModelAndView(
						"redirect:http://35.198.110.35/CouponSystemFinal2-0.0.1-SNAPSHOT/dist/index.html");

			} catch (CouponSystemExceptions e) {

			}

		} else if (usertype.equals("company")) {
			try {
				HttpSession session = request.getSession();
				session.setAttribute("Auth", "Company");
				CompanyFacade compf = (CompanyFacade) cs.login(uname, pwd, ClientType.COMPANY);
				return new ModelAndView(
						"redirect:http://35.198.110.35/CouponSystemFinal2-0.0.1-SNAPSHOT/dist2/Company/index.html");

			} catch (CouponSystemExceptions e) {
				e.printStackTrace();
			}
		} else if (usertype.equals("customer")) {
			try {

				HttpSession session = request.getSession();
				session.setAttribute("Auth", "Customer");
				CustomerFacade custf = (CustomerFacade) cs.login(uname, pwd, ClientType.CUSTOMER);
				System.out.println("session open" + session.getAttribute("Auth"));
				return new ModelAndView(
						"redirect:http://35.198.110.35/CouponSystemFinal2-0.0.1-SNAPSHOT/dist3/Customer/index.html");

			} catch (CouponSystemExceptions e) {
				e.printStackTrace();
			}

		}

		return new ModelAndView("redirect:http://35.198.110.35/CouponSystemFinal2-0.0.1-SNAPSHOT/errorPage.html");

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		System.out.println("Session" + " " + session.getAttribute("Auth") + " " + "closed");
		session.invalidate();
	}
}
