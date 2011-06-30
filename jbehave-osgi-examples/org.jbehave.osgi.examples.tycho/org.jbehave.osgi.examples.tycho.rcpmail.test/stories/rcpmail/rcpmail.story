Scenario: Ensure RCPmail is working properly

Given JBehave RCP Mail application is running

When user chooses menu item "Open Another Message View"
Then application shows another message view
