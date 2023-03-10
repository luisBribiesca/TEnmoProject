package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Data {
    private final DriverManagerDataSource ds = new DriverManagerDataSource();

    private JdbcTemplate jdbcTemplate;

    public Data() {
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        ds.setUsername("postgres");
        ds.setPassword("postgres1");
        jdbcTemplate = new JdbcTemplate(ds);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
