# Releasing

1. Update version in `gradle.properties`
2. Commit changes ("release vX.X.X") and create tag ("vX.X.X")
3. Push commit and tag to repo
4. Release via `./gradlew publish`
5. Create GH release