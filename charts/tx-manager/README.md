# tx-lcn

[tx-lcn](http://www.txlcn.org/) is one of the best distributed transaction framework and at least in China. Easy playing with SpringCloud、Dubbo and other RPC frameworks by useful rpc extensions，and support any JDBC transaction.

## TL;DR;

```bash
$ helm install yizhishang/tx-manager
```

By default this chart install one master pod containing redis master container and sentinel container, 2 sentinels and 1 redis slave.

## Introduction

This chart bootstraps a [tx-manager](https://github.com/yizhishang/tx-lcn) deployment on a [Kubernetes](http://kubernetes.io) cluster using the [Helm](https://helm.sh) package manager.

## Prerequisites

- Consistency
- High Availability
- High Accessibility
- High Expansibility

## Installing the Chart

To install the chart

```bash
$ helm install yizhishang/tx-manager --name tx-manager
```

The command deploys Redis on the Kubernetes cluster in the default configuration. By default this chart install one master pod containing redis master container and sentinel container, 2 sentinels and 1 redis slave. The [configuration](#configuration) section lists the parameters that can be configured during installation.

> **Tip**: List all releases using `helm list`

## Uninstalling the Chart

To uninstall/delete the deployment:

```bash
$ helm del --purge tx-manager
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Appliance mode

This chart can be used to launch Redis in a black box appliance mode that you can think of like a managed service. To run as an appliance, change the service type for the master and slave LBs to enable local access from within the K8S cluster.

To launch in VPC-only appliance mode, set appliance.serviceType to "LoadBalancer". If using appliance mode in Google Cloud, set appliance.annotations to:
`cloud.google.com/load-balancer-type:Internal`

```bash
$ helm install set="--spring.redis.host=192.168.110.119 --tx-lcn.manager.host=192.168.110.119" yizhishang/tx-manager
```

## Configuration

The following table lists the configurable parameters of the Redis chart and their default values.

| Parameter                        | Description                                                                                                                  | Default                                                                                                       |
| -------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| `service.parmas`                 | Application parameter                                                                                                        | `--spring.redis.host=192.168.110.119 --spring.redis.database=0 --tx-lcn.manager.host=192.168.110.119`         |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`. For example,

```bash
$ helm install yizhishang/tx-manager --name tx-manager
```

The above command sets the Redis server within  `default` namespace.

Alternatively, a YAML file that specifies the values for the parameters can be provided while installing the chart. For example,

```bash
$ helm install -f values.yaml yizhishang/tx-manager
```

> **Tip**: You can use the default [values.yaml](values.yaml)

## Internals
To see the pod roles, run the following:

```bash
$ kubectl get pods -L redis-role
```

