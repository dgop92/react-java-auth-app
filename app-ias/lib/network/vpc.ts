import { Construct } from "constructs";
import * as cdk from "aws-cdk-lib";
import * as ec2 from "aws-cdk-lib/aws-ec2";
import { getCloudFormationID, getResourceName } from "../../config/utils";
import { AwsEnvStackProps } from "../../config/custom-types";

export interface NetworkStackProps extends AwsEnvStackProps {}

export class NetworkStack extends cdk.Stack {
  public readonly vpc: ec2.Vpc;
  public readonly rdsPgSecurityGroup: ec2.SecurityGroup;

  constructor(scope: Construct, id: string, props: NetworkStackProps) {
    super(scope, id, props);

    this.vpc = new ec2.Vpc(this, getCloudFormationID(id, "vpc"), {
      vpcName: getResourceName(id, "vpc"),
      ipAddresses: ec2.IpAddresses.cidr("10.0.0.0/24"),
      maxAzs: 2,
      subnetConfiguration: [
        {
          name: "public",
          subnetType: ec2.SubnetType.PUBLIC,
          cidrMask: 26,
        },
      ],
    });

    // Create a security group for postgres rds instance
    this.rdsPgSecurityGroup = new ec2.SecurityGroup(
      this,
      getCloudFormationID(id, "rds-sg"),
      {
        vpc: this.vpc,
        allowAllOutbound: false,
        description: "Security group for RDS postgres instance",
        securityGroupName: getResourceName(id, "rds-pg-sg"),
      }
    );
    this.rdsPgSecurityGroup.addIngressRule(
      ec2.Peer.anyIpv4(),
      ec2.Port.tcp(5432),
      "Allow inbound traffic to postgres"
    );
  }
}
