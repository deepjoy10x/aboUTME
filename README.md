
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

[open-garage-direct.groovy](open-garage-direct.groovy)

*NOTE: I highly recommend upgrading firmware to version 1.10 or up to use this
device handler.*

First you need to setup your [OpenGarage](https://opengarage.io) normally,
the [Blynk](https://blynk.cc) integration steps are optional (not needed for
this integration).

Then you need to make your local OpenGarage web UI available to the internet.
This usually can be done in two ways:

1. On your router, map your OpenGarage's IP and 80 port to an external port.
   Please note that this way the communication is not encrypted, your garage
	 controller web UI is exposed to the internet without protection,
	 so it's not recommended.

2. In your home, run a 24/7 machine with a reverse proxy (e.g.
   [nginx](https://www.nginx.com/)). You can configure HTTPS and/or HTTP
   authentication this way, but you need a 24/7 machine to make it work.

After you exposed your OpenGarage web UI to the internet, login to your
[SmartThings developer account](https://graph.api.smartthings.com/),
create a new Device Handler, and import the code.

After that, you can create a new Device using the Device Handler you just
created.
On your SmartThings app, choose "Add a Thing" and the device you just created
from developer IDE will appear. Confirm it.

There are few required preferences you must set before you can really use the
device. You can set them up on either the app or IDE:

### Device key

This is the password you used on OpenGarage web UI to do actions and change
options. The factory default is `opendoor`.

### HTTP(s) prefix

This is the way you exposed the web UI to the internet.
If you just mapped the port on your router,
this should be something like `http://yourip:port`.
If you setup a reverse proxy with HTTPS,
this should be something like `https://yourdomain`.

### HTTP Authentication header

This is only needed if you setup a reverse proxy and setup HTTP authentication.
If you just use simple username/password authentication,
it should be something like `Basic dXNlcm5hbWU6cGFzc3dvcmQ=`,
while the part after `Basic ` is the base64 encoded username and password.
You can get that in Python easily:

```python
>>> from base64 import b64encode
>>> b64encode('username:password')
'dXNlcm5hbWU6cGFzc3dvcmQ='
```

If you setup other HTTP authentication methods,
e.g. Digest, refer to RFC to see how to get the `Authentication` HTTP header.

### State Refresh Rate

We couldn't push the garage door state to SmartThings right now so the update of
the state rely on refresh requests initiated from SmartThings to Blynk server.
This preference controls how frequent we send those refresh requests.

If you don't have a Contact Sensor setup with your OpenGarage,
you would need a more frequent refresh rate to keep the state up-to-date.

## Garage Door Helper SmartApp

[garage-door-helper.groovy](garage-door-helper.groovy)

This is a SmartApp to help you get more up-to-date state of your OpenGarage
(or any other unofficially supported garage device that doesn't needs pull)
more efficiently.

First you'll need a separated Contact Sensor installed on your garage door.

Then, login to your
[SmartThings developer account](https://graph.api.smartthings.com/),
create a new SmartApp, and import the code.

After that, on your SmartThings app you can add the SmartApp,
select your OpenGarage as the garage door and the contact sensor.

The idea behind this SmartApp is that whenever the contact sensor's state
changes, it will ask the garage door to refresh its state,
so your garage door's state will update in seconds instead of minutes.

You can also get push/sms notifications when your garage door device handler has
HTTP request failures.

## Presence and Garage Door SmartApp

[presence-and-garage-door.groovy](presence-and-garage-door.groovy)

This is a SmartApp to automate your garage door with a presence sensor
(probably in your car).
The basic idea is that when the presence sensor's state changed to not present,
that means your car is leaving so it closes the garage door;
When the presence sensor's state changed to present,
that means your car is arriving so it opens the garage door.

One tricky thing is that presence sensor may report false state changes.
When the presence sensor loses connection to the hub,
the state will change to not present and the SmartApp will try to close your
garage door.
This is not a big deal,
but when it restore the connection to the hub,
the state will change to present and the SmartApp will try to open your garage
door, and that's a thing to avoid.

To resolve this false report problem,
there's an option called "Real close threshold" in this SmartApp.
When set, for example to 300 seconds (5 minutes),
when your presence sensor's state changed to not present,
the SmartApp will check the door's current state,
and if it's already closed, check what's the time it actually closed.
If the actual close time is more than the threshold,
the SmartApp assumes that this is a false report,
and won't really open the garage door when its state next change to present.

Refer to the above section about how to install this SmartApp.