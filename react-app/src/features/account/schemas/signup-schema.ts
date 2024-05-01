import Joi from "joi";

export interface SignUpData {
  email: string;
  password: string;
}

export const SignUpSchema = Joi.object({
  email: Joi.string()
    .email({ tlds: { allow: false } })
    .required()
    .messages({
      "string.email": "please enter a valid email address",
      "string.empty": "email is required",
    }),
  // Password must have at least 8 characters, an uppercase letter, a lowercase letter and a number
  password: Joi.string()
    .pattern(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$/)
    .required()
    .messages({
      "string.pattern.base":
        "password must have at least 8 characters, an uppercase letter, a lowercase letter and a number",
      "string.empty": "password is required",
    }),
}).meta({ className: "SignUp" });
