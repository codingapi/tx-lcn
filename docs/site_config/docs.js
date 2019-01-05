export default {
    'en-us': {
        sidemenu: [
            {
                title: 'header title',
                children: [
                    {
                        title: 'demo1',
                        link: '/en-us/docs/demo1.html'
                    },
                    {
                        title: 'demo2',
                        link: '/en-us/docs/demo2.html'
                    },
                    {
                        title: 'dir',
                        opened: true,
                        children: [
                            {
                                title: 'demo3',
                                link: '/en-us/docs/dir/demo3.html',
                            }
                        ],
                    }
                ]
            }
        ],
        barText: 'Documentation',
    },
    'zh-cn': {
        sidemenu: [
            {
                title: '用户文档',
                children: [
                    {
                        title: '入门',
                        link: '/zh-cn/docs/preface.html',
                        opened: true,
                    },
                    {
                        title: '背景',
                        link: '/zh-cn/docs/background.html',
                        opened: true,
                    },
                    {
                        title: '快速开始',
                        link: '/zh-cn/docs/start.html',
                    },
                    {
                        title: '依赖',
                        link: '/zh-cn/docs/dependencies.html',
                    },
                    {
                        title: '示例',
                        children: [
                            {
                                title: 'dubbo示例',
                                link: '/zh-cn/docs/demo/dubbo.html',
                            },
                            {
                                title: 'springcloud示例',
                                link: '/zh-cn/docs/demo/springcloud.html',
                            },
                        ],
                    },
                    {
                        title: '原理介绍',
                        children: [
                            {
                                title: '控制原理',
                                link: '/zh-cn/docs/principle/control.html',
                            },
                            {
                                title: 'LCN模式',
                                link: '/zh-cn/docs/principle/lcn.html',
                            },
                            {
                                title: 'TCC模式',
                                link: '/zh-cn/docs/principle/tcc.html',
                            },
                            {
                                title: 'TXC模式',
                                link: '/zh-cn/docs/principle/txc.html',
                            },
                        ],
                    },
                    {
                        title: '配置手册',
                        children: [
                            {
                                title: 'TxClient配置',
                                link: '/zh-cn/docs/setting/client.html',
                            },
                            {
                                title: 'TxManager配置',
                                link: '/zh-cn/docs/setting/manager.html',
                            },
                            {
                                title: '集群与负载',
                                link: '/zh-cn/docs/setting/distributed.html',
                            },
                        ],
                    },
                    {
                        title: '扩展支持',
                        children: [
                            {
                                title: '事务模式扩展',
                                link: '/zh-cn/docs/expansion/transaction.html',
                            },
                            {
                                title: '通讯协议扩展',
                                link: '/zh-cn/docs/expansion/net.html',
                            },
                            {
                                title: 'RPC框架扩展',
                                link: '/zh-cn/docs/expansion/rpc.html',
                            },
                        ],
                    },
                    {
                        title: '通讯指令手册',
                        link: '/zh-cn/docs/communication.html',
                    },
                    {
                        title: 'TM管理手册',
                        link: '/zh-cn/docs/manageradmin.html',
                    },
                    {
                        title: '问题排查手册',
                        link: '/zh-cn/docs/debug.html',
                    },
                    {
                        title: '性能测试报告',
                        link: '/zh-cn/docs/test.html',
                    },
                    {
                        title: '开发者',
                        link: '/zh-cn/docs/developer.html',
                    },
                    {
                        title: 'FQA',
                        link: '/zh-cn/docs/fqa.html',
                    },
                ],
            },
        ],
        barText: '文档',
    },
};
