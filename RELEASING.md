# Releasing

## Prep work

1. Run `./gradlew apiCheck` to check breaking for breaking changes
2. Run `./gradlew spotlessCheck` to check for styling issues

## New version

1. Update version in `gradle.properties`
2. Commit changes with version update
3. Create annotated tag (`git tag -a vX.X.X -m "release vX.X.X"`)
4. Push commit and tag to repo (`git push tag vX.X.X`)
5. Release via `./gradlew publish`
6. Create GH release from tag