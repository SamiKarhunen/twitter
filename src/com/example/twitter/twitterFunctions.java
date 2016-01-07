package com.example.twitter;





import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class twitterFunctions {
	
	public String twitteranswer = "";
	
	
        /*
        this function uses twitter4j library to search with keywords from twitter
        it takes keyword and makes it a hashtag by adding # sign on front of it
        it then does the twitter-api defined procedures for searching with keywords and return 15 newest matching tweet
        */
        public String twitter(String keyword) throws TwitterException
	{
	    twitteranswer = "";  
		ConfigurationBuilder cf = new ConfigurationBuilder();
	        cf.setDebugEnabled(true)
	                .setOAuthConsumerKey("dqDQrlWrWgxSBxwicsHzHsksp")
	                .setOAuthConsumerSecret("psVPbrpspfPloi1Ax12quQxu7mUPUr3KkeSPD2jwweMy9tfS4e")
	                .setOAuthAccessToken("584005088-3XidszfjHDDCAwfBvCfNWDJWXzJXcvduCne4nzS6")
	                .setOAuthAccessTokenSecret("B3NKZN9Txzb9tfqh7Bpa4D7R80yFw4PhE1Xk9WBHfXy7B");
	     
	        
	        TwitterFactory tf = new TwitterFactory(cf.build());
	        twitter4j.Twitter twitter = tf.getInstance();
	        
	      //  ResponseList<twitter4j.Status> status  = twitter.getHomeTimeline();
	       // for (twitter4j.Status st : status)
	       // {
	        //    System.out.println(st.getUser().getName()+""+st.getText());
	       // }
	        
	         // The factory instance is re-useable and thread safe.
	   
	        TwitterFactory.getSingleton();
	    Query query = new Query("#"+keyword);
	    QueryResult result = twitter.search(query);
	    result.getTweets().stream().forEach((status) -> {
		twitteranswer = (twitteranswer + "@" + status.getUser().getScreenName() + ":" + status.getText() + " " + System.lineSeparator());
	    });
	    System.out.println(twitteranswer);
		return twitteranswer;
	}

}
