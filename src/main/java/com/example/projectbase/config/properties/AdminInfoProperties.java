package com.example.projectbase.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin")
@Getter
@Setter
public class AdminInfoProperties {

  private String email;
  private String password;
  private String name;

}
