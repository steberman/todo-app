package dev.steberman.todo.registration;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;

@Controller
@RequestMapping("/register")
public class RegistrationController {

  private static final String ATTR_MESSAGE_TYPE = "messageType";

  private static final String ATTR_MESSAGE = "message";

  private static final String REGISTER_VIEW = "register";

  private static final String ATTR_REGISTRATION = "registration";

  private final RegistrationService registrationService;

  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @GetMapping
  public String getRegisterView(Model model) {
    model.addAttribute(ATTR_REGISTRATION, new Registration());
    return REGISTER_VIEW;
  }

  @PostMapping
  public String registerUser(@Valid Registration registration,
                             BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute(ATTR_REGISTRATION, registration);
      return REGISTER_VIEW;
    }

    try {
      registrationService.registerUser(registration);

      redirectAttributes.addFlashAttribute(ATTR_MESSAGE,
        "You successfully registered for the Todo App. " +
          "Please check your email inbox for further instructions."
      );
      redirectAttributes.addFlashAttribute(ATTR_MESSAGE_TYPE, "success");

      return "redirect:/";
    } catch (AWSCognitoIdentityProviderException exception) {

      model.addAttribute(ATTR_REGISTRATION, registration);
      model.addAttribute(ATTR_MESSAGE, exception.getMessage());
      model.addAttribute(ATTR_MESSAGE_TYPE, "danger");

      return REGISTER_VIEW;
    }
  }
}
