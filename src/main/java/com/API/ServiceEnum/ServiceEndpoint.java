package com.API.ServiceEnum;

public enum ServiceEndpoint {
	
	
	verifyToken("https://openapis.angelbroking.com/openApi/user/VerifyToken"),
	userDetails("https://openapis.angelbroking.com/openApi/user/GetUserDetails"),
	orderStatus("https://openapis.angelbroking.com/openApi/trade/GetOrderBook"),
	generateToken("https://openapis.angelbroking.com/openApi/user/GenerateToken"),
	hashTokenLoink("https://openapis.angelbroking.com/openApi/user/FetchToken"),
	generateJWTAPI("https://openapis.angelbroking.com/openApi/user/GenerateJWT"),
	subBrokerLoginAPI("https://openapis.angelbroking.com/openApi/user/SubBrokerLogin"),
	
	sensibullOrderBook("https://openapis.angelbroking.com/openApi/trade/GetOrderBook"),
	sensibullPositions("https://openapis.angelbroking.com/openApi/trade/GetPosition"),
	sensibullPlaceOrder("https://openapis.angelbroking.com/openApi/trade/PlaceOrder"),
	sensibullModifyOrder("https://openapis.angelbroking.com/openApi/trade/ModifyOrder"),
	sensibullCancelOrder("https://openapis.angelbroking.com/openApi/trade/CancelOrder"),
	LoginAPI("https://openapis.angelbroking.com/openApi/user/login");
	
	


	
	private String url;
	ServiceEndpoint(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String data) {
		this.url = data+url;
	}

}
