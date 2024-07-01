# Insfrastruture As Code

## Useful commands

Network Stack in dev environment

```bash
cdk synth reactjava-network-prod-stack -c config=prod
cdk diff reactjava-network-prod-stack -c config=prod
cdk deploy reactjava-network-prod-stack -c config=prod
```

```bash
cdk synth reactjava-rds-prod-stack -c config=prod
cdk diff reactjava-rds-prod-stack -c config=prod
cdk deploy reactjava-rds-prod-stack -c config=prod
```
