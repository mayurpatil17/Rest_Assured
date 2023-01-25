package tests;
import static io.restassured.RestAssured.*;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;

public class GetCourse {

	public static void main(String[] args) throws InterruptedException {

		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.id("identifierId")).sendKeys("mayur.patil1@cogniwize.com");
		driver.findElement(By.xpath("//span[text()='Next']")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("password")).sendKeys("2JHt5AQp");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[text()='Next']")).click();
		Thread.sleep(3000); //---------------------------------------

		String Url= driver.getCurrentUrl();
		

		System.out.println(Url);

		//String Url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh4s_JzgUqE_N8O13Hlr5SjdccVYciBN00zvWObbPJ_6aYhQmFtr4j3N9N_vw8Xb6g&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=1&prompt=none";
		String partialUrl = Url.split("code=")[1];
		String code = partialUrl.split("&scope")[0];

		//----------Get Access Token:---------------------------------------------------------------------------------
		String accessTokenResponse=
				given()
				.log().all()
				.urlEncodingEnabled(false)    //for terminate the scope of % in url
				.queryParam("code",code)
				.queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" )
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type","authorization_code" )
				.when()
				.post("https://www.googleapis.com/oauth2/v4/token")
				.asString();

		System.out.println(accessTokenResponse);

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);

		//-----------Get Course:---------------------------------------------------------------------------------
		String getCourseResponse=
				given()
				.log().all()
				.queryParam("access_token", accessToken)
				.when()
				.get("https://rahulshettyacademy.com/getCourse.php")
				.asString();
		System.out.println(getCourseResponse);
	}
}
