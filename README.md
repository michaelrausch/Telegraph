# Telegraph
[![Build Status](https://travis-ci.org/michaelrausch/Telegraph.svg?branch=master)](https://travis-ci.org/michaelrausch/Telegraph)
[![Maintainability](https://api.codeclimate.com/v1/badges/01b62ede33372d1c44f7/maintainability)](https://codeclimate.com/github/michaelrausch/Telegraph/maintainability)
[![codecov](https://codecov.io/gh/michaelrausch/Telegraph/branch/master/graph/badge.svg)](https://codecov.io/gh/michaelrausch/Telegraph)

Telegraph is a simple contact form API.

### Key Features
- Mailjet & IFTTT integrations.
- Support for multiple websites with different recipients.

### Setup
**Note: This is still a WIP and probably shouldn't be used in production yet.**

**1. Clone this repo**

``
    git clone https://github.com/michaelrausch/Telegraph.git
``

**2. Rename config.example.yaml to config.yaml**

**3. Get an API keys for Mailjet and/or IFTTT**

**4. Paste your API keys into config.yaml and fill out the rest of your information**
- Add a cooldown (in seconds) to requests by adjusting the cooldown on rateLimiter

**5. Add your recipients to clientList**
Note: In this current version there is some weird terminology (like publickey) that will be replaced in an upcoming version. Publickey is the value used in realm in your request.

**6. Build the jar**

``
./gradlew fatJar
``

**7. Send a request**
```
{
    name: "Michael",
    email: "michael@rausch.nz",
    message: "This is a message",
    email_h_v: "", // This MUST be blank, is used to deter bots
    realm: "TEST" // This must equal the publickey for a client set up above
}
```

### Telemetry
Basic telemetry can be accessed by opening /telemetry. 

### Adding message handlers
1. Add a new package under au.telegraph.messaging
2. Create class called YourMessageHandler that implements MessageHandler, and optionally YourConfig which should be a POJO if you want to add configuration.
4. Implement the send(Message, ClientConfiguration) method, throw a MessageSendException if this fails
- Message will contain the message sent from the client, and the ClientConfiguration will contain the recipient information
6. Add your config POJO as a private var in AppConfig, and add a getter/setter.
7. Add your handler using contactPostHandler.addMessageHandler(MessageHandler) in Server.java.

### Adding Endpoints
1. Create a handler called YourHandler that implements HttpHandler
2. Set up your handler and call server.get(path, handler) or server.post(...,...) in Server.java
3. Implement Handle(HttpContext ctx)

This http server is based on Javalin but has some extended functionality. 
