package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.PayLoads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJSON {

	@Test(dataProvider="getBookData")
	public void AddBook(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";

		String addBookResponse=given()
				.log().all()
				.header("Content-Type","application/json")
				.body(PayLoads.addBookPayload(isbn,aisle))
				
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
	}
	
	//single dimensional array
	//double dimension
	
	
	@DataProvider(name="getBookData")
	public Object[][] PassData()
	{
		return new Object[][] {{"jkl","457"},{"rty","757"},{"jdl","127"}};
	}
	
	
}
