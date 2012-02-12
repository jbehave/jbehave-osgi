Authentication Support Story

Narrative:
In order to secure the business data used in the company
As the stack holder
I want that only authorized users be able to use the application

Scenario: Not authorized user must be forwarded to login


Scenario: Authorized user logs in the application

Given the user is on home page
And there is no active session
When the user calls the application using the URL "http://localhost:8888"
Then the login page is showed

When the user authenticate with a userID <userID> and password <password>
Then the AuthenticatedHome page is showed
!-- And the welcome message <welcome> is presented  
!-- And the user main role <role> is presented   

Examples:
|role|userID|password|welcome|
|admin|admin|admin|Welcome admin|
|-- user|danilo|demo|Welcome danilo --|
|-- manager|cvgaviao|demo|Welcome cvgaviao --|
|-- accounting|lana|demo|Welcome lana --|



