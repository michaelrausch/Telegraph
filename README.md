# Telegraph
[![Build Status](https://travis-ci.org/michaelrausch/michaelrausch.nz-contact-service.svg?branch=master)](https://travis-ci.org/michaelrausch/michaelrausch.nz-contact-service)
[![Maintainability](https://api.codeclimate.com/v1/badges/01b62ede33372d1c44f7/maintainability)](https://codeclimate.com/github/michaelrausch/michaelrausch.nz-contact-service/maintainability)
[![codecov](https://codecov.io/gh/michaelrausch/michaelrausch.nz-contact-service/branch/master/graph/badge.svg)](https://codecov.io/gh/michaelrausch/michaelrausch.nz-contact-service)

This is a new WIP version of the contact form API used on [https://michaelrausch.nz]() and a few other websites. 

### Improvements over the last version
- Messages can also be saved to a file or a SQL database (WIP).
- Improved fault tolerance (WIP).
- Looking into IFTTT support (WIP).
- +more

### Setup
**Note: This is still a WIP and probably shouldn't be used in production yet.**

**1. Clone this repo**

``
    git clone https://github.com/michaelrausch/michaelrausch.nz-contact-service.git
``

**2. Rename config.example.yaml to config.yaml**

**3. Get an API key for Mailjet**

Note: Currently, only Mailjet is supported

**3. Paste your API keys into config.yaml and fill out the rest of your information**

**4. Build the jar**

``
./gradlew fatJar
``

**5. Place your config.yaml in the same directory as the JAR and run**

