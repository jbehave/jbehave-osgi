Authentication Support Story

Narrative:
In order to secure the business data used
As the stack holder
I want that only authorized users uses the application

Scenario: Authorized user logs in the application

Given that application is running and reachable
When a not authenticated user enter the application
Then the login screen is showed

When the user with a userID <userID> and password <password> enter its credentials
Then the AuthenticatedHome page is showed
And the welcome message presented is <welcome> 
And the role status presented id <role>  

Examples:
|role|userID|password|welcome|
|admin|admin|admin|Welcome admin|
|user|danilo|demo|Welcome danilo|
|manager|cvgaviao|demo|Welcome cvgaviao|
|accounting|lana|demo|Welcome lana|


Scenario: Not authorized user must be blocked

