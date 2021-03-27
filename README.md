
# SmartThings and OpenGarage

This repository provides [SmartThings](https://www.smartthings.com/)
Device Handlers for [OpenGarage](https://opengarage.io),
and two SmartApps to help you automate your garage door.

## Device Handler (via Blynk)

[open-garage-blynk.groovy](open-garage-blynk.groovy)

First you need to setup your [OpenGarage](https://opengarage.io) normally,
including the [Blynk](https://blynk.cc) integration steps.

Then, login to your
[SmartThings developer account](https://graph.api.smartthings.com/),
create a new Device Handler, and import the code.

After that, you can create a new Device using the Device Handler you just
created.
On your SmartThings app, choose "Add a Thing" and the device you just created
from developer IDE will appear. Confirm it.

There are 3 required preferences you must set before you can really use the
device. You can set them up on either the app or IDE:

### Blynk Auth Token

This is the auth token Blynk sent you during setup.

### Blynk URL prefix

As at the time of writing blynk doesn't provide a proper HTTPS API so you have
to use the HTTP version of `http://blynk-cloud.com`.
