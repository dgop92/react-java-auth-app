import axios from "axios";
import { APIError, ErrorCode } from "./errors";

export const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_BASE_API_URL,
  timeout: 6000,
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const data = error.response.data;
    const apiErrorData = data?.apierror;

    if (apiErrorData === undefined) {
      return Promise.reject(error);
    }

    if (
      "message" in apiErrorData &&
      "debugMessage" in apiErrorData &&
      "timestamp" in apiErrorData &&
      "fieldErrors" in apiErrorData
    ) {
      return Promise.reject(
        new APIError(
          apiErrorData.message,
          apiErrorData.debugMessage,
          error.response.status,
          // for the moment, as the api is not returning the error level, we are setting it to unknown
          ErrorCode.UNKNOW_ERROR,
          apiErrorData.timestamp,
          apiErrorData.fieldErrors
        )
      );
    }

    return Promise.reject(error);
  }
);
