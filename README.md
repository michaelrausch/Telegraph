# Telegraph
[![Build Status](https://travis-ci.org/michaelrausch/Telegraph.svg?branch=master)](https://travis-ci.org/michaelrausch/Telegraph)
[![Maintainability](https://api.codeclimate.com/v1/badges/01b62ede33372d1c44f7/maintainability)](https://codeclimate.com/github/michaelrausch/Telegraph/maintainability)
[![codecov](https://codecov.io/gh/michaelrausch/Telegraph/branch/master/graph/badge.svg)](https://codecov.io/gh/michaelrausch/Telegraph)

Telegraph is a simple, yet extendable contact form API designed for web developers.

### Key Features
- Mailjet & IFTTT integrations.
- Support for multiple websites with different recipients.
- Extendable API

### Setup

**1. Clone this repo**

``
    git clone https://github.com/michaelrausch/Telegraph.git
``

**2. Rename config.example.yaml to config.yaml**

**3. Get an API keys for Mailjet and/or IFTTT**

**4. Paste your API keys into config.yaml and fill out the rest of your information**

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

Create a class that implements MessageHandler and add this to the contactFormHandler in Server.java. You will recieve a Message containing the contact form submission, and a ClientConfiguration containing the recipient details. Here's a simple console example: 

```
package au.telegraph.messaging;

import au.telegraph.configuration.models.ClientConfiguration;

/**
 * Prints messages to the console
 */
public class ConsoleMessageHandler implements MessageHandler {
    @Override
    public void send(Message message, ClientConfiguration clientConfiguration) {
        System.out.println("---- MESSAGE ----");
        System.out.println("From: " + message.getSenderAddress());
        System.out.println("From: " + message.getName());
        System.out.println("Message: " + message.getMessage());
        System.out.println("ClientRealmPK: " + clientConfiguration.getPublicKey());
        System.out.println("ClientName: " + clientConfiguration.getName());
        System.out.println("ClientEmail: " + clientConfiguration.getMailTo());
    }
}
```


### Adding Endpoints

Create a class that implements HttpHandler and then call server.get/post("", Handler). Here's an example:

Note: The default HTTP Server is Javalin, but has some extended functionality. You can also swap out the HTTP server to something else by extending HttpServer and HttpContext.

```
public class ExampleHandler implements HttpHandler {
    private static Logger logger = LoggerFactory.getLogger(TelemetryHandler.class.getName());
    private AppConfig config;

    public TelemetryHandler(AppConfig configuration) {
        logger.info("Loading Example Handler");

        config = configuration;
    }

    @Override
    public void Handle(HttpContext ctx) {
        if (!ctx.checkParamExists("test_parameter")){
            ctx.badRequest();
            return;
        }

        ctx.result("Hello,  World");
    }
```
