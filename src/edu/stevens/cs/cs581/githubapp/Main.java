//
// Main.java
//	This application is responsible for collect data from GitHub.com.
//						BoYu
//						Nov 27, 2011
//

package edu.stevens.cs.cs581.githubapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Language;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;
import java.lang.System;
import org.eclipse.egit.github.core.service.*;

public class Main {
	
	//
	// Output repository info with user.
	//
	
	public static void PrintRepositoryInfo(String user) throws IOException
	{
	    // Get user's repositories.
	    RepositoryService service = new RepositoryService();
	    for (Repository repo : service.getRepositories(user))
	    {
	    	repo.getWatchers();
	    	System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
	    }
	}
	
	//
	// Output watchers information.
	//
	
	public static void PrintWatchers(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		// For each language, print out the relevant repositories(100).
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    System.out.println("Language : " + l);
		    for ( SearchRepository s : repositories)
		    {	
		    	//System.out.println(s.getWatchers());
		    	repositoryCount++;
		    }
		    languageCount++;
	    }
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
	}
	
	//
	// Output forks information.
	//
	
	public static void PrintForks(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		// For each language, print out the relevant repositories(less than 100).
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    //System.out.println("language : " + l);
		    
		    for ( SearchRepository s : repositories)
		    {	
		    	//System.out.println(s.getForks());
		    	repositoryCount++;
		    }
		    languageCount++;
	    }
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
	}
	
	//
	// Output the quantities of contributor with different repositories.
	//
	
	public static void PrintContributorQuantity(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		HashMap<String, Integer> repositoryContributors = new HashMap<String, Integer>();

		// For each language, print out the relevant repositories(less than 100).
		
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    //System.out.println("language : " + l);
		    
		    // Loop every repository.
		    
		    CommitService commitService = new CommitService();
		    for ( SearchRepository s : repositories)
		    {	
		    	List<RepositoryCommit> commits = commitService.getCommits(s);
		    	
		    	//System.out.println("Repository : " + s);
		    	
		    	HashMap<String, Integer> contributorNumbers = new HashMap<String, Integer>();
		    	
		    	// Collect the quantity of contributors.
		    	
		    	// Loop every commit.
		    	for( RepositoryCommit repositoryCommit : commits)
		    	{
		    		User user = repositoryCommit.getCommitter();
		    		
		    		if(user!=null)
		    		{
		    			if(contributorNumbers.containsKey(user.getLogin()))
		    			{
		    				contributorNumbers.put(user.getLogin(), contributorNumbers.get(user.getLogin())+1);
		    			}
		    			else
		    			{
		    				contributorNumbers.put(user.getLogin(), 1);
		    			}
		    		}
		    	}
		    	
		    	// Calculate the total number together.
		    	int totalContributors = 0;
		    	for (Map.Entry<String, Integer> entry : contributorNumbers.entrySet()) 
		 		{
		 	        String key = entry.getKey();
		 	        Integer value = entry.getValue();
		 	        
		 	        totalContributors+= 1;
		 	    }
		    	
		    	// Record.
		    	repositoryContributors.put(s.getName(),	totalContributors);
		    	
		    	repositoryCount++;
		    }
		    
		    languageCount++;
	    }
	    
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
	    
	    // Print contributor information.
	    
	    for (Map.Entry<String, Integer> entry : repositoryContributors.entrySet()) 
		{
	        String key = entry.getKey();
	        Integer value = entry.getValue();
	        
	        //System.out.println("Repository : " + key + ". Contributor number = " + value + " .");
	        System.out.println(value);
	    }
	}
	
	//
	// Output the quantities of commits with different repositories.
	//
	
	public static void PrintCommits(RepositoryService rs, String[] langArray) throws IOException
	{
		CommitService commitService = new CommitService();
		// For each language, print out the relevant repositories(100).
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    
		    // Loop every repository
		    for ( SearchRepository s : repositories)
		    {	
		    	List<RepositoryCommit> commits = commitService.getCommits(s);
		    	System.out.println("Commit size = " + commits.size());
		    }
	    }
	}
	
	//
	// Print commit details with a repository.
	//
	
	public static void PrintCommitInfo(String owner, String name) throws IOException
	{
		SearchRepository repository = new SearchRepository(owner, name);
		System.out.println("Repository name : " + repository.getName());
		CommitService commitService = new CommitService();
		
		List<RepositoryCommit> commits = commitService.getCommits(repository);
		System.out.println("Commit size = " + commits.size());
		
		// Committer => Counts
		HashMap<String, Integer> committers = new HashMap<String, Integer>();
		//HashMap<String, User> committers2 = new HashMap<String, User>();	//
		
		int ignoredCount = 0;
		// Loop commits
		for(RepositoryCommit rc : commits)
		{
			User user = rc.getCommitter();
			
			// Recored user information into hash.
			if(user!=null)
			{
				if(committers.containsKey(user.getLogin()))
				{
					committers.put(user.getLogin(), committers.get(user.getLogin())+1);
				}
				else
				{
					committers.put(user.getLogin(), 1);
				}
			}
			else
			{
				ignoredCount ++;
			}
		}
		
		// Print commit info.
		
		for (Map.Entry<String, Integer> entry : committers.entrySet()) 
		{
	        String key = entry.getKey();
	        Integer value = entry.getValue();
	        
	        //...
	        UserService us = new UserService();
	        User user = us.getUser(key);
	        
	        System.out.println(key + " : " + value + ". Location : " + user.getLocation() ); 
	    }
		
		System.out.println("Ignored commits = " + ignoredCount);
	}
	
	//
	// Output an authenticated user's information.
	//
	
	public static void UserService(GitHubClient client) throws IOException
	{		
		UserService us = new UserService(client);
		List<User> followerList = us.getFollowers();		
	}
	
	//
	// Output repositories information with some given languages.
	//
	
	public static void PrintReposInfo(RepositoryService rs, String[] langArray) throws IOException
	{
		int repoCountPerLang = 0;
		int repoCount = 0;
		for ( String l : langArray)
	    {	
			repoCountPerLang = 0;
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    System.out.println("Language = " + l);
		    for ( SearchRepository s : repositories)
		    {	
		    	repoCountPerLang++;
		    }
		    System.out.println("Repos Count = " + repoCountPerLang);
		    repoCount += repoCountPerLang;
	    }
		System.out.println("Total Repos Count = " + repoCount);
	}

	//
	// main function.
	//
	
	public static void main(String[] args) throws IOException {

		//
		// Basic authentication
	    //
		
		/*
		GitHubClient client = new GitHubClient();
	    client.setCredentials("******", "******");
    	*/
		
		//
	    // Get programming language information.
		//
		
		/*
	    LinkedList<String> languages = new LinkedList<String>();
	    int j = 0;
	    for(Language l : Language.values())
	    {	
	    		languages.add(l.name());
	    		System.out.print("\"" + l.name() + "\",");
	    		j++;
	    }
	    System.out.println("Number of Languages = " + j );
		System.in.read();
	    */
	    
		//
		// All the languages.
		//
		
		/*
	    String [] languages = {"ACTIONSCRIPT","ADA","APPLESCRIPT","ARC","ASP","ASSEMBLY","BATCHFILE",
	    	"BLITZMAX","BOO","BRAINFUCK","C","CSHARP","CPLUSPLUS", "COFFEESCRIPT","COLDFUSION",
	    	"COMMON_LISP","CSS","CUCUMBER","CYTHON", "D","D_OBJDUMP","DARCS_PATCH","DELPHI","EIFFEL",
	    	"EMACS_LISP","ERLANG", "FSHARP","FACTOR","FANCY","FORTRAN","GO","GROOVY","HAML","HASKELL","HAXE",
	    	"HTML","HTML_DJANGO","HTML_ERB","HTML_PHP","JAVA","JAVA_SERVER_PAGE","JAVASCRIPT","LLVM",
	    	"LUA","MAKEFILE","MATLAB", "NIMROD","NU","NUMPY","OBJDUMP","OBJECTIVE_C","OBJECTIVE_J",
	    	"OCAML","OOC","OPENCL","PARROT_INTERNAL_REPRESENTATION","PERL","PROLOG","PHP","PURE_DATA",
	    	"PYTHON","R","RACKET","RAW_TOKEN_DATA","REBOL","RUBY","SASS","SCALA","SCHEME",
	    	"SELF","SHELL","SMALLTALK","TEXTILE","VALA","VERILOG","VHDL","VIML","VISUAL_BASIC","XML",
	    	"XQUERY","XS","YAML"};
	    */
		
		//
		// Most popular languages.
		//
		
	    String [] languages = {
	    	"APPLESCRIPT","ASP","ASSEMBLY","C","CSHARP","CPLUSPLUS","COFFEESCRIPT","COLDFUSION",
	    	"COMMON_LISP","CPP_OBJDUMP","CSS","CUCUMBER","CYTHON","D","D_OBJDUMP","DARCS_PATCH",
	    	"DELPHI","ERLANG","FSHARP","FORTRAN","GO","GROOVY","HASKELL","HAXE","HTML","HTML_DJANGO",
	    	"HTML_ERB","HTML_PHP","INI","IO","IRC_LOG","JAVA","JAVA_SERVER_PAGE","JAVASCRIPT",
	    	"LUA","MAKEFILE","MATLAB","MAX_MSP","OBJDUMP","OBJECTIVE_C","OBJECTIVE_J",
	    	"PERL","PROLOG","PHP","PYTHON","R","RAW_TOKEN_DATA","REBOL","RUBY","SCALA",
	    	"SCHEME","SHELL","SMALLTALK","TEXTILE","VISUAL_BASIC","XML","XQUERY","XS","YAML"
	    };
	    
	    //
	    // Version2 service.
	    //
	    
	    RepositoryService service2 = new RepositoryService(new GitHubClient( IGitHubConstants.HOST_API_V2));
	    
	    System.out.println("Starting...");
	    
	    PrintWatchers(service2, languages);
	    
	    PrintForks(service2, languages);
	    
	    PrintContributorQuantity(service2, languages);
	    
	    PrintRepositoryInfo("boyu2011");
	    
	    PrintReposInfo(service2, languages);
	    
	    PrintCommits(service2, languages);
	   
	    PrintCommitInfo("boyu2011", "post_now");
	    PrintCommitInfo("rails", "rails");
	    PrintCommitInfo("jquery", "jquery");
	    
	    System.out.println("END OF MAIN.");
	}    
}
