package com.github.helper;

public class Constants {

	public static Boolean gitflag;
	public static Boolean flagAuthonticate;
	public static Boolean flagCommit;
	
	
	//URL
	public static final String BRANCH="sha";
	public static final String strLoginUserName="/users/auth_token.json";
	public static final String strRepository="/users/repository.json";
	public static final String strBranch="/users/branch.json";
	public static final String strCommits="/users/commit.json";
	public static final String strOrganisation="/users/organization.json";
	public static final String strOrganisationRepository="/users/org_repository.json";
	public static final String strOrganisationBranch="/users/org_branch.json";
	public static final String strOrganisationCommits="/users/org_commit.json";
	
	//Database Tables
	public static final String RepositoryTableName="Repository";
	public static final String CommitsTableName="Commits";
	public static final String BranchTableName="Branch";
	public static final String OrganisationName="Organisation";
	public static final String OrgRepositoryTableName="OrganisationRepository";
	
	//Shared Preferences
	public static final String strFileName = "githublogin";
	public static final String AUTH_KEY = "auth_token";
	public static final String LOGIN_USERNAME = "LOGIN_USERNAME";
	
}
