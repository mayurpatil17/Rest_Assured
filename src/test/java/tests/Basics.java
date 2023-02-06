package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Files.PayLoads;

public class Basics {

	public static void main(String[] args) throws IOException {
//---------------Add Place---------------------------------------------------------------------------
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String addPlaceResponse = given()
			.log().all()
			.queryParam("key", "qaclick123")
			.header("Content-Type","application/json")
			//.body(PayLoads.AddPlacePayload())
			.body(new String(Files.readAllBytes(Paths.get("E:\\Rest Assured\\Demo.json"))))
			
			.when()
				.post("/maps/api/place/add/json")
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("scope", equalTo("APP"))
				.header("server", "Apache/2.4.41 (Ubuntu)")
				.extract().response().asString();
		
			JsonPath js = new JsonPath(addPlaceResponse);
			String placeId = js.getString("place_id");
			System.out.println(placeId);
			
//----------Update Place----------------------------------------------------------------:
			given()
				.log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.header("Content-Type","application/json")
				.body("{\r\n" + 
						"\"place_id\":\""+placeId+"\",\r\n" + 
						"\"address\":\"70 winter walk, USA\",\r\n" + 
						"\"key\":\"qaclick123\"\r\n" + 
						"}")
			.when()
				.put("/maps/api/place/update/json")
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));
			
//---------------Get Place:--------------------------------------------------------------
			String newaddress="70 winter walk, USA";
			String addPlaceAddress = given()
				.log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
			.when()
				.get("/maps/api/place/get/json")
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("address", equalTo("70 winter walk, USA"))
				.header("Server", "Apache/2.4.41 (Ubuntu)")
				.extract().response().asString();                       //for Json Path this line is compulsory to get variable for ex. PlaceAddress
			
			JsonPath js1 = new JsonPath(addPlaceAddress);
			String ActualAddress = js1.getString("address");
			
	        assertEquals(ActualAddress, newaddress);
			
//-------------------------------------------------------------------------------------------
				
	}
}
