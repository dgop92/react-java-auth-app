import { Construct } from "constructs";
import * as cdk from "aws-cdk-lib";
import * as ec2 from "aws-cdk-lib/aws-ec2";
import * as rds from "aws-cdk-lib/aws-rds";
import * as logs from "aws-cdk-lib/aws-logs";
import { getCloudFormationID, getResourceName } from "../../config/utils";
import { AwsEnvStackProps } from "../../config/custom-types";

export interface RDSStackProps extends AwsEnvStackProps {
  vpc: ec2.Vpc;
  rdsPgSecurityGroup: ec2.SecurityGroup;
}

export class RDSStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props: RDSStackProps) {
    super(scope, id, props);

    const { vpc, rdsPgSecurityGroup } = props;

    new rds.DatabaseInstance(this, getCloudFormationID(id, "db"), {
      instanceIdentifier: getResourceName(id, "db"),
      vpc,
      vpcSubnets: { subnets: vpc.publicSubnets },
      multiAz: false,
      // Just for testing purposes, we are making it publicly accessible and
      // allow any ip to connect to the database.
      securityGroups: [rdsPgSecurityGroup],
      publiclyAccessible: true,
      port: 5432,
      engine: rds.DatabaseInstanceEngine.postgres({
        version: rds.PostgresEngineVersion.VER_13_14,
      }),
      instanceType: ec2.InstanceType.of(
        ec2.InstanceClass.BURSTABLE3,
        ec2.InstanceSize.MICRO
      ),
      allocatedStorage: 20,
      storageType: rds.StorageType.GP2,
      credentials: rds.Credentials.fromPassword(
        "mydbadmin",
        cdk.SecretValue.ssmSecure(
          `/${props.config.appName}/${props.config.env}/pgdb-password`
        )
      ),
      cloudwatchLogsRetention: logs.RetentionDays.ONE_WEEK,
      enablePerformanceInsights: true,
      backupRetention: cdk.Duration.days(2),
    });
  }
}
