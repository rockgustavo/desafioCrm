package com.rockgustavo.desafiocrm.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Value("schema.sql")
    private ClassPathResource schemaScript;

    @Value("data.sql")
    private ClassPathResource dataScript;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void initializeDatabase() {
        try {
            // Verifica se a tabela existe
            jdbcTemplate.queryForObject("SELECT 1 FROM customer LIMIT 1", Integer.class);
            System.out.println("Tabela 'customer' foi encontrada. Não será executado os scripts SQL...");
        } catch (Exception e) {
            System.err.println("Tabela 'customer' não encontrada. Executando scripts SQL...");
            try {
                executeSqlScript(schemaScript);
                executeSqlScript(dataScript);
            } catch (RuntimeException ex) {
                System.err.println("Erro ao executar scripts de inicialização: " + ex.getMessage());
            }
        }
    }

    private void executeSqlScript(ClassPathResource scriptResource) {
        if (scriptResource.exists()) {
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, scriptResource);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar script SQL: " + scriptResource.getPath(), e);
            }
        } else {
            throw new RuntimeException("O script SQL não foi encontrado: " + scriptResource.getPath());
        }
    }

}
