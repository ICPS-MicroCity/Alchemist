var publishCmd = `
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md
git push --force origin \${nextRelease.version} || exit 6
sed 's/version\\s*=\\s*".*"\\s*$/version = "\${nextRelease.version}"/' -i src/main/hugo/config.toml
git -C build/website/ add . || exit 1
git -C build/website/ commit -m "chore: update website to version \${nextRelease.version}" || exit 2
git -C build/website/ push || exit 3
./gradlew shadowJar --parallel || ./gradlew shadowJar --parallel || exit 4
./gradlew releaseKotlinMavenOnMavenCentralNexus --parallel || exit 5
./gradlew publishKotlinMavenPublicationToGithubRepository --continue || true
`
var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    ["@semantic-release/exec", {
        "publishCmd": publishCmd,
    }],
    ["@semantic-release/github", {
        "assets": [ 
            { "path": "build/shadow/*-all.jar" },
         ]
    }],
    "@semantic-release/git",
)
module.exports = config
