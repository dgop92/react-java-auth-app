import { axiosClient } from "../../../common/axios-client";
import { UpdateProfileData } from "../../dashboard/schemas/update-profile-schema";
import { SignUpData } from "../schemas/signup-schema";

export class AccountRepository {
  async registerWithEmail(input: SignUpData): Promise<void> {
    const response = await axiosClient.post(
      "/api/v1/users/create-email-password",
      input
    );
    return response.data;
  }

  async updateProfile(input: UpdateProfileData): Promise<void> {
    const response = await axiosClient.patch("/api/v1/users/me", input);
    return response.data;
  }
}
