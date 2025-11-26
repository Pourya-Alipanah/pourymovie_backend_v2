package com.pourymovie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("PouryMovie API")
                    .description("Use the base API URL as http://localhost:1406/api/v2 just for local development")
                    .version("2.0"))
            .addServersItem(new Server().url("http://localhost:1406/api/v2"))
            .addServersItem(new Server().url("https://api.pourymovie.ir/api/v2"))
            .addSecurityItem(new SecurityRequirement().addList("access-token"));
  }

  @Bean
  public OperationCustomizer pageableOperationCustomizer() {
    return (operation, handlerMethod) -> {
      for (var param : handlerMethod.getMethodParameters()) {
        if (param.getParameterType().equals(Pageable.class)) {
          operation.getParameters().forEach(parameter -> {
            if ("size".equals(parameter.getName())) {
              parameter.getSchema().setDefault(10);
            }
          });
        }
      }
      return operation;
    };
  }
}
