import { getOsEnv } from "./env-utils";

export function loadEnvironmentVariables(envname: string) {
  if (["dev", "test", "prod"].includes(envname)) {
    require("dotenv").config({ path: `./env-vars/.${envname}.env` });
  } else {
    throw new Error(`Invalid environment name: ${envname}`);
  }

  const config = {
    env: getOsEnv("ENVIROMENT"),
    appName: getOsEnv("APP_NAME"),
    region: getOsEnv("AWS_REGION"),
    accountId: getOsEnv("AWS_ACCOUNT_ID"),
  };
  return config;
}

export type BuildConfig = ReturnType<typeof loadEnvironmentVariables>;
