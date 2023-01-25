package tests;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import Files.ReusableMethods;

public class RestAssuredAutomation {

	public static void main(String[] args) {
		//-------------------Login----------------------------------------------------------------

		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter SessFilt = new SessionFilter();

		given()
		.log().all()
		.header("Content-Type","application/json")
		.body("{\r\n" + 
				"    \"username\": \"mayur.patil1\",\r\n" + 
				"    \"password\": \"2JHt5AQp\"\r\n" + 
				"}")
		.filter(SessFilt)
		.when()
		.post("/rest/auth/1/session")
		.then()
		.log().all()
		.extract().response().asString();
		//---------------Create Issue-------------------------------------------------------------------------------------------------

		String idResponse=
				given()
				.log().all()
				.header("Content-Type","application/json")
				.body("{\r\n" + 
						"    \"fields\": {\r\n" + 
						"        \"project\": {\r\n" + 
						"            \"key\": \"RAA\"\r\n" + 
						"        },\r\n" + 
						"        \"summary\": \"My first issue through Rest Assured Automation\",\r\n" + 
						"        \"description\": \"Created through postman\",\r\n" + 
						"        \"issuetype\": {\r\n" + 
						"            \"name\": \"Task\"\r\n" + 
						"        }\r\n" + 
						"    }\r\n" + 
						"}")
				.filter(SessFilt)
				.when()
				.post("/rest/api/2/issue/")
				.then()
				.log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();

		JsonPath js2=ReusableMethods.rawToJson(idResponse);
		String ID=js2.getString("id");

		//---------------Add Comment-----------------------------------------------------------------------------------------

		String expComment="Hi How are you?";

		String AddCommentResponse=
				given()
				.log().all()
				.header("Content-Type","application/json")
				.pathParam("key", ""+ID+"")
				.body("{\r\n" + 
						"    \"body\": \""+expComment+"\"\r\n" + 
						"}")
				.filter(SessFilt)
				.when()
				.post("/rest/api/2/issue/{key}/comment")
				.then()
				.log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();

		JsonPath js = ReusableMethods.rawToJson(AddCommentResponse);
		String AddCommentId = js.getString("id");
		System.out.println(AddCommentId);

		//---------------------get attachment-------------------------------------------------------------------------------------

		given()
		.log().all()
		.filter(SessFilt)
		.pathParam("key", ""+ID+"")
		.header("X-Atlassian-Token","no-check")
		.header("Content-Type","multipart/form-data")
		.multiPart(new File("C:\\Users\\HP\\eclipse-workspace\\RestAssured\\src\\test\\java\\tests\\MyFile.txt"))
		.when()
		.post("/rest/api/2/issue/{key}/attachments")
		.then()
		.log().all()
		.assertThat().statusCode(200);

		//---------------------get issue----------------------------------------------------------------------------------------
		String getIssueResponse=	
				given()
				.log().all()
				.filter(SessFilt)
				.pathParam("key", ""+ID+"")
				.queryParam("fields", "comment")
				.when()
				.get("/rest/api/2/issue/{key}")
				.then()
				.log().all()
				.extract().response().asString();
		
		 JsonPath js1 = ReusableMethods.rawToJson(getIssueResponse);
		String issueResp=js1.getString("comment") ;
//------------Validate Comment----------------------------------------------------------------------------------------------------------
        //Find Count 
		int commentCount = js1.getInt("fields.comment.comments.size()");
		System.out.println("total course count is"+commentCount);

		for(int i=0;i<commentCount;i++)
		{

			//
			if(js1.getString("fields.comment.comments["+i+"].id").equals(AddCommentId))
			{

				String actualComm=js1.getString("fields.comment.comments["+i+"].body");
				Assert.assertEquals(expComment,actualComm);
				System.out.println(actualComm);
				break;
			
			}
		}


	}

}
