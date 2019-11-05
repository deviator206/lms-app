package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PushNotificationService implements IPushNotificationService {

	//final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	//final static private String SERVER_KEY = "AAAAn20fA3Q:APA91bHtl_CtrbWDCErXTg2t5sQI-rQFQoZrLkZWuWV6MoGyCFFFOMm6s77C7PirEaDDZ3xazTXen_VsXWZeU6ZTu9bvJYSCfYsvBXCmD7v5AdQin4gBSvdqplvc2usZtUOvt1f7T_b1";


	@Value("${app.pushNotificationServerUrl}")
	private String pushNotificationServerUrl;
	
	@Value("${app.pushNotificationServerKey}")
	private String pushNotificationServerKey;
	/**
	 * 
	 * Method to send push notification to Android FireBased Cloud messaging Server.
	 * 
	 * @param tokenId
	 *            Generated and provided from Android Client Developer
	 * @param serverKey
	 *            Key which is Generated in FCM Server
	 * @param message
	 *            which contains actual information.
	 * 
	 */
	@Override
	public void sendNotification(String tokenId, String message) {

		try {
			// Create URL instance.
			URL url = new URL(pushNotificationServerUrl);
			// create connection.
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// set method as POST or GET
			conn.setRequestMethod("POST");
			// pass FCM server key
			conn.setRequestProperty("Authorization", "key=" + pushNotificationServerKey);
			// Specify Message Format
			conn.setRequestProperty("Content-Type", "application/json");
			// Create JSON Object & pass value
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode infoJson1 = mapper.createObjectNode();
			//JSONObject infoJson = new JSONObject();

			
			infoJson1.put("title", "Alankit");
			infoJson1.put("body", message);
			//infoJson.put("title", "Alankit");
			//infoJson.put("body", message);

			ObjectNode json1 = mapper.createObjectNode();
			json1.put("to", tokenId.trim());
			json1.put("notification", infoJson1);
			
			//JSONObject json = new JSONObject();
			//json.put("to", tokenId.trim());
			//json.put("notification", infoJson);

			//System.out.println("json :" + json.toString());
			//System.out.println("infoJson :" + infoJson.toString());
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			//wr.write(json.toString());
			wr.write(json1.toString());
			wr.flush();
			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			if (status != 0) {

				if (status == 200) {
					// SUCCESS message
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
				} else if (status == 401) {
					// client side error
					System.out.println("Notification Response : TokenId : " + tokenId + " Error occurred :");
				} else if (status == 501) {
					// server side error
					System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + tokenId);
				} else if (status == 503) {
					// server side error
					System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + tokenId);
				}
			}
		} catch (MalformedURLException mlfexception) {
			// Prototcal Error
			System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		} catch (Exception mlfexception) {
			// URL problem
			System.out.println(
					"Reading URL, Error occurred while sending push	Notification!.." + mlfexception.getMessage());
		}

	}

	@Override
	public void sendNotificationMulti(List<String> putIds2, String message) {
		try {
			// Create URL instance.
			URL url = new URL(pushNotificationServerUrl);
			// create connection.
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// set method as POST or GET
			conn.setRequestMethod("POST");
			// pass FCM server key
			conn.setRequestProperty("Authorization", "key=" + pushNotificationServerKey);
			// Specify Message Format
			conn.setRequestProperty("Content-Type", "application/json");
			// Create JSON Object & pass value

			ObjectMapper mapper = new ObjectMapper();
			//JSONArray regId = null;
			ObjectNode objData1 = null;//mapper.createObjectNode();
			//JSONObject objData = null;
			ObjectNode data1 = null;//mapper.createObjectNode();
			//JSONObject data = null;
			ObjectNode notif1 = null;//mapper.createObjectNode();
			//JSONObject notif = null;

			//regId = new JSONArray();
			//for (int i = 0; i < putIds2.size(); i++) {
				//regId.put(putIds2.get(i));
			//}
			
			
			ArrayNode arrayNode = mapper.createArrayNode();
			for (int i = 0; i < putIds2.size(); i++) {
				arrayNode.add(putIds2.get(i));
			}
			data1 = mapper.createObjectNode();
			data1.put("message", message);
			//data = new JSONObject();
			//data.put("message", message);
			
			notif1 = mapper.createObjectNode();
			notif1.put("title", "Alankit Universe");
			notif1.put("text", message);
			
			//notif = new JSONObject();
			//notif.put("title", "Alankit Universe");
			//notif.put("text", message);

			objData1 = mapper.createObjectNode();
			//objData = new JSONObject();
			//objData.put("registration_ids", regId);
			//objData.put("registration_ids", arrayNode);
			//objData.put("data", data);
			//objData.put("notification", notif);
			objData1.put("registration_ids", arrayNode);
			objData1.put("data", data1);
			objData1.put("notification", notif1);
			//System.out.println("!_@rj@_group_PASS:>" + objData.toString());

			//System.out.println("json :" + objData.toString());
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(objData1.toString());
			wr.flush();
			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			if (status != 0) {

				if (status == 200) {
					// SUCCESS message
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
				} else if (status == 401) {
					// client side error
					System.out.println("Notification Response : TokenId : " + putIds2 + " Error occurred :");
				} else if (status == 501) {
					// server side error
					System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + putIds2);
				} else if (status == 503) {
					// server side error
					System.out.println("Notification Response : FCM Service is Unavailable TokenId : " + putIds2);
				}
			}
		} catch (MalformedURLException mlfexception) {
			// Prototcal Error
			System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		} catch (IOException mlfexception) {
			// URL problem
			System.out.println(
					"Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		} catch (Exception exception) {
			// General Error or exception.
			System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
		}

	}
}
