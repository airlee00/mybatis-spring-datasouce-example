package com.mybatis.sample.config;

import java.util.Map;

public class DataSourceVo {
	private String driverClassName;// org.postgresql.Driver
	private String jndi;// jdbc/bizDataSource01
	private String url;// jdbc;//postgresql;////localhost;//5432/devfwdb
	private String username;// fwapp
	private String password;// ENC(33cSwxJmy8LFNRCrPu4rMUMkddDDj13i)
	private String name;// SLT-PG-DataSource01,SCP-PG-DataSource01
	
	private Map<String, String> pool;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJndi() {
		return jndi;
	}

	public void setJndi(String jndi) {
		this.jndi = jndi;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Map<String, String> getPool() {
        return pool;
    }

    public void setPool(Map<String, String> pool) {
        this.pool = pool;
    }

    @Override
    public String toString() {
        return "DataSourceVo [driverClassName=" + driverClassName + ", jndi=" + jndi + ", url=" + url + ", username=" + username + ", password="
                + password + ", name=" + name + ", pool=" + pool + "]";
    }


}
