//
// Main.java
//	This application is responsible for collect data from GitHub.com.
//						BoYu
//						Nov 27, 2011
//

package edu.stevens.cs.cs581.githubapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

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
		int watcherCount = 0;
		// For each language, print out the relevant repositories(100).
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    //System.out.println("Language : " + l);
		    for ( SearchRepository s : repositories)
		    {	
		    	watcherCount += s.getWatchers();
		    	//System.out.println( "Repository name : " + s.getName() + ".  \nThe number of watchers : " + s.getWatchers());
		    	System.out.println(s.getWatchers());
		    	repositoryCount++;
		    }
		    languageCount++;
	    }
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
	    System.out.println("Total watchers : " + watcherCount);
	}
	
	//
	// Output forks information.
	//
	
	public static void PrintForks(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		int forkCount = 0;
		// For each language, print out the relevant repositories(less than 100).
	    for ( String l : langArray)
	    {	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    //System.out.println("Language : " + l);
		    
		    for ( SearchRepository s : repositories)
		    {	
		    	forkCount += s.getForks();
		    	//System.out.println("Repository name : " + s.getName() + ".  \nThe number of forks : " + s.getForks());
		    	System.out.println(s.getForks());
		    	repositoryCount++;
		    }
		    languageCount++;
	    }
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
	    System.out.println("Total forks : " + forkCount);
	}
	
	//
	// Output the quantities of contributor with different repositories.
	//
	
	public static void PrintContributorQuantity(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		
		//
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
	//
	//
	
	public static void PrintContributors(RepositoryService rs, String[] langArray) throws IOException
	{
		int languageCount = 0;
		int repositoryCount = 0;
		HashMap<String, Integer> repositoryContributors = new HashMap<String, Integer>();

		// For each language, print out the relevant repositories(less than 100).
		
	    for ( String l : langArray)
	    {	
	    	System.out.println("language : " + l);
	    	
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    
		    // Loop every repository.
		    
		    //CommitService commitService = new CommitService();
		    
		    for ( SearchRepository s : repositories)
		    {	
		    	repositoryCount++;
		    	//bug??
		    	List<Contributor> contributors = rs.getContributors(s, false);
		    	System.out.println("Repository: " + s.getName() + " Contribotors: " + contributors.size());
		    }    
		    languageCount++;
	    }
	    
	    System.out.println("Total languages : " + languageCount);
	    System.out.println("Total repositories : " + repositoryCount);
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
	// Print the commit quantities with different contributers in a repository.
	//
	
	public static void PrintCommitNumberForContributors(String owner, String name) throws IOException
	{
		SearchRepository repository = new SearchRepository(owner, name);
		System.out.println("Repository name : " + repository.getName());
		CommitService commitService = new CommitService();
		List<RepositoryCommit> commits = commitService.getCommits(repository);
		System.out.println("Commit size = " + commits.size());
		
		// Committer name => The number of commits
		HashMap<String, Integer> committers = new HashMap<String, Integer>();
		//HashMap<String, User> committers2 = new HashMap<String, User>();	//
		
		int ignoredCount = 0;
		
		// Loop commits
		for(RepositoryCommit rc : commits)
		{
			//test code---------
			Commit c = rc.getCommit();
			c.getUrl();
			//------------------
			
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
	        
	        //System.out.println(/*key + " : " + */value/* + ". Location : " + user.getLocation()*/ ); 
	        if(value>100)
	        	System.out.println(key + ": " + value); 
	    }
		
		System.out.println("Ignored commits = " + ignoredCount);
		
	} // End of PrintCommitInfo()
	
	//
	// Output commit details for one repository.
	//
	
	public static void PrintRepositoryCommitDetails(
			String owner, String name, String fileName) throws IOException
	{
		SearchRepository repository = new SearchRepository(owner, name);
		System.out.println("Repository name : " + repository.getName());
		CommitService commitService = new CommitService();
		
		List<RepositoryCommit> repositoryCommits = commitService.getCommits(repository);
		System.out.println("Commit size = " + repositoryCommits.size());
		
		// Keep commit details.
		LinkedList<RepositoryCommitInfo> repositoryCommitInfoList = new LinkedList<RepositoryCommitInfo>();
		
		// UserLogin => Location
		HashMap<String, String> contributorLocations = new HashMap<String, String>();
		
		//DataService dataService = new DataService();
		//Blob blob = dataService.getBlob(repository, "250d0fccda2e81417160aaba2485f58436763a6e");
		
		//
		// Loop every commit.
		//
		
		for(RepositoryCommit repositoryCommit : repositoryCommits)
		{
			//
			// fetch commit information and store into a list.
			//
			
			/*
			DataService ds = new DataService();
			Blob blob = ds.getBlob(repository, repositoryCommit.getSha());
			repositoryCommit.getCommitter().getLocation();
			*/
			// filter message keywords
			//if(repositoryCommit.getCommit().getMessage().matches("^[F|f]ix[\\s\\S]*|[\\s\\S]*[I|i]ssue[\\s\\S]*"))
			//if(repositoryCommit.getCommit().getMessage().matches("[\\s\\S]*[B|b]ug[\\s\\S]*"))
			
			// The length of message must be less than 140.
			String message = "";
			if(repositoryCommit.getCommit().getMessage().length()>140) 
				message = repositoryCommit.getCommit().getMessage().substring(0,139);
			else
				message = repositoryCommit.getCommit().getMessage();
			
			repositoryCommitInfoList.add(
				new RepositoryCommitInfo(message,
										 repositoryCommit.getCommit().getAuthor().getName(),
										 repositoryCommit.getCommit().getAuthor().getDate(),
										 repositoryCommit.getUrl(),
										 repositoryCommit.getSha()));
			
			// Filled the contributor locations into hash map.
			if(repositoryCommit.getCommitter()!=null)
			{
				if(!contributorLocations.containsKey(repositoryCommit.getCommitter().getLogin()))
				{
					UserService userService = new UserService();
					User user = userService.getUser(repositoryCommit.getCommitter().getLogin());
					contributorLocations.put(
							repositoryCommit.getCommitter().getLogin(), 
							user.getLocation());
				}
			}
			
			/*
			//
			// get a CommitFile list
			//
			List<CommitFile> commitFiles = repositoryCommit.getFiles();
			
			RepositoryCommit rc = commitService.getCommit(repository, repositoryCommit.getSha());
			
			if(commitFiles!=null)
			{
				for(CommitFile cf : commitFiles)
				{
					System.out.println(cf.getFilename());
				}
			}
			
			DataService dataService = new DataService();
			*/
			
		}// End of for
		
		//
		// Filter the commit messages.
		//
		
		HashMap<String, Integer> messageTypes = new HashMap<String, Integer>();
		messageTypes.put("Add", 0);
		messageTypes.put("Update", 0);
		messageTypes.put("Fix", 0);
		messageTypes.put("Remove", 0);
		messageTypes.put("Merge", 0);
		
		//HashMap<String, Integer> messageTypes2 = new HashMap<String, Integer>();
		
		// Key => contributor name, 
		// Value => the number of different message types.[0:Add, 1:Update, 2:Fix, 3:Remove, 4:Merge]
		HashMap<String, int[]> contributorMessages = new HashMap<String, int[]>();
		
		for(RepositoryCommitInfo rci : repositoryCommitInfoList)
		{
			String messageFirstWord = rci.getMessage().split(" ")[0];
			
			if(messageFirstWord.matches("^[A|a][D|d][D|d].*"))
			{
				messageTypes.put("Add", messageTypes.get("Add")+1);
				
				if(contributorMessages.containsKey(rci.getAuthor()))
				{
					contributorMessages.get(rci.getAuthor())[0] += 1;
				}
				else
				{
					if(contributorMessages.get(rci.getAuthor())==null)
					{
						contributorMessages.put(rci.getAuthor(), new int[5]);
						contributorMessages.get(rci.getAuthor())[0] = 1;
					}
					else
					{
						contributorMessages.get(rci.getAuthor())[0] = 1;
					}
				}
			}
			else if(messageFirstWord.matches("^[U|u][P|p][D|d][A|a][T|t][E|e].*"))
			{
				messageTypes.put("Update", messageTypes.get("Update")+1);
				
				if(contributorMessages.containsKey(rci.getAuthor()))
				{
					contributorMessages.get(rci.getAuthor())[1] += 1;
				}
				else
				{
					if(contributorMessages.get(rci.getAuthor())==null)
					{
						contributorMessages.put(rci.getAuthor(), new int[5]);
						contributorMessages.get(rci.getAuthor())[1] = 1;
					}
					else
					{
						contributorMessages.get(rci.getAuthor())[1] = 1;
					}
				}
			}
			else if(messageFirstWord.matches("^[F|f][I|i][X|x].*"))
			{
				messageTypes.put("Fix", messageTypes.get("Fix")+1);
				
				if(contributorMessages.containsKey(rci.getAuthor()))
				{
					contributorMessages.get(rci.getAuthor())[2] += 1;
				}
				else
				{
					if(contributorMessages.get(rci.getAuthor())==null)
					{
						contributorMessages.put(rci.getAuthor(), new int[5]);
						contributorMessages.get(rci.getAuthor())[2] = 1;
					}
					else
					{
						contributorMessages.get(rci.getAuthor())[2] = 1;
					}
				}
			}
			else if(messageFirstWord.matches("^[R|r][E|e][M|m][O|o][V|v][E|e].*"))
			{
				messageTypes.put("Remove", messageTypes.get("Remove")+1);
				
				if(contributorMessages.containsKey(rci.getAuthor()))
				{
					contributorMessages.get(rci.getAuthor())[3] += 1;
				}
				else
				{
					if(contributorMessages.get(rci.getAuthor())==null)
					{
						contributorMessages.put(rci.getAuthor(), new int[5]);
						contributorMessages.get(rci.getAuthor())[3] = 1;
					}
					else
					{
						contributorMessages.get(rci.getAuthor())[3] = 1;
					}
				}
			}
			else if(messageFirstWord.matches("^[M|m][E|e][R|r][G|g][E|e].*"))
			{
				messageTypes.put("Merge", messageTypes.get("Merge")+1);
				if(contributorMessages.containsKey(rci.getAuthor()))
				{
					contributorMessages.get(rci.getAuthor())[4] += 1;
				}
				else
				{
					if(contributorMessages.get(rci.getAuthor())==null)
					{
						contributorMessages.put(rci.getAuthor(), new int[5]);
						contributorMessages.get(rci.getAuthor())[4] = 1;
					}
					else
					{
						contributorMessages.get(rci.getAuthor())[4] = 1;
					}
				}
			}
			
			/*
			if(messageTypes2.containsKey(messageFirstWord))
				messageTypes2.put(messageFirstWord, messageTypes2.get(messageFirstWord)+1);
			else
				messageTypes2.put(messageFirstWord, 1);
			*/
		}
		
		//
		// Output
		//
		
		for (Map.Entry<String, Integer> entry : messageTypes.entrySet()) 
		{
	        String key = entry.getKey();
	        Integer value = entry.getValue();
	        
	        System.out.println("Message type: " + key + " number: " + value);
		}
		
		//
		// Print contributor information.
		//
		
		System.out.println("contributor commits.");
		for (Map.Entry<String, int[]> entry : contributorMessages.entrySet()) 
		{
	        String key = entry.getKey();
	        int[] value = entry.getValue();
	        int sum = 0;
	        for(int index=0; index<value.length; index++)
	        	sum+=value[index];
	        if(sum>5)
	        {
	        	System.out.println("Contributor name: " + key);
	        	System.out.println(value[0] + "   " + value[2] + "   " + (value[1]+value[3]+value[4]) + "   ");
	        	System.out.print(value[3]+"   ");
	        	System.out.println(value[4]);
	        }
		}
		
		//
		// Output contributor locations.
		//
		/*
		System.out.println("contributor locations.");
		for(Map.Entry<String, String> entry : contributorLocations.entrySet())
		{
			//System.out.println("Author Login: "+ entry.getKey() + ". Location: " + entry.getValue());
			System.out.print("\"" + entry.getValue() + "\"," );
		}
		*/
		
		/*
		for (Map.Entry<String, Integer> entry : messageTypes2.entrySet()) 
		{
	        String key = entry.getKey();
	        Integer value = entry.getValue();
	        if(value>30)
	        	System.out.println("Message type2: " + key + " number: " + value);
		}
		*/
		//
		// Output commit info.
		//
		
		//for(RepositoryCommitInfo rci : repositoryCommitInfoList)
		//{
			//rci.OutputInfo();
		//}
		
		//
		// Write commit info into a file.
		//
		/*
		BufferedWriter out = new BufferedWriter(
			new FileWriter(fileName));
		for(RepositoryCommitInfo rci : repositoryCommitInfoList)
		{
			out.write(rci.getMessage());
			out.newLine();
			
			out.write(rci.getAuthor());
			out.newLine();
			out.write(rci.getDate().toString());
			out.newLine();
			out.write(rci.getUrl());
			out.newLine();
			out.write(rci.getSha());
			out.newLine();
			//out.write("----------------------------------------------------------");
			//out.newLine();
			
		}
		out.close();
		*/
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
		int languageCount = 0;
		for ( String l : langArray)
	    {	
			repoCountPerLang = 0;
		    List<SearchRepository> repositories = rs.searchRepositories(l,l);
		    System.out.println("Language = " + l);
		    for ( SearchRepository s : repositories)
		    {	
		    	repoCountPerLang++;
		    	CommitService commitService = new CommitService();
		    	List<RepositoryCommit> commits = commitService.getCommits(s);
		    	if(commits.size()>5000)
		    	{
		    		System.out.println("Repository name: " + s.getName());
		    		System.out.println("Commits: " + commits.size());
		    	}
		    }
		    System.out.println("Repos Count = " + repoCountPerLang);
		    repoCount += repoCountPerLang;
		    languageCount++;
	    }
		System.out.println("Total Repos Count = " + repoCount);
		System.out.println("Total language count = " + languageCount);
		
	}
	
	public static void TestCodeForRegularExpression()
	{
		String [] strings = { "Fix\n", "fix\n555", "8fix", "tfix888", "fiyx" };
	    for(String string : strings)
	    {
	    	if(string.matches("[\\s\\S]*ix[\\s\\S]*"))
	    	{
	    		System.out.println(string + " matched " + ".*[F|f].*");
	    	}
	    }
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
	    
	    System.out.println("Program Starting...");
	    
	    //
	    // Start collect data.
	    //
	    
	    //PrintWatchers(service2, languages);
	    
	    //PrintForks(service2, languages);
	    
	    //PrintContributors(service2, languages);
	    
	    //PrintContributorQuantity(service2, languages);
	    
	    //PrintRepositoryInfo("boyu2011");
	    
	    //PrintReposInfo(service2, languages);
	    
	    //PrintCommits(service2, languages);
	   
	    //PrintCommitNumberForContributors("boyu2011", "post_now");
	    //PrintCommitNumberForContributors("rails", "rails");
	    //PrintCommitNumberForContributors("jquery", "jquery");
	    //PrintCommitNumberForContributors("apache", "couchdb");
	    //PrintCommitNumberForContributors("mxcl", "homebrew");
	    //PrintCommitNumberForContributors("ruby", "ruby");
	    //PrintCommitNumberForContributors("torvalds", "linux");    
	    
	    //
	    // Commit message behavior.
	    //
	    
	    //
	    // Java 
	    //
	    
	    //PrintRepositoryCommitDetails("apache", "cassandra", 
	    //	"/Users/boyu2011/stevens/cs581/project/github-repos/cassandra-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("facebook", "facebook-android-sdk", 
	    //	"/Users/boyu2011/stevens/cs581/project/github-repos/androidsdk-repo-commits-log.txt");
	    
	    
	    //PrintRepositoryCommitDetails("KentBeck", "junit", 
		//    "/Users/boyu2011/stevens/cs581/project/github-repos/hadoopcommon-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("nathanmarz", "storm", 
		//	    "/Users/boyu2011/stevens/cs581/project/github-repos/storm-repo-commits-log.txt");
	     
	    //PrintRepositoryCommitDetails("elasticsearch", "elasticsearch", 
		//	    "/Users/boyu2011/stevens/cs581/project/github-repos/elasticsearch-repo-commits-log.txt");

	    //PrintRepositoryCommitDetails("joyent", "node", 
		//	    "/Users/boyu2011/stevens/cs581/project/github-repos/node-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("cakephp", "cakephp", 
		//	    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    //
	    // PHP
	    //
	    
	    /*
	    PrintRepositoryCommitDetails("symfony", "symfony", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("EllisLab", "CodeIgniter", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("zendframework", "zf2", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");

	    PrintRepositoryCommitDetails("sebastianbergmann", "phpunit", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("codeguy", "Slim", 
	  		"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("avalanche123", "Imagine", 
		  	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("fuel", "fuel", 
		  	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	     
	    PrintRepositoryCommitDetails("symfony", "symfony-standard", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");

	    PrintRepositoryCommitDetails("markjaquith", "WordPress", 
		   	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("symphonycms", "symphony-2", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    */
	    
	    //
	    // Top10 Popular forked repositories.
	    //
	    
	    /*
	    PrintRepositoryCommitDetails("rails", "rails", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/rails-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("mxcl", "homebrew", 
	  		"/Users/boyu2011/stevens/cs581/project/github-repos/homebrew-repo-commits-log.txt");
	  	   
	    PrintRepositoryCommitDetails("twitter", "bootstrap", 
		  	"/Users/boyu2011/stevens/cs581/project/github-repos/bootstrap-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("h5bp", "html5-boilerplate", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/html5-boilerplate-repo-commits-log.txt");
		
	    PrintRepositoryCommitDetails("robbyrussell", "oh-my-zsh", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/ho-my-zsh-repo-commits-log.txt");
		
	    PrintRepositoryCommitDetails("joyent", "node", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/node-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("jquery", "jquery", 
		   	"/Users/boyu2011/stevens/cs581/project/github-repos/jquery-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("diaspora", "diaspora", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/diaspora-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("jquery", "jquery-ui", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/jquery-ui-repo-commits-log.txt");
	       
	    PrintRepositoryCommitDetails("git", "git", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/git-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("facebook", "three20", 
		    	"/Users/boyu2011/stevens/cs581/project/github-repos/three20-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("django", "django", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/django-repo-commits-log.txt");
	   
	    PrintRepositoryCommitDetails("cakephp", "cakephp", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("postgres", "postgres", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/postgres-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("apache", "httpd", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/httpd-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("ruby", "ruby", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/ruby-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("phpbb", "phpbb", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/phpbb-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("facebook", "tornado", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/tornado-repo-commits-log.txt");
	   
		PrintRepositoryCommitDetails("wayneeseguin", "rvm", 
				"/Users/boyu2011/stevens/cs581/project/github-repos/rvm-repo-commits-log.txt");
		
		PrintRepositoryCommitDetails("mongodb", "mongo", 
				"/Users/boyu2011/stevens/cs581/project/github-repos/mongo-repo-commits-log.txt");
		*/
		
	    //
	    // Top 10 Watched repositories.
	    //
	    
	    PrintRepositoryCommitDetails("rails", "rails", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/rails-repo-commits-log.txt");
	    /*
	    PrintRepositoryCommitDetails("joyent", "node", 
			    "/Users/boyu2011/stevens/cs581/project/github-repos/node-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("twitter", "bootstrap", 
			  	"/Users/boyu2011/stevens/cs581/project/github-repos/bootstrap-repo-commits-log.txt");
	
	    PrintRepositoryCommitDetails("jquery", "jquery", 
			   	"/Users/boyu2011/stevens/cs581/project/github-repos/jquery-repo-commits-log.txt");
		    
	    PrintRepositoryCommitDetails("h5bp", "html5-boilerplate", 
	    	"/Users/boyu2011/stevens/cs581/project/github-repos/html5-boilerplate-repo-commits-log.txt");
		
	    PrintRepositoryCommitDetails("mxcl", "homebrew", 
		  	"/Users/boyu2011/stevens/cs581/project/github-repos/homebrew-repo-commits-log.txt");
		
	    PrintRepositoryCommitDetails("diaspora", "diaspora", 
		    	"/Users/boyu2011/stevens/cs581/project/github-repos/diaspora-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("facebook", "three20", 
		    	"/Users/boyu2011/stevens/cs581/project/github-repos/three20-repo-commits-log.txt");
	    
	    PrintRepositoryCommitDetails("plataformatec", "devise", 
		    "/Users/boyu2011/stevens/cs581/project/github-repos/devise-repo-commits-log.txt");
	    */
	    
	    //PrintRepositoryCommitDetails("KentBeck", "junit", 
	    //	"/Users/boyu2011/stevens/cs581/project/github-repos/junit-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("torvalds", "linux", 
		//    "/Users/boyu2011/stevens/cs581/project/github-repos/linux-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("django", "django", 
	    //	"/Users/boyu2011/stevens/cs581/project/github-repos/django-repo-commits-log.txt");
	   
	    //PrintRepositoryCommitDetails("cakephp", "cakephp", 
	    //	"/Users/boyu2011/stevens/cs581/project/github-repos/cakephp-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("postgres", "postgres", 
		//    	"/Users/boyu2011/stevens/cs581/project/github-repos/postgres-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("boyu2011", "post_now", 
	    //  	"/Users/boyu2011/stevens/cs581/project/github-repos/post_now-repo-commits-log.txt");
	    
	    //PrintRepositoryCommitDetails("eclipse", "egit-github", 
	    //  	"/Users/boyu2011/stevens/cs581/project/github-repos/egit-github-repo-commits-log.txt");
	    
	    //PrintReposInfo(service2, languages);
	    
	    /*
	    String str = "AdD file 1.";
	    //String [] strList = str.split(" ");
	    //for(String s : strList)
	    //	System.out.println(strList[0]);
	    if(str.matches("^[A|a][D|d][D|d].*"))
	    	System.out.println("match.");
		*/
	    
	    System.out.println("Program end.");
	}    
}
