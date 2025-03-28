package com.trackswiftly.client_service.utils;



import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trackswiftly.utils.exception.UnableToProccessIteamException;


public class TenantStatementInspector implements StatementInspector{
    

    private static final Logger logger = LoggerFactory.getLogger(TenantStatementInspector.class);

    @Override
    public String inspect(String sql) {
       logger.info("Generated SQL: {}", sql);

       String lowerSql = sql.toLowerCase();

       // Ignore sequence fetch queries and insert statements
       if (lowerSql.startsWith("select nextval") || lowerSql.startsWith("insert")) {
        return sql;
       }

        if (lowerSql.startsWith("select") && !lowerSql.contains("tenant_id")) {
            throw new UnableToProccessIteamException("Generated query does not contain tenant_id condition: " + sql);
        }

        return sql;

    }
}
