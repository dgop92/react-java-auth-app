import axios from "axios";
import { APIError, ErrorCode } from "./errors";
import { firebaseAuth } from "../features/account/services/firebase-service";

export const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_BASE_API_URL,
  timeout: 6000,
});

axiosClient.interceptors.request.use(
  async (config) => {
    const token = await firebaseAuth.currentUser?.getIdToken();
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    // eslint-disable-next-line no-param-reassign
    config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const data = error.response.data;
    const apiErrorData = data;

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
