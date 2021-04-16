
/**
 *  SmartThings device handler for OpenGarage via Blynk.
 *
 *  Copyright 2018 Yuxuan "fishy" Wang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *  Some of the code are from https://github.com/adamheinmiller/ST_MyQ by Adam Heinmiller,
 *  used under Apache License.
 *
 */

metadata {
	definition(name: "OpenGarage-Blynk", namespace: "fishy", author: "Yuxuan Wang") {
		capability "Contact Sensor"
		capability "Door Control"
		capability "Garage Door Control"
		capability "Momentary"
		capability "Polling"
		capability "Refresh"
		capability "Switch"

		attribute "lastHttpStatus", "enum", ["succeeded", "failed"]
	}

	preferences {
		input(
			name: "auth_token",
			type: "text",
			title: "Blynk Auth Token",
			description: "Your Auth Token from Blynk",
			required: true,
		)

		input(
			name: "blynk_prefix",
			type: "text",
			title: "Blynk URL prefix",
			description: "Example: \"http://blynk-cloud.com\" or \"https://blynk-proxy.appspot.com\"",
			required: true,
		)

		input(
			name: "refresh_rate",
			type: "enum",
			title: "State Refresh Rate",
			options: [
				"Every minute",
				"Every 5 minutes",
				"Every 10 minutes",
				"Every 15 minutes",
				"Every 30 minutes",
				"Every hour",
				"Disabled",
			],
			description: "Only disable it if you have another contact sensor hooked on the garage door",
			required: true,
		)
	}

	tiles {
		standardTile("sDoorToggle", "device.door", width: 1, height: 1, canChangeIcon: false) {
			state(
				"default",
				label: "",
			)

			state(
				"unknown",
				label: "Unknown",