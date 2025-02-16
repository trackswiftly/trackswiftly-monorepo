# Advanced Liquibase Features and Best Practices

This README provides an overview of advanced Liquibase concepts and best practices to help you manage your database migrations efficiently in your Spring Boot projects.

---

## 1. Liquibase Contexts and Labels

### Contexts
Contexts allow you to apply changesets only in specific environments (e.g., dev, test, or prod).

**Example:**
```yaml
- changeSet:
    id: 1
    author: user
    context: dev
    changes:
      - createTable:
          tableName: test_table
```

### Labels
Labels allow for more complex targeting of changesets.

**Example:**
```yaml
- changeSet:
    id: 2
    author: user
    labels: production
```

Run specific labels:
```bash
./mvnw liquibase:update -Dliquibase.labels=production
```

---

## 2. Rollback Strategies
Rollbacks are critical for disaster recovery and controlled deployments. Liquibase allows you to define rollback procedures for each changeset.

**Rollback Example:**
```yaml
- changeSet:
    id: 3
    author: user
    changes:
      - addColumn:
          tableName: users
          columns:
            - column:
                name: age
                type: int
    rollback:
      - dropColumn:
          tableName: users
          columnName: age
```

Run rollback commands:
```bash
./mvnw liquibase:rollback -Dliquibase.tag=version_1.0
./mvnw liquibase:rollbackToDate 2025-02-01
```

---

## 3. Changelog Parameters
You can use parameters in changelogs for flexibility and reusability.

**In `application.properties`:**
```properties
liquibase.parameter.schema_name=my_schema
```

**In changelog:**
```yaml
- changeSet:
    id: 4
    author: user
    changes:
      - createTable:
          schemaName: ${schema_name}
          tableName: example_table
```

---

## 4. Multiple Changelog Files
Organize large projects using a master changelog that includes multiple files.

**Master changelog (`db.changelog-master.yml`):**
```yaml
databaseChangeLog:
  - include:
      file: db/changelog/initial_schema.yml
  - include:
      file: db/changelog/add_users_table.yml
```

---

## 5. Preconditions
Preconditions define conditions that must be met before executing a changeset.

**Example:**
```yaml
- changeSet:
    id: 5
    author: user
    preConditions:
      - onFail: MARK_RAN
      - tableExists:
          tableName: users
    changes:
      - addColumn:
          tableName: users
          columns:
            - column:
                name: last_login
                type: datetime
```

---

## 6. Custom SQL Scripts
You can embed or reference SQL in your changesets.

**Embedded SQL:**
```yaml
- changeSet:
    id: 6
    author: user
    changes:
      - sql: |
          INSERT INTO users (name, email) VALUES ('John Doe', 'john@example.com');
```

**External SQL File:**
```yaml
- changeSet:
    id: 7
    author: user
    changes:
      - sqlFile:
          path: db/scripts/populate_users.sql
```

---

## 7. Managing Data
Liquibase supports managing reference and seed data using `loadData` and `updateData` changes.

**Loading Data from CSV:**
```yaml
- changeSet:
    id: 8
    author: user
    changes:
      - loadData:
          file: db/data/users.csv
          tableName: users
```

---

## 8. Dynamic Data Type Mapping
Liquibase can map data types dynamically for different databases.

**Example:**
```yaml
- changeSet:
    id: 9
    author: user
    changes:
      - createTable:
          tableName: example_table
          columns:
            - column:
                name: created_at
                type: datetime
```

---

## 9. Database Snapshots
Liquibase can take a snapshot of your database structure for comparison or auditing purposes.

**Generate a snapshot:**
```bash
./mvnw liquibase:snapshot
```

---

## 10. Changelog Version Control (Tags)
Tags are useful for marking specific versions of your database.

**Tagging your database:**
```bash
./mvnw liquibase:tag -Dliquibase.tag=version_1.0
```

---

## 11. Advanced Rollbacks with `diffChangeLog`
You can generate a changelog based on differences between two databases or a database and a snapshot:

```bash
./mvnw liquibase:diffChangeLog
```

---

## 12. Liquibase Docker Support
Liquibase also offers a Docker image for easier integration with CI/CD pipelines:

```bash
docker run --rm -v $(pwd):/liquibase/changelog liquibase/liquibase update
```

---

## 13. Integration with CI/CD
- Automate database changes with Jenkins or GitHub Actions.
- Validate changelogs before deployment.

---

## 14. Using Checksums
Liquibase validates changesets using checksums to ensure they haven't been altered.

**Re-run changesets with modified checksums:**
```yaml
- changeSet:
    id: 10
    author: user
    runOnChange: true
    changes:
      - modifyDataType:
          columnName: age
          tableName: users
          newDataType: bigint
```

---

## 15. Best Practices
- **Changelog Naming:** Use descriptive names for changelog files and changesets.
- **Version Control:** Store changelogs in version control (e.g., Git).
- **Backup:** Always back up your database before running migrations.
- **Testing:** Test migrations in a staging environment before deploying to production.







### Versioned Naming Convention for Liquibase ChangeSet IDs

##### How to Structure ChangeSet IDs

```
<version>-<sequence>
```


**Example :**
```
databaseChangeLog:
  - changeSet:
      id: "1.0.0-1" # Version 1.0.0, first changeset
      author: obaid
      changes:
        - createTable:
            tableName: vehicle_type
            columns:
              - column:
                  name: id
                  type: bigint
                  constraints:
                    primaryKey: true
                    nullable: false

  - changeSet:
      id: "1.0.0-2" # Version 1.0.0, second changeset
      author: obaid
      changes:
        - createTable:
            tableName: vehicles
            columns:
              - column:
                  name: id
                  type: bigint
                  constraints:
                    primaryKey: true
                    nullable: false

  - changeSet:
      id: "1.1.0-1" # Version 1.1.0, first changeset
      author: obaid
      changes:
        - addColumn:
            tableName: vehicles
            columns:
              - column:
                  name: color
                  type: varchar(50)
```