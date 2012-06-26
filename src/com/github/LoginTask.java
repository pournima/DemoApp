//package com.github;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.github.helper.AppStatus;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class LoginTask extends AsyncTask<String, Void, String> {
//	private LoginInActivity context;
//	AppStatus mAppStatus;
//	private String strPassword;
//
//	public String getStrPassword() {
//		return strPassword;
//	}
//
//	public void setStrPassword(String strPassword) {
//		this.strPassword = strPassword;
//	}
//
//	public LoginTask(LoginInActivity context, String password) {
//		// TODO Auto-generated constructor stub
//		this.context = context;
//		this.strPassword = password;
//		mAppStatus = new AppStatus();
//
//	}
//
//	@Override
//	protected String doInBackground(String... emailId) {
//		// TODO Auto-generated method stub
//		// showLoading(true, "Loading", "In Process please wait...");
//		String strJsonReponse = null;
//		try {
//			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//			params.add(new BasicNameValuePair("email", emailId[0]));
//			params.add(new BasicNameValuePair("password", strPassword));
//			strJsonReponse = RestClient.getInstance(context).doApiCall(
//					Constants.strSignIn, "POST", params);
//			Log.i("Authenticate UserName and Password Response:....",
//					String.valueOf(strJsonReponse));
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return strJsonReponse;
//	}
//
//	@Override
//	protected void onPostExecute(String strJsonReponse) {
//
//		try {
//			JSONObject mJsonObject = new JSONObject(strJsonReponse);
//			Log.i("Status.........", String.valueOf(strJsonReponse));
//			String strStatus = mJsonObject.getString("success");
//			Log.i("Status.........", String.valueOf(strStatus));
//			if (strStatus.equals("true")) {
//				String strAuthToken = mJsonObject.getString("auth_token");
//				Log.i("Auth Token:.....", strAuthToken);
//
//				String strUserLoginId = mJsonObject.getString("id");
//				Log.i("UserLoginId:.....", strUserLoginId);
//				/* --------------------START--------------------- */
//				/* Stored AUTH Token into shared preferences */
//				mAppStatus = AppStatus.getInstance(context);
//				boolean bResponse = mAppStatus.saveAuthKey(Constants.AUTH_KEY,
//						strAuthToken);
//				
//				mAppStatus.saveAuthKey(Constants.LOGIN_USERID,strUserLoginId);
//				
//				if (bResponse == true)
//					Log.i("AUTH TOKEN", "save successfully....!!");
//				else
//					Log.i("AUTH TOKEN", "NOT save ......!!");
//				/*---------------------END------------------------- */
//
//				Intent intent = new Intent(context, POLS2Activity.class); // switch
//																			// to
//																			// home
//																			// page
//				// showDialog(0); // calling PROGREESS DIALOG
//
//				context.startActivity(intent);
//				context.finish();
//				// edTxtPassword.setText("");
//			} else {
//				Log.i("Status", "Invalid Password: Fail");
//			//	SignInActivity mSignInActivity = new SignInActivity();
//				context.warningDialogBox("Invalid Email id or Password.!");
//				context.dismissDialog(0);  //To disable progress bar
//				//	mSignInActivity.invalidUser();
//				// showLoading(false, "", "");
//				// message("Invalid Email id or Password.!");
//				// warningDialogBox("Invalid Email id or Password.!");
//				// edTxtPassword.setText("");
//				return;
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
