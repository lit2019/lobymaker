**Shelf Backend Assignment**

By **Mark Andrew**

**LobbyEntity**

|<a name="t.168f626faf8755817e4e84b9b51d122bd9b4a0"></a><a name="t.0"></a>**Column**|**Description**|**Data Type**|
| :-: | :-: | :-: |
|**id**|The unique identifier for the lobby.|Long|
|**title**|The title of the lobby.|String|
|**admin\_id**|The ID of the user who created the lobby.|Long|

**UserEntity**

|<a name="t.09d1633c098b0d02b1c246ba21ccb6748af558"></a><a name="t.1"></a>**Column**|          **Description**          |**Data Type**|
| :-: |:---------------------------------:| :-: |
|**id**|The unique identifier for the user.|Long|
|**username**|                             |String|
|**password**|                                   |String|

**InvitationEntity**

|<a name="t.f0a61b360aa58df5140291b3b4a661e5cf47d6"></a><a name="t.2"></a>**Column**|**Description**|**Data Type**|
| :-: | :-: | :-: |
|**id**|The unique identifier for the lobby-player mapping.|Long|
|**lobby\_id**|The ID of the lobby.|Long|
|**receiver\_id**|The ID of the receiver.|Long|
|**sender\_id**|The ID of the sender.|Long|
|**invite\_status**|The status of the invitation.|<p>InviteStatus</p><p>(ACCEPTED, DECLINED, SENT, REVOKED).</p>|

**Controller end points**

|<a name="t.735426611b18c11c1f52cb2a323735b9c27e83"></a><a name="t.3"></a>**URL**|**Input Datatype**|**Output Datatype**|
| :-: | :-: | :-: |
|/user/login|UserForm||
|/user/signup|UserForm||
|/lobby/create|LobbyForm (JSON)|void|
|/lobby/get/{isAdmin}|String (PathVariable)|List of LobbyData|
|/lobby/invite/{lobbyId}/{username}|Long, String (PathVariables)|void|
|/lobby/invitation/update/{inviteId}/{inviteStatus}|Long, InviteStatus (PathVariables)|void|
|/lobby/invitation/revoke/{inviteId}|Long (PathVariable)|void|
|/lobby/invitation/received|None|List of InvitationData|
|/lobby/invitation/sent|InviteStatus (RequestParam)|List of InvitationData|
|/lobby/members/{lobbyId}|Long (PathVariable)|List of UserEntity|

- **/lobby/create** creates a new lobby. The input data is a **LobbyForm** JSON object, which contains the name of the lobby.
- **/lobby/get/{isAdmin}** gets all lobbies in which the user is present, or all lobbies created by the current user if **isAdmin** is set to **true**.
- **/lobby/invite/{lobbyId}/{username}** invites a user to join a lobby. The input data is the ID of the lobby and the username of the user to invite.
- **/lobby/invitation/update/{inviteId}/{inviteStatus}** updates the status of an invitation. The input data is the ID of the invitation and the new status of the invitation.
- **/lobby/invitation/revoke/{inviteId}** revokes an invitation. The input data is the ID of the invitation.
- **/lobby/invitation/received** gets all received invitations for the current user.
- **/lobby/invitation/sent** gets all sent invitations for the current user, optionally filtered by status.

**Maven dependencies (we can also use Gradle too as an alternate)**

| Dependency                    | Usage                                                                                                    |
|-------------------------------|----------------------------------------------------------------------------------------------------------|
| spring-boot-starter-actuator   | Adds production-ready features to the application, such as health checks, metrics, and monitoring.     |
| spring-boot-starter-data-jpa   | Provides support for using Spring Data JPA to interact with a relational database using Java Persistence API (JPA). |
| spring-boot-starter-web        | Sets up a basic web application with Spring MVC, including embedded Tomcat for web server functionality.  |
| lombok                        | Simplifies Java code by providing annotations to automatically generate getters, setters, constructors, etc. during compile time. |
| spring-boot-devtools           | Provides development-time tools like automatic application restart, live reload, and more for faster development iterations. |
| mysql-connector-java           | MySQL Connector/J library for connecting to a MySQL database.                                           |
| spring-boot-starter-test       | Includes testing dependencies for unit and integration testing.                                        |
| h2                            | Embedded H2 database for testing and development purposes.                                             |
| spring-boot-starter-security   | Sets up Spring Security to secure the application with authentication and authorization.               |
| hibernate-core                 | The core Hibernate library for ORM (Object-Relational Mapping) functionality.                          |
| jjwt-api                      | JSON Web Token (JWT) API library for handling JWTs.                                                    |
| jjwt-impl                     | Implementation of the JWT API.                                                                        |
| jjwt-jackson                  | Provides Jackson integration for parsing and creating JSON Web Tokens.                                 |
| spring-boot-starter-thymeleaf | Integrates Thymeleaf templating engine for server-side rendering of web pages.                         |

