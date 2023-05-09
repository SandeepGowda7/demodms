//package com.lti.demodms.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//import com.arangodb.ArangoDB;
//import com.arangodb.springframework.annotation.EnableArangoRepositories;
//import com.arangodb.springframework.config.ArangoConfiguration;
//
//@Configuration
//@EnableJpaRepositories(basePackages = {"com.lti.demodm"})
//public class ArangoConfig implements ArangoConfiguration {
//	
//	@Override
//	public ArangoDB.Builder arango() {
//	    return new ArangoDB.Builder()
//	      .host("127.0.0.1", 8529)
//	      .user("root").password("test"); 
//	}
//	
//	@Override
//	public String database() {
//		// TODO Auto-generated method stub
//		return "ConvergedOps";
//	}
//	
//}
