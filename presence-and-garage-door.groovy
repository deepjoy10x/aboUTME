/**
 *  Presence and Garage Door.
 *
 *  Copyright 2016 Yuxuan Wang
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
	name: "Presence and Garage Door",
	namespace: "fishy",
	author: "Yuxuan Wang",
	description: "Use presence sensor to automate Garage Door",
	category: "Safety & Security",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
)


preferences {
	section("Garage Door") {
		input(
			"door",
			"capability.garageDoorControl",
			title: "Which One?",
		)
	}
	section("Presence Sensors") {
		input(
			"cars",
			"capability.presenceSensor",
			title: "Cars?",
			multiple: true,
		)
	}
	section("Real close threshold") {
		input(
			"seconds",
			"number",
			title: "Seconds?",
			required: false,
		)
		input(
			"phone",
			"text",
