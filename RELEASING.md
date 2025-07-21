# Releasing

## Prep work

1. Run `./gradlew apiCheck` to check breaking for breaking changes
2. Run `./gradlew spotlessCheck` to check for styling issues

## New version

1. Update version in `gradle.properties`
2. Commit changes ("release vX.X.X") and create tag ("vX.X.X")
3. Push commit and tag to repo
4. Release via `./gradlew publish`
5. Create GH release