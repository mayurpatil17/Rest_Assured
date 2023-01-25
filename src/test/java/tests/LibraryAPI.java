package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import Files.PayLoads;

public class LibraryAPI {

	public static void main(String[] args) 
	{
	  //Post Book
		
		RestAssured.baseURI = "http://216.10.245.166";

		String addBookResponse=given()
				.log().all()
				.header("Content-Type","application/json")
				.body(PayLoads.addBookPayload("rta", "158"))
			.when()
				.post("/Library/Addbook.php")
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("Msg", equalTo("successfully added"))
				.header("server", "Apache")
				.extract().response().asString();

		JsonPath js = new JsonPath(addBookResponse);
		String BookId = js.getString("ID");
		System.out.println(BookId);

System.out.println("--------------------------------------------------------------");
	//Get Book with author name 

		given()
			.log().all()
			.queryParam("AuthorName", "John foe")
		.when()
			.get("/Library/GetBook.php?AuthorName=John foe")
		.then()
			.log().all()
			.assertThat().statusCode(200)
			.header("Server","Apache");
			
System.out.println("--------------------------------------------------------------");
	//Add Book with Book ID

		given()
			.log().all()
			.queryParam("ID", BookId)
		.when()
			.get("/Library/GetBook.php?ID="+BookId+"")
		.then()
			.log().all()
			.assertThat().statusCode(200)
			.header("Server","Apache");
		
System.out.println("--------------------------------------------------------------");

	//Delete Book By Book ID

        given()
        	.log().all()
        	.header("Content-Type","application/json")
        	.body(PayLoads.DeleteBookIDPayload(BookId))
        .when()
        	.post("/Library/DeleteBook.php")
        .then()
        	.log().all()
        	.assertThat().statusCode(200)
        	.body("msg", equalTo("book is successfully deleted"))     
        	.header("Server", "Apache");  	
	}
}
