import { axiosClient } from "../../../common/axios-client";
import { SignUpData } from "../schemas/signup-schema";

export class AccountRepository {
  async registerWithEmail(input: SignUpData): Promise<void> {
    const response = await axiosClient.post(
      "/api/v1/users/create-email-password",
      input
    );
    return response.data;
  }
}
