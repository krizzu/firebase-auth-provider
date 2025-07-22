# Firebase Auth Provider

![Kotlin version](https://img.shields.io/badge/kotlin-2.2.0-blue?logo=kotlin)
![Ktor version](https://img.shields.io/badge/ktor-3.2.2-red?logo=ktor)
![License](https://img.shields.io/github/license/krizzu/firebase-auth-provider)
![Maven Central](https://img.shields.io/maven-central/v/com.kborowy/firebase-auth-provider)

Ktor authentication provider for Firebase Auth module.

## Installation

![maven central latest](https://img.shields.io/maven-central/v/com.kborowy/firebase-auth-provider?label=Latest%20version&color=green)

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.kborowy:firebase-auth-provider:VERSION")
}
```

## Usage

A [Firebase](https://firebase.google.com/) project
with [Authentication module](https://firebase.google.com/products/auth) is required.
See [sample project](./sample/README.md) to learn more.

```kotlin
install(Authentication) {
    firebase("my-auth") {
        realm = "My Server"

        setup {
            // required, see configuration below 
        }

        validate { token ->
            UserIdPrincipal(token.uid)
        }
    }
}
```

## Configuration

The authentication provider requires initialization of a FirebaseApp instance. You can either:

1. Provide an existing FirebaseApp instance, or

2. Initialize a new instance using a service account file

### Existing FirebaseApp instance

Provide a pre-configured FirebaseApp instance:

```kotlin
firebase("auth-firebase") {
    setup {
        firebaseApp = FirebaseApp.getInstance()
    }
}
```

### Initialize FirebaseApp with admin file

Provide a service account JSON file OR an `InputStream` containing the service account credentials.
Optionally specify a `firebaseAppName` to identify your Firebase instance.

```kotlin
firebase(name = "auth-firebase", firebaseAppName = "my-fb-app") {
    setup {
        // Choose one initialization method:

        adminFile = File("path/to/admin/file.json")
        // OR
        adminFileStream = myFile.inputStream()
    }
}

```

### Authentication Parameters

| **Param** | **Required** | **Description**                                                                                                                                                                                                                          |
|-----------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| validate  | Required     | Lambda receiving decoded and verified [FirebaseToken](https://firebase.google.com/docs/reference/admin/java/reference/com/google/firebase/auth/FirebaseToken), expected to return Principal, if user is authorized or null otherwise.    |
| realm     | Optional     | String describing the protected area or the scope of protection. This could be a message like "Access to the staging site" or similar, so that the user knows to which space they are trying to get access to. Defaults to "Ktor Server" |

# License

    Copyright 2023 Krzysztof Borowy
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
