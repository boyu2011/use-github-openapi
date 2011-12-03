//
// Author: BoYu
//
package edu.stevens.cs.cs581.githubapp;

public class RepositoryCommitInfo {
	
	public String message;
	public String author;
	public java.util.Date date;
	public String url;
	public String sha;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}
	
	public RepositoryCommitInfo(String message, String author, java.util.Date date, String url, String sha)
	{
		this.message = message;
		this.author = author;
		this.date = date;
		this.url = url;
		this.sha = sha;
	}
	
	public void OutputInfo()
	{
		System.out.println(this.getMessage());
		//System.out.println("Author: " + this.getAuthor());
		//System.out.println("Date:" + this.getDate());
		//System.out.println("URL: " + this.getUrl());
		//System.out.println("SHA: " + this.getSha());
		System.out.println("-------------------------------------------------------------");
	}
}
