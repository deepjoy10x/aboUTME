
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

If you would like to use HTTPS for better security,
you'll need a special HTTPS proxy.
Refer to my [blynk-proxy](https://github.com/fishy/blynk-proxy) project for more
details.
You could setup your own proxy using blynk-proxy,
or if after understanding the
[risks](https://github.com/fishy/blynk-proxy/blob/master/README.md#should-i-use-your-app-engine-app)
you still don't mind, you could use `https://blynk-proxy.appspot.com`.

### State Refresh Rate

We couldn't push the garage door state to SmartThings right now so the update of
the state rely on refresh requests initiated from SmartThings to Blynk server.
This preference controls how frequent we send those refresh requests.

If you don't have a Contact Sensor setup with your OpenGarage,
you would need a more frequent refresh rate to keep the state up-to-date.

## Device Handler (Direct)
