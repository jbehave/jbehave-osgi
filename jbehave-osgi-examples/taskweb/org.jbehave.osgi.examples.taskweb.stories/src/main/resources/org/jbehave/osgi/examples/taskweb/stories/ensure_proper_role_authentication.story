Authentication Support Story

Narrative:
In order to secure the business data used in the company
As the stack holder
I want that only authorized users be able to use the application

Scenario: Authorized user logs in the application

Given the user is on home page
And the login button is enabled
When the user clicks in login button
Then the login button is disabled
And the login dialog is presented


When the user authenticates with a userID <userID> and password <password>
Then the TaskManagement page is presented
And the user main role <role> description is presented 

When the user clicks in logout button
Then the user is forwarded to the home page
And the login button is enabled

Examples:
|role|userID|password|
|admin|admin|demo|
|-- consultant|dan|demo --|
|-- manager|cvgaviao|demo --|



Scenario: User cancels the login attempt

Given the user is on home page
And the login button is enabled
When the user clicks in login button
Then the login dialog is presented
And the login button is disabled

When the user chooses to cancel the login
Then the login dialog is closed
And the notification "Login canceled !" is displayed
And the login button is enabled




