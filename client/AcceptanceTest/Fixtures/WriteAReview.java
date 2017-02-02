package Fixtures;

import Tests.WriteReviewController;
import control.HomepageUserController;
import entity.SearchBookResult;
import entity.User;
import fit.ActionFixture;

public class WriteAReview extends ActionFixture{
	
	
	
	WriteReviewController wr;
	User user;
	SearchBookResult book;
	
	public void setUP()
	{
		user = new User("Or","Koren","302659743","12345","PerBook","Standard");
		HomepageUserController userMain = new HomepageUserController();
		userMain.setConnectedUser(user);
		book = new SearchBookResult("23", "Steve Jobs", "English", "Summary", "TOC", "key1,key2", "Walter Isaacson", "Biography", "Computers", "34.9");
	}
	
	public void setReviewText(String str)
	{
		wr.setTxtAreaReview(str);
	}
	
	public void sendReview()
	{
		//wr.submitButtonPressed(event);
		wr.btnSubmit.fire();
	}

}
