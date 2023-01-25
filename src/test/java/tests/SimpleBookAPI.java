package tests;

import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import Files.ReusableMethods;

public class SimpleBookAPI {

	public static void main(String[] args) {
		RestAssured.baseURI = "https://simple-books-api.glitch.me";
		
//---------------Get Status of an API--------------------------------------------------------------
		given()
			.log().all()
		.when()
			.get("/status")
		.then()
			.log().all()
			.assertThat().statusCode(200)
			.body("status", equalTo("OK"));
System.out.println("------------------------------------------------------------------------------------");

//---------------Get List of Books -----------------------------------------------------------------

        given()
        	.log().all()
        .when()
        	.get("/books")
        .then()
        	.log().all()
        	.header("Content-Type", "application/json; charset=utf-8")
        	.assertThat().statusCode(200);
System.out.println("--------------------------------------------------------------------------------");

//-------------------Get a single book ------------------------------------------------------
       
		given()
			.log().all()
		.when()
			.get("/books/2")
		.then()
			.log().all()
			.assertThat().statusCode(200);
		
//--------------------Generate Access token for API Authenticaion----------------------------
		String AccessToken=given()
			.log().all()
			.header("Content-Type","application/json")
			.body("{\r\n" + 
					"   \"clientName\": \"Mayur\",\r\n" + 
					"   \"clientEmail\": \"mayurpatil41@gmail.com\"\r\n" + 
					"}")
		.when()
			.post("/api-clients/")
		.then()
			.log().all()
			.assertThat().statusCode(201)
			.extract().response().asString();
		
		JsonPath js  = ReusableMethods.rawToJson(AccessToken);
		String token = js .getString("accessToken");
		System.out.println(token);
//-------------------------------------------------------------------------------------------
//Submit Order
		String orderIDResponse=given()
		.log().all()
		.header("Content-Type", "application/json")
		.header("Authorization", ""+token+"")
		.body("{\r\n"
				+ "  \"bookId\": 4,\r\n"
				+ "  \"customerName\": \"Mayur\"\r\n"
				+ "}")
		
		.when()
			.post("/orders")
			
		.then()
			.log().all()
			.assertThat().statusCode(201)
			.extract().response().asString();
		
		JsonPath js1 = ReusableMethods.rawToJson(orderIDResponse);
		String orderID = js1.getString("orderId");
		System.out.println(orderID);
//------------------------------------------------------------------------------------------------
//Get all orders
		given()
			.log().all()
			.header("Authorization", ""+token+"")
		.when()
			.get("/orders")
		.then()
			.log().all()
			.assertThat().statusCode(200);
System.out.println("---------------------------------------------------------------------------");
//----------------------Get an order -----------------------------------------------------------

		given()
			.log().all()
			.header("Authorization", ""+token+"")
		.when()
			.get("/orders/"+orderID+"")
		.then()
			.log().all()
			.assertThat().statusCode(200);

System.out.println("---------------------------------------------------------------------------");
//----------------------Update an order-----------------------------------------------------------
		given()
			.log().all()
			.header("Authorization", ""+token+"")
			.body("{\r\n" + 
					"  \"customerName\": \"Mayur456Patil\"\r\n" + 
					"}")
		.when()
			.patch("/orders/"+orderID+"")
		.then()
			.log().all()
			.assertThat().statusCode(204);
System.out.println("-----------------------Update order End--------------------------------------------------------");
		
		given()
		.log().all()
		.header("Authorization", ""+token+"")
	.when()
		.get("/orders/"+orderID+"")
	.then()
		.log().all()
		.assertThat().statusCode(200);


System.out.println("----------------------------Get order End---------------------------------------------");
//----------------------Delete an order-----------------------------------------------------------
		given()
			.log().all()
			.header("Authorization", ""+token+"")
		.when()
			.delete("/orders/"+orderID+"")
		.then()
			.log().all()
			.assertThat().statusCode(204);
System.out.println("-------------Delete order End---------------------------------------------------");
		
		
	}
}
