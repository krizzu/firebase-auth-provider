# Releasing

1. Change version in `gradle.properties` to release version
2. Change version in README.md, Download section
3. Commit changes and create tag (vX.X.X)
4. Push to repo
5. Release via `./gradlew publish`