
/**
 *  Garage Door Helper.
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
 */

definition(
	name: "Garage Door Helper",
	namespace: "fishy",
	author: "Yuxuan Wang",
	description: "Use contact sensor to better refresh garage door state",
	category: "Safety & Security",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png",
)


preferences {
	section("Garage Door") {
		input(
			"door",
			"capability.refresh",
			title: "Pick a garage door",
			multiple: false,
		)
	}
	section("Contact Sensor") {
		input(
			"contact",
			"capability.contactSensor",
			title: "Pick a contact sensor",
			multiple: false,
			required: false,
		)
		input(
			"refresh_rate",
			"number",
			title: "refresh for every N seconds (default 5)",
			required: false,
		)
		input(
			"max_refresh",
			"number",
			title: "stop refreshing after N minutes (default 2)",
			required: false,
		)
	}
	section("Notification") {
		input(