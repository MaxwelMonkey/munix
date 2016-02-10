dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache=false
    cache.use_query_cache=false
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost/munix"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = "password"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
//			dbCreate = "create"
//			url = "jdbc:mysql://localhost/munix_test"
//			driverClassName = "com.mysql.jdbc.Driver"
//			username = "root"
//			password = "root"
        }
    }
    production {
        dataSource {
//            jndiName = "java:comp/env/munixDatasource"
			dbCreate = "update"
			url = "jdbc:mysql://localhost/munix"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "root"
			password = "munix"
			properties {
				maxActive = 50
				maxIdle = 25
				minIdle = 5
				initialSize = 5
				minEvictableIdleTimeMillis = 60000
				timeBetweenEvictionRunsMillis = 60000
				maxWait = 10000
				validationQuery = "SELECT 1"
			}
	        }
    }
}
