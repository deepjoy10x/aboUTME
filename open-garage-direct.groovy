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
				backgroundColor: "#afafaf",
			)

			state(
				"closed",
				label: "Closed",
				icon: "st.doors.garage.garage-closed",
				action: "open",
				backgroundColor: "#00a0dc",
			)
			state(
				"closing",
				label: "Closing",
				icon: "st.doors.garage.garage-closing",
				backgroundColor: "#ffdd00",
			)
			state(
				"open",
				label: "Open",
				icon: "st.doors.garage.garage-open",
				action: "close",
				backgroundColor: "#ffdd00",
			)
			state(
				"opening",
				label: "Opening",
				icon: "st.doors.garage.garage-opening",
				backgroundColor: "#ffdd00",
			)
		}

		standardTile("sCar", "device.carStatus") {
			state(
				"unknown",
				label: "Car: Unknown",
				backgroundColor: "#afafaf",
			)
			state(
				"present",
				label: "Car: Present",
				backgroundColor: "#00a0dc",
			)
			state(
				"absent",
				label: "Car: Absent",
				backgroundColor: "#ffdd00",
			)
		}

		standardTile("sRefresh", "device.door", inactiveLabel: false, decoration: "flat") {
			state(
				"default",
				label: "",
				action: "refresh.refresh",
				icon: "st.secondary.refresh",
			)
		}

		standardTile("sContact", "device.contact") {
			state(
				"open",
				label: "Open",
				icon: "st.contact.contact.open",
				backgroundColor: "#ffa81e",
			)
			state(
				"closed",
				label: "Closed",
				icon: "st.contact.contact.closed",
				backgroundColor: "#79b821",
			)
		}

		main(["sDoorToggle"])
		details(["sDoorToggle", "sCar", "sRefresh"])
	}
}


def installed() {
	log.debug "Installing OpenGarage Door"

	checkRefresh()
	refresh()
}

def updated() {
	log.debug "Updating OpenGarage Door"

	checkRefresh()
	refresh()
}

def parse(description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'contact' attribute
	// TODO: handle 'switch' attribute
}

def push() {
	log.debug "Executing 'push'"

	def initStatus
	getDoorStatus() { status -> initStatus = status }

	def target
	if (initStatus == "closed" || initStatus == "closing") {
		log.debug "Door is in a closed status, opening"
		target = "open"
	} else if (initStatus == "open" || initStatus == "opening") {
		log.debug "Door is in an open status, closing"
		target = "closed"
	} else if (initStatus == "unknown") {
		log.debug "Door is in an unknown state, doing nothing"
		return
	}

	flipDoor()
	refreshUntil(target)
}

def poll() {
	log.debug "OpenGarage Polling"
	refresh()
}

def refresh() {
	doRefresh()
}

def checkRefresh() {
	switch (refresh_rate.toLowerCase()) {
	case "disabled":
		unschedule(doRefresh)
		doRefresh()
		break
	case "every 5 minutes":
		runEvery5Minutes(doRefresh)
		break
	case "every 10 minutes":
		runEvery10Minutes(doRefresh)
		break
	case "every 15 minutes":
		runEvery15Minutes(doRefresh)
		break
	case "every 30 minutes":
		runEvery30Minutes(doRefresh)
		break
	case "every hour":
		runEvery1Hour(doRefresh)
		break
	case "every minute":
	default:
		runEvery1Minute(doRefresh)
		break
	}
}

def doRefresh() {
	log.debug "Refreshing Door State"

	getDoorStatus() { status ->
		setDoorState(status)
		log.debug "Door Status: $status"
	}
}

def afterForceRefresh(status, startTime) {
	def time = (now() - startTime) / 1000
	log.debug "Final Door Status: $status, took $time seconds"
	setDoorState(status)
}

def forceRefreshUntil(data) {
	def timestamp = now()
	def target = data.targetStatus
	log.debug "forceRefreshUntil: ${new Date()}, timestamp: $timestamp, stops at ${data.stopAt}, target status: $target"
	def scheduleNext = true
	if (timestamp >= data.stopAt) {
		log.debug "Stopping refreshing..."
		getDoorStatus() { status ->
			afterForceRefresh(status, data.startTime)
		}
		scheduleNext = false
		return
	}
	getDoorStatus() { status ->
		log.debug "forceRefreshUntil: get door status: $status"
		if (status == target) {
			log.debug "Got target st