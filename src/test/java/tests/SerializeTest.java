package tests;

import java.util.ArrayList;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import pojo.AddPlace;
import pojo.Location;
import pojo.addPlaceResponsePOJO;

public class SerializeTest {

	public static void main(String[] args) {
		//serialization is achieved by setters
		
		AddPlace p = new AddPlace();
		p.setAccuracy("50");
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://google.com");
		p.setLanguage("French-IN");
		
		Location l = new Location();
		l.setLat("-38.383494");
		l.setLng("33.427362");
		p.setLocation(l);
		
		ArrayList<String> a = new ArrayList<String>();
		a.add("shoe park");
		a.add("shop");
		p.setTypes(a);
		
		//Rest Assured
		
		//RestAssured.baseURI="https://rahulshettyacademy.com";
		RequestSpecification req = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123")
				.setContentType(ContentType.JSON)
				.build();
		
		
		 RequestSpecification addPlaceRequest = given()
			.log().all()
			.spec(req)
			.body(p);
		 
		 String addPlaceResponse = addPlaceRequest
		.when()
			.post("/maps/api/place/add/json")
		.then()
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		System.out.println(addPlaceResponse);
		
		//Deserialization is achieved by getters
		RequestSpecification request=new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key","qaclick123")
                .setContentType(ContentType.JSON)
                .build();
        
        ResponseSpecification res=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
      RequestSpecification addPlaceReq = given()
        .log().all()
        .spec(request)
        .body(p);
         
         addPlaceResponsePOJO apr=addPlaceReq
    .when()
         .post("/maps/api/place/add/json")
     .then()
          .spec(res)
          .extract().response().as(addPlaceResponsePOJO.class);
        
        System.out.println("status    = "+apr.getStatus());
        System.out.println("place_id  = "+apr.getPlace_id());
        System.out.println("reference = "+apr.getReference());
        System.out.println("scope     = "+apr.getScope());
        System.out.println("id        = "+apr.getId());
	}
}













