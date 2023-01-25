package tests;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;

import Files.PayLoads;
import Files.ReusableMethods;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		//1. Print No of courses returned by API

		JsonPath js = ReusableMethods.rawToJson(PayLoads.getCoursesPayload());
		int courseCount = js.getInt("courses.size()");
		System.out.println("No. of Courses : "+courseCount);
		System.out.println("-----------------------------------------------------------------");

		//2.Print Purchase Amount

		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount is : "+purchaseAmount);

		System.out.println("-----------------------------------------------------------------");

		//3. Print Title of the first course

		String titleOfFirstCourse = js.getString("courses[0].title");
		System.out.println("Title of the first course : "+titleOfFirstCourse);

		System.out.println("-----------------------------------------------------------------");

		//4. Print All course titles and their respective Prices

		for(int i=0;i<courseCount;i++)
		{
			String CoursesTitles = js.getString("courses["+i+"].title");
			System.out.println(CoursesTitles);

			int PricesOfCourses = js.getInt("courses["+i+"].price");
			System.out.println(PricesOfCourses);
		}

		System.out.println("-----------------------------------------------------------------");

		//5. Print no of copies sold by RPA Course

		for(int j=0;j<courseCount;j++)
		{
			if(js.getString("courses["+j+"].title").equals("RPA"))
			{
				int copies = js.getInt("courses["+j+"].copies");
				System.out.println("Number of Copies are :"+copies);
			}
		}

		System.out.println("-----------------------------------------------------------------");

		//6. Verify if Sum of all Course prices matches with Purchase Amount

		int CoursePrice=0;
		for(int k=0;k<courseCount;k++)
		{
			int price = js.getInt("courses["+k+"].price");
			int copies = js.getInt("courses["+k+"].copies");

			CoursePrice=CoursePrice+(price*copies); 
		}

		System.out.println("Sum of all Course prices :"+CoursePrice);

		Assert.assertEquals(CoursePrice, purchaseAmount,"Assertion Failed");		
	}

}
