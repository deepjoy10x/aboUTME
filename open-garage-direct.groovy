/**
 *  SmartThings device handler for OpenGarage.
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
	definition(name: "OpenGarage-Direct", namespace: "fishy", author: "Yuxuan Wang") {
		capability "Contact Sensor"
		capability "Door Control"
		capability "Garage Door Control"
		capability "Momentary"
		capability "Polling"
		capability "Refresh"
		capability "Switch"

		capability "Actuator"
		capability "Sensor"

		attribute "lastHttpStatus", "enum", ["succeeded", "failed"]
		attribute "carStatus", "enum", ["present", "absent", "unknown"]
		attribute "distance", "number"
		attribute "readCount", "number"
	}

	preferences {
		input(
			name: "device_key",
			type: "text",
			title: "Device key",
			description: "Factory default is \"opendoor\"",
			required: true,
		)

		input(
			name: "http_prefix",
			type: "text",
			title: "HTTP(s) prefix",
			description: "Example: \"http://yourip:8080\" or \"https://opengarage.myhome.com\"",
			required: true,
		)

		input(
			name: "http_auth",
			type: "text",
			title: "HTTP Authorization header",
			description: "Optional. Example: \"Basic dXNlcm5hbWU6cGFzc3dvcmQ=\"",
			required: false,
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
		standardTile("sDoorToggle", "device.door", width: 2, height: 2, canChangeIcon: false) {
			state(
				"unknown",
				label: "Unknown",
				icon: "st.unknown.unknown.unknown",
				action: "refresh.refresh",
				backgroundColor: "#afaf