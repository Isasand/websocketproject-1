/*
 * WebSocketClient.ino
 *
 *  Created on: 24.05.2015
 *
 */

#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <WebSocketsClient.h>

#include <Hash.h>
#include <Adafruit_Sensor.h>
#include "DHT.h"

#define DHTPIN 2     // what digital pin the DHT22 is conected to
#define DHTTYPE DHT22   // there are multiple kinds of DHT sensors

DHT dht(DHTPIN, DHTTYPE);

ESP8266WiFiMulti WiFiMulti;
WebSocketsClient webSocket;

#define USE_SERIAL Serial

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {

	switch(type) {
		case WStype_DISCONNECTED:
			USE_SERIAL.printf("[WSc] Disconnected!\n");
			break;
		case WStype_CONNECTED: {
			USE_SERIAL.printf("[WSc] Connected to url: %s\n", payload);
       
		
		}
			break;
		case WStype_TEXT:
			USE_SERIAL.printf("[WSc] get text: %s\n", payload);

			// send message to server
			 //webSocket.sendTXT("{\"action\":\"updatedata\",\"type\":\"Tempsensor\",\"id\":\"1\",\"data\" : \"25.3\"}");
			break;
		case WStype_BIN:
			USE_SERIAL.printf("[WSc] get binary length: %u\n", length);
			hexdump(payload, length);

			// send data to server
			// webSocket.sendBIN(payload, length);
			break;
	}

}

void setup() {
	// USE_SERIAL.begin(921600);
	USE_SERIAL.begin(115200);

	//Serial.setDebugOutput(true);
	USE_SERIAL.setDebugOutput(true);

	USE_SERIAL.println();
	USE_SERIAL.println();
	USE_SERIAL.println();

	for(uint8_t t = 4; t > 0; t--) {
		USE_SERIAL.printf("[SETUP] BOOT WAIT %d...\n", t);
		USE_SERIAL.flush();
		delay(1000);
	}
   WiFi.begin("Poppekorn", "Jason2009");  
  // WiFi.begin("Bryant", "Jason2009");  

  while (WiFi.status() != WL_CONNECTED) { // Wait for the Wi-Fi to connect
    delay(1000);;
	}

	// server address, port and URL
	webSocket.begin("192.168.43.252", 8080, "/WebSocketProject/actions");
  //webSocket.begin("192.168.0.108", 8080, "/WebSocketProject/actions");
 

	// event handler
	webSocket.onEvent(webSocketEvent);

	// try ever 5000 again if connection has failed
	webSocket.setReconnectInterval(5000);

}

void loop() {
	webSocket.loop();
  
  float temp = dht.readTemperature();
  String t = (String)temp;
  webSocket.sendTXT("{\"action\":\"updatedata\",\"type\":\"Tempsensor\",\"id\":\"1\",\"data\" : \""+t+"\"}");
  USE_SERIAL.println("{\"action\":\"updatedata\",\"type\":\"Tempsensor\",\"id\":\"1\",\"data\" : \""+t+"\"}");
  delay(2000);
 
}
