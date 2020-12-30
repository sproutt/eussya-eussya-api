/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Run CI") {
    startOn {
        gitPush { enabled = true }
        codeReviewOpened { enabled = true }
    }

    gradlew("clean build")
}