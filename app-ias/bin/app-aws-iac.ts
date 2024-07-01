#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { loadEnvironmentVariables } from "../config/app-env-vars";
import { getStackName } from "../config/utils";
import { NetworkStack } from "../lib/network/vpc";
import { RDSStack } from "../lib/storage/rdsStack";

const app = new cdk.App();

function getConfig() {
  let envName = app.node.tryGetContext("config") as string;
  if (!envName) {
    throw new Error(
      "Context variable missing on CDK command. Pass in as `-c config=XXX`"
    );
  }

  const buildConfig = loadEnvironmentVariables(envName);
  return buildConfig;
}

const config = getConfig();

const networkStackName = getStackName(config.appName, "network", config.env);
const networkStack = new NetworkStack(app, networkStackName, {
  env: {
    region: config.region,
    account: config.accountId,
  },
  config: config,
});
cdk.Tags.of(networkStack).add("project:name", config.appName);
cdk.Tags.of(networkStack).add("project:env", config.env);
cdk.Tags.of(networkStack).add("project:stack", "network");

const rdsStackName = getStackName(config.appName, "rds", config.env);
const rdsStack = new RDSStack(app, rdsStackName, {
  env: {
    region: config.region,
    account: config.accountId,
  },
  config: config,
  vpc: networkStack.vpc,
  rdsPgSecurityGroup: networkStack.rdsPgSecurityGroup,
});
cdk.Tags.of(rdsStack).add("project:name", config.appName);
cdk.Tags.of(rdsStack).add("project:env", config.env);
cdk.Tags.of(rdsStack).add("project:stack", "rds");
