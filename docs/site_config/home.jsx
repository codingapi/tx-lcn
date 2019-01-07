import React from 'react';

export default {
  'zh-cn': {
    brand: {
      brandName: 'TX-LCN分布式事务框架',
      briefIntroduction: 'LCN并不生产事务，LCN只是本地事务的协调工',
      buttons: [
        {
          text: '立即开始',
          link: '/zh-cn/docs/demo.html',
          type: 'primary',
        },
        {
          text: '查看Github',
          link: 'https://github.com/codingapi/tx-lcn',
          type: 'normal',
        },
      ],
    },
    introduction: {
      title: '高性能的分布式事务框架',
      desc: '兼容dubbo、springcloud框架，支持RPC框架拓展，支持各种ORM框架、NoSQL、负载均衡、事务补偿',
      img: '/img/architecture.png',
    },
    features: {
      title: '特性一览',
      list: [
        {
          img: '/img/feature_transpart.png',
          title: '一致性',
          content: '通过TxManager协调控制与事务补偿机制确保数据一致性',
        },
        {
          img: '/img/feature_loadbalances.png',
          title: '高可用',
          content: '项目模块不仅可高可用部署，事务协调器也可集群化部署',
        },
        {
          img: '/img/feature_service.png',
          title: '易用性',
          content: '仅需要在业务方法上添加@TxTransaction注解即可',
        },
        {
          img: '/img/feature_hogh.png',
          title: '扩展性',
          content: '支持各种RPC框架扩展，支持通讯协议与事务模式扩展',
        },
      ],
    },
    start: {
      title: '快速开始',
      desc: <p> <br /> 简单加几个注解在协作的微服务上，即可享受TXLCN带来的好处！ <br /> <br /> </p>,
      img: '/img/quick_start.png',
      button: {
        text: '好了，开始吧',
        link: '/zh-cn/docs/start.html',
      },
    },
    users: {
      title: '用户',
      desc: <span>目前采用的客户</span>,
      list: [
        '/img/users_alibaba.png','/img/users_alibaba.png',
      ],
    },
  },
  'en-us': {
    brand: {
      brandName: 'brandName',
      briefIntroduction: 'some description of product',
      buttons: [
        {
          text: 'Quick Start',
          link: '/en-us/docs/demo1.html',
          type: 'primary',
        },
        {
          text: 'View on Github',
          link: '',
          type: 'normal',
        },
      ],
    },
    introduction: {
      title: 'introduction title',
      desc: 'some introduction of your product',
      img: '/img/architecture.png',
    },
    features: {
      title: 'Feature List',
      list: [
        {
          img: '/img/feature_transpart.png',
          title: 'feature1',
          content: 'feature description',
        },
        {
          img: '/img/feature_loadbalances.png',
          title: 'feature2',
          content: 'feature description',
        },
        {
          img: '/img/feature_service.png',
          title: 'feature3',
          content: 'feature description',
        },
        {
          img: '/img/feature_hogh.png',
          title: 'feature4',
          content: 'feature description',
        },
        {
          img: '/img/feature_runtime.png',
          title: 'feature5',
          content: 'feature description',
        },
        {
          img: '/img/feature_maintenance.png',
          title: 'feature6',
          content: 'feature description',
        }
      ]
    },
    start: {
      title: 'Quick start',
      desc: 'some description text',
      img: '/img/quick_start.png',
      button: {
        text: 'READ MORE',
        link: '/en-us/docs/demo1.html',
      },
    },
    users: {
      title: 'users',
      desc: <span>some description</span>,
      list: [
        '/img/users_alibaba.png',
      ],
    },
  },
};
