package com.example.twitter;

import java.util.List;

import javax.servlet.annotation.WebServlet;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


import twitter4j.TwitterException;

@SuppressWarnings("serial")
@Theme("twitter")
public class TwitterUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = TwitterUI.class)
	public static class Servlet extends VaadinServlet {
	}
	String user = null;
	
	@Override
	protected void init(VaadinRequest request) {
		
		
            /* defining Vaadin components for layout */
            final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		TextField username = new TextField("Username", "");
		PasswordField password = new PasswordField("Password", "");
		TextField addkeyword = new TextField("You can add keyword here");
		Label success = new Label();
		Button register = new Button("Register");
		Button login = new Button("Login");
		Button logout = new Button("Logout");
		Button addkeywordbutton = new Button("Save keyword");
		Button getkeywords = new Button("Search with your keywords");
		databaseConnection db = new databaseConnection();
		twitterFunctions tw = new twitterFunctions();
		Label header = new Label("This site is for checking your favorite hashtags or keywords from Twitter." + System.lineSeparator() + 
				"You need to register or login and then you can save keywords and search with them." + System.lineSeparator() + "The search returns 15 latest tweets from all of your keywords");
		
		HorizontalLayout hor = new HorizontalLayout();
		VerticalLayout ver = new VerticalLayout();
		hor.setSpacing(true);
		layout.addComponent(header);
		layout.addComponent(success);
		layout.addComponent(username);
		layout.addComponent(password);
		hor.addComponents(login, register);
		layout.addComponent(hor);
		header.setWidth("800px");
		layout.setComponentAlignment(header, Alignment.TOP_CENTER);
		
		

		 /* 
                functions for button named getkeywords
                this function adds or updates the view for user-defined keywords
                it calls the twitter function from twitterFunctions class to search those keywords from twitter 
                */
		
		getkeywords.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				ver.removeAllComponents();
				layout.addComponent(ver);
				// TODO Auto-generated method stub
				@SuppressWarnings("rawtypes")
				List keywords = db.Getkeywords(user);
				TextArea[] Textrea = new TextArea[keywords.size()];
				for (int i = 0; i < keywords.size(); i++)
				{
					try {
						
						Textrea[i] = new TextArea("#"+keywords.get(i).toString());
						Textrea[i].setHeight("200px");
						Textrea[i].setWidth("1000px");
						Textrea[i].setValue(tw.twitter(keywords.get(i).toString()));
						ver.addComponent(Textrea[i]);
						
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		
		/*
                functions for button named addkeywordbutton 
                this button calls AddKeywords method from databaseConnection class
                and saves users keyword to database and then clears the addkeyword textfield
                */
		addkeywordbutton.addClickListener(new Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				db.AddKeywords(user, addkeyword.getValue());
				addkeyword.clear();
			}
			
		});
		
		
                /* 
                functions for button named login
                this button takes username and passwords and compares them to ones in database
                by calling Login method from databaseConnection class
                Login function returns password matching the username and then this function
                compares the returned and user defines passwords and if they match 
                it then changes the UI by removing options to login or register and instead giving option to 
                logout or add keywords or check keywords
                if the username and password won't match then it gives notification about that for user
                */
                login.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				
				user = username.getValue();	
				String login = db.Login(username.getValue()).toString();
					if (("[" + password.getValue() + "]").equals(login))
					{
						
						
						success.setValue("Succesful login " + user);
						layout.addComponent(logout);
						layout.removeComponent(username);
						layout.removeComponent(password);
						layout.removeComponent(hor);
						layout.addComponent(addkeyword);
						layout.addComponent(addkeywordbutton);
						layout.addComponent(getkeywords);
						
					}
					else
					{
						success.setValue("Check your username and password");
					}
			}
			
		});
		
		
		/* 
                functions for button named register
                takes username and password and check if they are at least 5 characters long and calls function Register from databaseConnection
                which then saves them to database (and checks if the username is already taken)
                if those matches it then changes the UI by removing options to login or register and instead giving option to 
                logout or add keywords or check keywords
                if the username is already taken it gives notification about that for user
                
                */
		register.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				try
					{
					// TODO Auto-generated method stub
					
					if (username.getValue().length() < 5 || password.getValue().length() < 5){
						success.setValue("Username and password must be at least 5 characters long");
					}
					else {
					db.Register(username.getValue(), password.getValue());
					user = username.getValue();
					layout.addComponent(logout);
					layout.removeComponent(username);
					layout.removeComponent(password);
					layout.removeComponent(hor);
					layout.addComponent(addkeyword);
					layout.addComponent(addkeywordbutton);
					layout.addComponent(getkeywords);
					success.setValue("Succesful registration, you are now logged in " + user);
					}
					}
				catch (Exception e)
				{
					success.setValue("The username is already taken");
				}
					
				}
		}); 
		
		/* 
                functions for button named logout 
                changes the UI by removing user defined keyword searches, adding keywords and the search for them
                and adds the choices to login or register
                */
                logout.addClickListener(new Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				user = null;
				layout.addComponent(username);
				username.clear();
				layout.addComponent(password);
				password.clear();
				hor.addComponents(login, register);
				layout.addComponent(hor);
				ver.removeAllComponents();
				layout.removeComponent(addkeyword);
				layout.removeComponent(addkeywordbutton);
				layout.removeComponent(getkeywords);
				layout.removeComponent(logout);
				success.setValue("");
			}
			
		});
		
		
		
		
	}

}