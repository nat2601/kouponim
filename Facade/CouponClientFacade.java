package Facade;

import Exceptions.CouponSystemExceptions;

public interface CouponClientFacade {
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemExceptions;
}
