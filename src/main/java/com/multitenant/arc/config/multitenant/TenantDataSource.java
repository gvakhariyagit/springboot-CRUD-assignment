package com.multitenant.arc.config.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;

@Component
public class TenantDataSource {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${db.base.url}")
    private String dbBaseUrl;
    
    public static final String tenantID[] = {"schema"};


    private HashMap<String, DataSource> dataSources = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantDataSource.class);

    /**
     * This will return data source base on tenantId.
     * If will not find data source for tenantId, then it will create new one, store it and return it.
     * */
    public DataSource getDataSource(String tenantId) {
    	if (dataSources.get(tenantId) != null) {
            LOGGER.info("Selected existing data source for tenant id : {}", tenantId);
            return dataSources.get(tenantId);
        }
        DataSource dataSource = createDataSource(tenantId);
        if (dataSource != null) {
            dataSources.put(tenantId, dataSource);
        }
        return dataSource;
    }


    private DataSource createDataSource(String tenantId) {
    	
    	tenantID[0] = tenantID[0].replace("schema", tenantId);
            LOGGER.info("Creating datasource for tenant id : {}", tenantId);
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(driverClassName)
                    .username(userName)
                    .password(password)
                 //   .url(dbBaseUrl + "/" + tenantId)
                    .url(dbBaseUrl);
            
            DataSource ds = factory.build();
            
      //      org.springframework.core.io.Resource initSchema = new ClassPathResource(tenantId+"_schema.sql");
      //      DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
    //        DatabasePopulatorUtils.execute(databasePopulator, ds);
     

            return ds;

    }
}
