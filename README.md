# License Service

Licence services provides RESTFul API to check if an user is eligible to generate license for given content.

### Design Decision

#### Project Structure

Since the scope of this project involves only four entities, I opted for a flat package structure to maintain simplicity
and avoid unnecessary over-engineering. If the system's domain were more complex or expected to scale significantly, I
would adopt a domain-driven design (DDD) approach with separate sub-packages for each bounded context.

#### Eligibility Logic

The eligibility logic for content access can be determined by various criteria such as subscription status, user roles,
or purchase history. To keep the implementation straightforward for this assignment, I have focused on role-based and
purchase-based access control as the primary criteria for entitlement.

### Models

- User
    - id: UUID
    - email: String
    - password: String (bcrypt hash)
    - userRole: Role(User, Admin)
- Content
    - id: UUID
    - content: String
- PurchaseHistory
    - id: UUID
    - purchasedContents: [Content]
    - purchasedBy: User

### Docs

Visit: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
then, put `/v3/api-docs` in explore bar and press `explore` button.
