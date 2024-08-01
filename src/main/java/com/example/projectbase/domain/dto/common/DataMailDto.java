package com.example.projectbase.domain.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DataMailDto {
  private String from;
  private String to;
  private String[] cc;
  private String[] bcc;
  private String subject;
  private List<Object[]> body;
  private String[] attachments;
}