import * as cdk from "aws-cdk-lib";
import { BuildConfig } from "./app-env-vars";

export interface AwsEnvStackProps extends cdk.StackProps {
  config: Readonly<BuildConfig>;
}
