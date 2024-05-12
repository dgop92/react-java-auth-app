import Joi from "joi";

export interface UpdateProfileData {
  firstName?: string;
  lastName?: string;
}

export const UpdateProfileSchema = Joi.object({
  firstName: Joi.string().max(50).allow("").optional(),
  lastName: Joi.string().max(80).allow("").optional(),
}).meta({ className: "UpdateProfile" });
