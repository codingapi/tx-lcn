// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import HeaderFooterLayout from './layouts/HeaderFooterLayout';
import Dashboard from './pages/Dashboard';
import Builder from './pages/Builder';
import Task from './pages/Task';
import Show from './pages/show';
import Resource from './pages/Resource';
import TxClient from './pages/TxClient';
import NotFound from './pages/NotFound';
// 登录页面
import Login from './pages/Login';
import MonitorDetail from './pages/MonitorDetail';

const routerConfig = [
  {
    path: '/',
    layout: HeaderFooterLayout,
    component: Dashboard,
  },
  {
    path: '/MonitorDetail',
    layout: HeaderFooterLayout,
    component: MonitorDetail,
  },
  {
    path: '/TxClient',
    layout: HeaderFooterLayout,
    component: TxClient,
  },
  {
    path: '/builder',
    layout: HeaderFooterLayout,
    component: Builder,
  },
  {
    path: '/task',
    layout: HeaderFooterLayout,
    component: Task,
  },
  {
    path: '/resource',
    layout: HeaderFooterLayout,
    component: Resource,
  },
  {
    path: '/show',
    layout: HeaderFooterLayout,
    component: Show,
  },
  {
    path: '/login',
    layout: '',
    component: Login,
  },
  {
    path: '*',
    layout: HeaderFooterLayout,
    component: NotFound,
  },
];

export default routerConfig;
