package dev.steberman.todo.registration;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Registration {

  @NotBlank
  private String username;

  @Email
  private String email;

  @ValidInvitationCode
  private String invitationCode;

  
  @Override
  public String toString() {
    return "Registration{" +
      "username='" + username + '\'' +
      ", email='" + email + '\'' +
      ", invitationCode='" + invitationCode + '\'' +
      '}';
  }
}
