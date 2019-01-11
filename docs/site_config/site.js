// 全局的一些配置
export default {
  rootPath: '', // 发布到服务器的根目录，需以/开头但不能有尾/，如果只有/，请填写空字符串
  port: 8080, // 本地开发服务器的启动端口
  domain: 'txlcn.org', // 站点部署域名，无需协议和path等
  defaultSearch: 'baidu', // 默认搜索引擎，baidu或者google
  defaultLanguage: 'zh-cn',
  'en-us': {
    pageMenu: [
      {
        key: 'home', // 用作顶部菜单的选中
        text: 'HOME',
        link: '/en-us/index.html',
      },
      {
        key: 'docs',
        text: 'DOCS',
        link: '/en-us/docs/preface.html',
      },
      // {
      //   key: 'blog',
      //   text: 'BLOG',
      //   link: '/en-us/blog/index.html',
      // },
      // {
      //   key: 'community',
      //   text: 'COMMUNITY',
      //   link: 'https://meta.discoursecn.org/',
      //   target:'_blank',
      // },
    ],
    disclaimer: {
        title: 'Vision',
        content: 'We will devote ourselves to creating a fast, efficient and concurrent distributed transaction solution.',
    },
    documentation: {
      title: 'Documentation',
      list: [
        {
          text: 'Overview',
          link: '/en-us/docs/background.html',
        },
        {
          text: 'Quick start',
          link: '/en-us/docs/start.html',
        },
        {
          text: 'Developer guide',
          link: '/en-us/docs/demo/env.html',
        },
      ],
    },
    resources: {
      title: 'Friendship Link',
      list: [
        // {
        //   text: 'Blog',
        //   link: '/en-us/blog/index.html',
        // },
        {
          text: 'CodingApi',
            link: 'https://www.codingapi.com/',
            target:'_blank',
        },
      ],
    },
    copyright: 'Copyright © 2018 CodingApi',
  },
  'zh-cn': {
    pageMenu: [
      {
        key: 'home',
        text: '首页',
        link: '/zh-cn/index.html',
      },
      {
        key: 'docs',
        text: '文档',
        link: '/zh-cn/docs/preface.html',
      },
      // {
      //   key: 'blog',
      //   text: '博客',
      //   link: '/zh-cn/blog/index.html',
      // },
      // {
      //   key: 'community',
      //   text: '社区',
      //     link: 'https://meta.discoursecn.org/',
      //     target:'_blank',
      // },
    ],
    disclaimer: {
        title: '愿景',
        content: '我们将致力于打造一个快捷、高效、兼任性强的分布式事务解决方案',
    },
    documentation: {
      title: '文档',
      list: [
        {
          text: '概览',
          link: '/zh-cn/docs/background.html',
        },
        {
          text: '快速开始',
          link: '/zh-cn/docs/start.html',
        },
        {
          text: '开发者指南',
          link: '/zh-cn/docs/demo/env.html',
        },
      ],
    },
    resources: {
      title: '友情链接',
      list: [
        // {
        //   text: '博客',
        //   link: '/zh-cn/blog/index.html',
        // },
        {
          text: 'CodingApi',
            link: 'https://www.codingapi.com/',
            target:'_blank',
        },
      ],
    },
    copyright: 'Copyright © 2018-2019 CodingApi',
  },
};
