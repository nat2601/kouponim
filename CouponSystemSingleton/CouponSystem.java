package CouponSystemSingleton;

import Corpus.ConnectionPool;
import Corpus.Customer;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerDBDAO;
import Exceptions.CouponSystemExceptions;
import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CouponClientFacade;
import Facade.CustomerFacade;


public class CouponSystem {
	Customer c = new Customer();
	private static CouponSystem instance = new CouponSystem();
	// Attributes
	CompanyDBDAO comp = new CompanyDBDAO();
	CustomerDBDAO cust = new CustomerDBDAO();
	CouponDBDAO coup = new CouponDBDAO();
	// Thread
	
	public static CouponSystem getinstance() {
		return instance;

	}

	public CouponSystem() {

	}

	/**
	 * 
	 */
	public void shutdown() {
		
		ConnectionPool.getinstance().closeAllConnection();

	}

	/**
	 * 
	 * @param name
	 * @param password
	 * @param c
	 *            = clientype
	 * @return
	 * @throws CouponSystemExceptions
	 */
	public CouponClientFacade login(String name, String password, ClientType c) throws CouponSystemExceptions {
		if (c == ClientType.ADMINISTRATOR) {
			AdminFacade ad = new AdminFacade();
			System.out.println("Welcome Administrator");
			return ad.login(name, password, ClientType.ADMINISTRATOR);

		} else if (c == ClientType.COMPANY) {
			CompanyFacade compf = new CompanyFacade();
			System.out.println("Welcome Company");
			return compf.login(name, password, ClientType.COMPANY);
		} else if (c == ClientType.CUSTOMER) {
			CustomerFacade custf = new CustomerFacade();
			System.out.println("Welcome Customer");
			return custf.login(name, password, ClientType.CUSTOMER);
		} else {
			throw new CouponSystemExceptions("Youre access is refused");
		}
	}
}
