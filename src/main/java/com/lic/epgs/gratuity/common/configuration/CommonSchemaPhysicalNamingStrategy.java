package com.lic.epgs.gratuity.common.configuration;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@Component
public class CommonSchemaPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
	private final CommonConfigurationProperties schemaConfiguration;

    public CommonSchemaPhysicalNamingStrategy(CommonConfigurationProperties schemaConfiguration) {
        this.schemaConfiguration = schemaConfiguration;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if(name != null) {
            Identifier identifier = super.getIdentifier(schemaConfiguration.getConfig()
                    .get(name.getText()), name.isQuoted(), jdbcEnvironment);
            return super.toPhysicalSchemaName(identifier, jdbcEnvironment);
        }
        return name;
    }
}
