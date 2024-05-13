# Firebase Auth Provider

Ktor 2.0 authentication provider for Firebase Auth module.

## Download

```kotlin
repositories {
  mavenCentral()
}
dependencies {
  implementation("com.kborowy:firebase-auth-provider:1.1.1")
}
```

## Usage

You need to setup [Firebase](https://firebase.google.com/) project
with [Authentication module](https://firebase.google.com/products/auth) enabled. See [sample project](./sample/README.md) to learn more.

```kotlin
install(Authentication) {
    firebase("my-auth") {
        adminFile = File("path/to/admin/file.json")
        realm = "Sample Server"

        /**
         * A decoded and verified Firebase token.
         * Can be used to get the uid and other user attributes available in the token.
         */
        validate { token ->
            UserIdPrincipal(token.uid)
        }
    }
}
```

## API

| **Param** | **Required** | **Description**                                                                                                                                                                                                                          |
|-----------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| adminFile | Required | [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) instance, pointing to your Service account for your Firebase project. See [sample project](./sample/README.md) to learn more.                                        |
| validate  | Required | Lambda receiving decoded and verified [FirebaseToken](https://firebase.google.com/docs/reference/admin/java/reference/com/google/firebase/auth/FirebaseToken), expected to return Principal, if user is authorized or null otherwise.    |
| realm     | Optional | String describing the protected area or the scope of protection. This could be a message like "Access to the staging site" or similar, so that the user knows to which space they are trying to get access to. Defaults to "Ktor Server" |

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
